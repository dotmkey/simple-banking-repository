package com.dotmkey.simplebankingsystem.domain.model.statement;

import com.dotmkey.simplebankingsystem.domain.Statement;
import com.dotmkey.simplebankingsystem.domain.model.Account;
import com.dotmkey.simplebankingsystem.domain.model.statement.exception.IncorrectCardPINException;

public class CardPINIsCorrectForAccount extends Statement {

    private final Account account;
    private final String cardPIN;

    public CardPINIsCorrectForAccount(Account account, String cardPIN) {
        this.account = account;
        this.cardPIN = cardPIN;
    }

    @Override
    public boolean check() {
        return account.checkPIN(this.cardPIN);
    }

    @Override
    protected RuntimeException exception() {
        return new IncorrectCardPINException();
    }
}
