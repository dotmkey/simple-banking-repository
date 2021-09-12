package com.dotmkey.simplebankingsystem.domain.model.statement.exception;

public class AccountOfCardNumberDoesNotExistException extends RuntimeException {

    public AccountOfCardNumberDoesNotExistException(String cardNumber) {
        super(String.format("Account of card number %s does not exist", cardNumber));
    }
}
