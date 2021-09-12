package com.dotmkey.simplebankingsystem.domain.model;

import java.util.Optional;

public interface AccountRepository {

    void save(Account account);

    Optional<Account> ofCardNumber(String cardNumber);

    void remove(Account account);
}
