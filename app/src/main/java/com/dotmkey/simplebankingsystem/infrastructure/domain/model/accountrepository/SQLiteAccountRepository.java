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
        String query = "insert into card (number, pin, hashed_pin, balance) values (?, ?, ?, ?)";
        try (PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, account.cardNumber());
            statement.setString(2, account.cardPIN());
            statement.setString(3, account.hashedCardPIN());
            statement.setLong(4, account.balance());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(Account account) {
        String query = "update card set balance = ? where number = ?";
        try (PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setLong(1, account.balance());
            statement.setString(2, account.cardNumber());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Account ofCardNumber(String cardNumber) {
        String query = "select * from card where number = ? limit 1";
        try (PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, cardNumber);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                return null;
            }

            return new Account(
                resultSet.getString("number"),
                resultSet.getString("pin"),
                resultSet.getString("hashed_pin"),
                resultSet.getInt("balance")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void remove(Account account) {
        String query = "delete from card where number = ?";
        try (PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, account.cardNumber());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
