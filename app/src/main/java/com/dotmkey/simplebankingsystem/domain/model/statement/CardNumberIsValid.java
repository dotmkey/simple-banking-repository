package com.dotmkey.simplebankingsystem.domain.model.statement;

import com.dotmkey.simplebankingsystem.domain.DomainRegistry;
import com.dotmkey.simplebankingsystem.domain.Statement;

public class CardNumberIsValid extends Statement {

    private final String cardNumber;

    public CardNumberIsValid(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean check() {
        return DomainRegistry.instance().cardNumberService().isValid(this.cardNumber);
    }

    @Override
    protected RuntimeException exception() {
        return new CardNumberIsNotValidException();
    }

    public class CardNumberIsNotValidException extends RuntimeException {

        public CardNumberIsNotValidException() {
            super(String.format("Card number %s is not valid", CardNumberIsValid.this.cardNumber));
        }
    }
}
