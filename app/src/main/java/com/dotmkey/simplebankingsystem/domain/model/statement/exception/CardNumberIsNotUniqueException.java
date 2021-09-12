package com.dotmkey.simplebankingsystem.domain.model.statement.exception;

public class CardNumberIsNotUniqueException extends RuntimeException {

    public CardNumberIsNotUniqueException(String cardNumber) {
        super(String.format("Card number %s is not unique", cardNumber));
    }
}
