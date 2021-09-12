package com.dotmkey.simplebankingsystem.domain.model.statement;

import com.dotmkey.simplebankingsystem.domain.DomainRegistry;
import com.dotmkey.simplebankingsystem.domain.Statement;
import com.dotmkey.simplebankingsystem.domain.model.statement.exception.CardNumberIsNotUniqueException;

public class CardNumberIsUnique extends Statement {

    private final String cardNumber;

    public CardNumberIsUnique(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean check() {
        return DomainRegistry.instance().accountRepository().ofCardNumber(this.cardNumber) == null;
    }

    @Override
    protected RuntimeException exception() {
        return new CardNumberIsNotUniqueException(this.cardNumber);
    }
}
