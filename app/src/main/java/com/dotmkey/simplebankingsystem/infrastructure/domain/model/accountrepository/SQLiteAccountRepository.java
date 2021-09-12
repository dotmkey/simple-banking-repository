package com.dotmkey.simplebankingsystem.infrastructure.domain.model.accountrepository;

import com.dotmkey.simplebankingsystem.domain.model.Account;
import com.dotmkey.simplebankingsystem.domain.model.AccountRepository;

import java.sql.*;

public class SQLiteAccountRepository implements AccountRepository {

    private final Connection connection;

    public SQLiteAccountRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Account account) {
        if (this.ofCardNumber(account.cardNumber()) == null) {
            this.insert(account);
        } else {
            this.update(account);
        }
    }

    private void insert(Account account) {
        var query = "insert into card (number, hashed_pin, balance) values (?, ?, ?)";
        try (var statement = this.connection.prepareStatement(query)) {
            statement.setString(1, account.cardNumber());
            statement.setString(2, account.hashedCardPIN());
            statement.setLong(3, account.balance());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(Account account) {
        var query = "update card set balance = ? where number = ?";
        try (var statement = this.connection.prepareStatement(query)) {
            statement.setLong(1, account.balance());
            statement.setString(2, account.cardNumber());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Account ofCardNumber(String cardNumber) {
        var query = "select * from card where number = ? limit 1";
        try (var statement = this.connection.prepareStatement(query)) {
            statement.setString(1, cardNumber);
            var resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                return null;
            }

            return new Account(
                resultSet.getString("number"),
                resultSet.getString("hashed_pin"),
                resultSet.getInt("balance")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void remove(Account account) {
        var query = "delete from card where number = ?";
        try (var statement = this.connection.prepareStatement(query)) {
            statement.setString(1, account.cardNumber());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
