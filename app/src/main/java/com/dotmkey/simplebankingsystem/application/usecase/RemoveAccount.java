package com.dotmkey.simplebankingsystem.application.usecase;

import com.dotmkey.simplebankingsystem.domain.model.Account;
import com.dotmkey.simplebankingsystem.domain.model.AccountService;

public class RemoveAccount {

    private final AccountService accountService;

    public RemoveAccount(AccountService accountService) {
        this.accountService = accountService;
    }

    public void execute(String cardNumber) {
        this.accountService.removeAccount(cardNumber);
    }

    public void execute(Account account) {
        this.accountService.removeAccount(account);
    }
}
