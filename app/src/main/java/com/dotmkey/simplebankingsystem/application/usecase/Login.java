package com.dotmkey.simplebankingsystem.application.usecase;

import com.dotmkey.simplebankingsystem.application.service.SessionService;
import com.dotmkey.simplebankingsystem.domain.model.Account;
import com.dotmkey.simplebankingsystem.domain.model.AuthService;

public class Login {

    private final AuthService authService;
    private final SessionService sessionService;

    public Login(AuthService authService, SessionService sessionService) {
        this.authService = authService;
        this.sessionService = sessionService;
    }

    public Account execute(String cardNumber, String cardPIN) {
        var account = this.authService.authenticate(cardNumber, cardPIN);
        this.sessionService.start(account);

        return account;
    }
}
