package com.dotmkey.simplebankingsystem.domain.model.statement.exception;

public class CardNumberIsNotValidException extends RuntimeException {

    public CardNumberIsNotValidException(String cardNumber) {
        super(String.format("Card number %s is not valid", cardNumber));
    }
}
