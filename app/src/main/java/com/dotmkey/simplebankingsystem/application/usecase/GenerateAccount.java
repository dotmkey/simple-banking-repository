package com.dotmkey.simplebankingsystem.application.usecase;

import com.dotmkey.simplebankingsystem.domain.model.AccountService;
import com.dotmkey.simplebankingsystem.domain.model.RawCredentials;

public class GenerateAccount {

    private final AccountService accountService;

    public GenerateAccount(AccountService accountService) {
        this.accountService = accountService;
    }

    public RawCredentials execute() {
        return this.accountService.generate();
    }
}
