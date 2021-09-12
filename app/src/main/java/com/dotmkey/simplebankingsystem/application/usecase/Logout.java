package com.dotmkey.simplebankingsystem.application.usecase;

import com.dotmkey.simplebankingsystem.application.service.SessionService;

public class Logout {

    private final SessionService sessionService;

    public Logout(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public void execute() {
        this.sessionService.close();
    }
}
