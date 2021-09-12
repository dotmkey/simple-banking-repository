package com.dotmkey.simplebankingsystem.application.usecase;

import com.dotmkey.simplebankingsystem.domain.model.Account;
import com.dotmkey.simplebankingsystem.domain.model.AccountService;

public class Transfer {

    private final AccountService accountService;

    public Transfer(AccountService accountService) {
        this.accountService = accountService;
    }

    public void execute(String cardNumberFrom, String cardNumberTo, long amount) {
        this.accountService.transfer(cardNumberFrom, cardNumberTo, amount);
    }

    public void execute(Account accountFrom, Account accountTo, long amount) {
        this.accountService.transfer(accountFrom, accountTo, amount);
    }
}
