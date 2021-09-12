package com.dotmkey.simplebankingsystem.domain.model.statement.exception;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String cardNumber, long amount) {
        super(String.format(
            "Not enough funds on balance of account with card number = %s to transfer amount = %d", cardNumber, amount
        ));
    }
}
