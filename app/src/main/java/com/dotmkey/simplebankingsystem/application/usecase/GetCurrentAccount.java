package com.dotmkey.simplebankingsystem.application.usecase;

import com.dotmkey.simplebankingsystem.application.service.SessionService;
import com.dotmkey.simplebankingsystem.domain.model.Account;

public class GetCurrentAccount {

    private final SessionService sessionService;

    public GetCurrentAccount(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public Account execute() {
        return this.sessionService.account();
    }
}
