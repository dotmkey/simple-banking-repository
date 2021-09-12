package com.dotmkey.simplebankingsystem.application.usecase;

import com.dotmkey.simplebankingsystem.domain.Assertion;
import com.dotmkey.simplebankingsystem.domain.model.Account;
import com.dotmkey.simplebankingsystem.domain.model.AccountRepository;
import com.dotmkey.simplebankingsystem.domain.model.statement.AccountOfCardNumberExists;

public class GetAccount {

    private final AccountRepository accountRepository;

    public GetAccount(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account execute(String cardNumber) {
        Assertion.assertA(new AccountOfCardNumberExists(cardNumber));

        return this.accountRepository.ofCardNumber(cardNumber).orElseThrow();
    }
}
