package com.dotmkey.simplebankingsystem.application.service;

import com.dotmkey.simplebankingsystem.domain.model.Account;

public interface SessionService {

    void start(Account account);

    Account account();

    void close();
}
