package com.dotmkey.simplebankingsystem.domain.model;

public interface AccountRepository {

    void save(Account account);

    Account ofCardNumber(String cardNumber);

    void remove(Account account);
}
