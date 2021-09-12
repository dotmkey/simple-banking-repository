package com.dotmkey.simplebankingsystem.application.usecase;

import com.dotmkey.simplebankingsystem.domain.model.AccountService;

public class AddIncome {

    private final AccountService accountService;

    public AddIncome(AccountService accountService) {
        this.accountService = accountService;
    }

    public void execute(String cardNumber, long income) {
        this.accountService.addIncome(cardNumber, income);
    }
}
