package com.dotmkey.simplebankingsystem.application.service;

import com.dotmkey.simplebankingsystem.domain.model.Account;

import java.util.Optional;

public interface SessionService {

    void start(Account account);

    Optional<Account> account();

    void close();
}
