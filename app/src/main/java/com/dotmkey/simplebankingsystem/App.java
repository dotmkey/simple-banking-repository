package com.dotmkey.simplebankingsystem;

import com.dotmkey.simplebankingsystem.application.service.SessionService;
import com.dotmkey.simplebankingsystem.application.usecase.*;
import com.dotmkey.simplebankingsystem.domain.DomainRegistry;
import com.dotmkey.simplebankingsystem.domain.model.*;
import com.dotmkey.simplebankingsystem.infrastructure.application.sessionservice.InMemorySessionService;
import com.dotmkey.simplebankingsystem.infrastructure.domain.model.accountrepository.SQLiteAccountRepository;
import com.dotmkey.simplebankingsystem.infrastructure.domain.model.hasher.SHA512Hasher;
import com.dotmkey.simplebankingsystem.port.cmd.Interaction;
import org.sqlite.SQLiteDataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public class App {

    private final static HashMap<String, Object> DIContainer = new HashMap<>();
    private final static HashMap<String, Object> argContainer = new HashMap<>();

    public static void main(String[] args) {
        for (var i = 0; i < args.length; i++) {
            if (args[i].equals("-fileName") && i < args.length - 1) {
                argContainer.put("fileName", args[i + 1]);
            }
        }

        if (!argContainer.containsKey("fileName")) {
            throw new RuntimeException("Argument -fileName is required.");
        }

        resolveDI();

        var interaction = (Interaction) DIContainer.get(Interaction.class.getName());
        interaction.execute();
    }

    private static void resolveDI() {
        resolveConnection();
        resolveAccountRepository();
        resolveAccountService();
        resolveAuthService();
        resolveCardNumberService();
        resolveHasher();
        resolveDomainRegistry();
        resolveSessionService();
        resolveGenerateAccount();
        resolveGetCurrentAccount();
        resolveLogin();
        resolveLogout();
        resolveAddIncome();
        resolveGetAccount();
        resolveTransfer();
        resolveRemoveAccount();
        resolveInteraction();
    }

    private static Connection resolveConnection() {
        return (Connection) DIContainer.computeIfAbsent(
            Connection.class.getName(),
            k -> {
                var path = Paths.get((String) argContainer.get("fileName")).toAbsolutePath();
                if (!Files.exists(path)) {
                    try {
                        Files.createFile(path);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                var dataSource = new SQLiteDataSource();
                dataSource.setUrl("jdbc:sqlite:" + path);

                try {
                    var connection = dataSource.getConnection();
                    try (var statement = connection.createStatement()) {
                        statement.executeUpdate(
                            "create table if not exists card(" +
                                "id integer primary key, " +
                                "number text not null, " +
                                "hashed_pin text not null," +
                                "balance integer not null default 0" +
                                ")"
                        );
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    return connection;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        );
    }

    private static AccountRepository resolveAccountRepository() {
        return (AccountRepository) DIContainer.computeIfAbsent(
            AccountRepository.class.getName(),
            k -> new SQLiteAccountRepository(resolveConnection())
        );
    }

    private static AccountService resolveAccountService() {
        return (AccountService) DIContainer.computeIfAbsent(
            AccountService.class.getName(),
            k -> new AccountService(resolveAccountRepository())
        );
    }

    private static AuthService resolveAuthService() {
        return (AuthService) DIContainer.computeIfAbsent(
            AuthService.class.getName(),
            k -> new AuthService(resolveAccountRepository())
        );
    }

    private static CardNumberService resolveCardNumberService() {
        return (CardNumberService) DIContainer.computeIfAbsent(
            CardNumberService.class.getName(),
            k -> new CardNumberService()
        );
    }

    private static Hasher resolveHasher() {
        return (Hasher) DIContainer.computeIfAbsent(Hasher.class.getName(), k -> new SHA512Hasher());
    }

    private static DomainRegistry resolveDomainRegistry() {
        DomainRegistry.configure(resolveHasher(), resolveCardNumberService(), resolveAccountRepository());
        return DomainRegistry.instance();
    }

    private static SessionService resolveSessionService() {
        return (SessionService) DIContainer.computeIfAbsent(
            SessionService.class.getName(),
            k -> new InMemorySessionService(resolveAccountRepository())
        );
    }

    private static GenerateAccount resolveGenerateAccount() {
        return (GenerateAccount) DIContainer.computeIfAbsent(
            GenerateAccount.class.getName(),
            k -> new GenerateAccount(resolveAccountService())
        );
    }

    private static Login resolveLogin() {
        return (Login) DIContainer.computeIfAbsent(
            Login.class.getName(),
            k -> new Login(resolveAuthService(), resolveSessionService())
        );
    }

    private static Logout resolveLogout() {
        return (Logout) DIContainer.computeIfAbsent(
            Logout.class.getName(),
            k -> new Logout(resolveSessionService())
        );
    }

    private static GetCurrentAccount resolveGetCurrentAccount() {
        return (GetCurrentAccount) DIContainer.computeIfAbsent(
            GetCurrentAccount.class.getName(),
            k -> new GetCurrentAccount(resolveSessionService())
        );
    }

    private static AddIncome resolveAddIncome() {
        return (AddIncome) DIContainer.computeIfAbsent(
            AddIncome.class.getName(),
            k -> new AddIncome(resolveAccountService())
        );
    }

    private static GetAccount resolveGetAccount() {
        return (GetAccount) DIContainer.computeIfAbsent(
            GetAccount.class.getName(),
            k -> new GetAccount(resolveAccountRepository())
        );
    }

    private static Transfer resolveTransfer() {
        return (Transfer) DIContainer.computeIfAbsent(
            Transfer.class.getName(),
            k -> new Transfer(resolveAccountService())
        );
    }

    private static RemoveAccount resolveRemoveAccount() {
        return (RemoveAccount) DIContainer.computeIfAbsent(
            RemoveAccount.class.getName(),
            k -> new RemoveAccount(resolveAccountService())
        );
    }

    private static Interaction resolveInteraction() {
        return (Interaction) DIContainer.computeIfAbsent(
            Interaction.class.getName(),
            k -> new Interaction(
                resolveGenerateAccount(),
                resolveGetCurrentAccount(),
                resolveLogin(),
                resolveLogout(),
                resolveAddIncome(),
                resolveGetAccount(),
                resolveTransfer(),
                resolveCardNumberService(),
                resolveRemoveAccount()
            )
        );
    }
}
