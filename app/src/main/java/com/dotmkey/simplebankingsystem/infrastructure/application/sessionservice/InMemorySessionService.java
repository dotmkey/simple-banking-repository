package com.dotmkey.simplebankingsystem.infrastructure.application.sessionservice;

import com.dotmkey.simplebankingsystem.application.service.SessionService;
import com.dotmkey.simplebankingsystem.domain.model.Account;
import com.dotmkey.simplebankingsystem.domain.model.AccountRepository;

import java.util.Optional;

public class InMemorySessionService implements SessionService {

    private final AccountRepository accountRepository;
    private String curAccountNumber;

    public InMemorySessionService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void start(Account account) {
        this.curAccountNumber = account.cardNumber();
    }

    @Override
    public Optional<Account> account() {
        return this.curAccountNumber == null
            ? Optional.empty()
            : this.accountRepository.ofCardNumber(this.curAccountNumber);
    }

    @Override
    public void close() {
        this.curAccountNumber = null;
    }
}
