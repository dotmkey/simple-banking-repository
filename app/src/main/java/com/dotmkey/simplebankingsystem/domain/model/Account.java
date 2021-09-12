package com.dotmkey.simplebankingsystem.domain.model;

import com.dotmkey.simplebankingsystem.domain.Assertion;
import com.dotmkey.simplebankingsystem.domain.DomainRegistry;
import com.dotmkey.simplebankingsystem.domain.model.statement.CardNumberIsUnique;
import com.dotmkey.simplebankingsystem.domain.model.statement.CardNumberIsValid;

public final class Account {

    private final String cardNumber;
    private final String cardPIN;
    private final String hashedCardPIN;
    private Long balance = 0L;

    public Account(String cardNumber, String cardPIN) {
        Assertion.assertA(new CardNumberIsValid(cardNumber));
        Assertion.assertA(new CardNumberIsUnique(cardNumber));

        this.cardNumber = cardNumber;
        this.cardPIN = cardPIN;
        this.hashedCardPIN = DomainRegistry.instance().hasher().hash(cardPIN);
    }

    // infrastructure costructor (aka data mapper)
    public Account(String cardNumber, String cardPIN, String hashedCardPIN, long balance) {
        this.cardNumber = cardNumber;
        this.cardPIN = cardPIN;
        this.hashedCardPIN = hashedCardPIN;
        this.balance = balance;
    }

    public void updateBalance(long balance) {
        this.balance = balance;
    }

    public String cardNumber() {
        return this.cardNumber;
    }

    public String cardPIN() {
        return this.cardPIN;
    }

    public String hashedCardPIN() {
        return this.hashedCardPIN;
    }

    public Long balance() {
        return this.balance;
    }

    public boolean checkPIN(String cardPIN) {
        return DomainRegistry.instance().hasher().hash(cardPIN).equals(this.hashedCardPIN);
    }
}
