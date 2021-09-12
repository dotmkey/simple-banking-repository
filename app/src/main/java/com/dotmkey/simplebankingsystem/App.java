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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
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
        resolveLogin();
        resolveAddIncome();
        resolveGetAccount();
        resolveTransfer();
        resolveRemoveAccount();
        resolveInteraction();
    }

    private static Connection resolveConnection() {
        if (!DIContainer.containsKey(Connection.class.getName())) {
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
                            "pin text not null, " +
                            "hashed_pin text not null," +
                            "balance integer not null default 0" +
                        ")"
                    );
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                DIContainer.putIfAbsent(Connection.class.getName(), connection);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return (Connection) DIContainer.get(Connection.class.getName());
    }

    private static AccountRepository resolveAccountRepository() {
        DIContainer.putIfAbsent(AccountRepository.class.getName(), new SQLiteAccountRepository(resolveConnection()));
        return (AccountRepository) DIContainer.get(AccountRepository.class.getName());
    }

    private static AccountService resolveAccountService() {
        DIContainer.putIfAbsent(AccountService.class.getName(), new AccountService(resolveAccountRepository()));
        return (AccountService) DIContainer.get(AccountService.class.getName());
    }

    private static AuthService resolveAuthService() {
        DIContainer.putIfAbsent(AuthService.class.getName(), new AuthService(resolveAccountRepository()));
        return (AuthService) DIContainer.get(AuthService.class.getName());
    }

    private static CardNumberService resolveCardNumberService() {
        DIContainer.putIfAbsent(CardNumberService.class.getName(), new CardNumberService());
        return (CardNumberService) DIContainer.get(CardNumberService.class.getName());
    }

    private static Hasher resolveHasher() {
        DIContainer.putIfAbsent(Hasher.class.getName(), new SHA512Hasher());
        return (Hasher) DIContainer.get(Hasher.class.getName());
    }

    private static DomainRegistry resolveDomainRegistry() {
        DomainRegistry.configure(resolveHasher(), resolveCardNumberService(), resolveAccountRepository());
        return DomainRegistry.instance();
    }

    private static SessionService resolveSessionService() {
        DIContainer.putIfAbsent(SessionService.class.getName(), new InMemorySessionService(resolveAccountRepository()));
        return (SessionService) DIContainer.get(SessionService.class.getName());
    }

    private static GenerateAccount resolveGenerateAccount() {
        DIContainer.putIfAbsent(GenerateAccount.class.getName(), new GenerateAccount(resolveAccountService()));
        return (GenerateAccount) DIContainer.get(GenerateAccount.class.getName());
    }

    private static Login resolveLogin() {
        DIContainer.putIfAbsent(Login.class.getName(), new Login(resolveAuthService(), resolveSessionService()));
        return (Login) DIContainer.get(Login.class.getName());
    }

    private static Logout resolveLogout() {
        DIContainer.putIfAbsent(Logout.class.getName(), new Logout(resolveSessionService()));
        return (Logout) DIContainer.get(Logout.class.getName());
    }

    private static GetCurrentAccount resolveGetCurrentAccount() {
        DIContainer.putIfAbsent(GetCurrentAccount.class.getName(), new GetCurrentAccount(resolveSessionService()));
        return (GetCurrentAccount) DIContainer.get(GetCurrentAccount.class.getName());
    }

    private static AddIncome resolveAddIncome() {
        DIContainer.putIfAbsent(AddIncome.class.getName(), new AddIncome(resolveAccountService()));
        return (AddIncome) DIContainer.get(AddIncome.class.getName());
    }

    private static GetAccount resolveGetAccount() {
        DIContainer.putIfAbsent(GetAccount.class.getName(), new GetAccount(resolveAccountRepository()));
        return (GetAccount) DIContainer.get(GetAccount.class.getName());
    }

    private static Transfer resolveTransfer() {
        DIContainer.putIfAbsent(Transfer.class.getName(), new Transfer(resolveAccountService()));
        return (Transfer) DIContainer.get(Transfer.class.getName());
    }

    private static RemoveAccount resolveRemoveAccount() {
        DIContainer.putIfAbsent(RemoveAccount.class.getName(), new RemoveAccount(resolveAccountService()));
        return (RemoveAccount) DIContainer.get(RemoveAccount.class.getName());
    }

    private static Interaction resolveInteraction() {
        DIContainer.putIfAbsent(
            Interaction.class.getName(),
            new Interaction(
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
        return (Interaction) DIContainer.get(Interaction.class.getName());
    }
}
