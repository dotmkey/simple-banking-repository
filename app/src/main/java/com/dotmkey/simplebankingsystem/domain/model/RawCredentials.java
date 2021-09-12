package com.dotmkey.simplebankingsystem.domain.model;

public class RawCredentials {

    private final String cardNumber;
    private final String cardPIN;

    public RawCredentials(String cardNumber, String cardPIN) {
        this.cardNumber = cardNumber;
        this.cardPIN = cardPIN;
    }

    public String cardNumber() {
        return this.cardNumber;
    }

    public String cardPIN() {
        return this.cardPIN;
    }
}
