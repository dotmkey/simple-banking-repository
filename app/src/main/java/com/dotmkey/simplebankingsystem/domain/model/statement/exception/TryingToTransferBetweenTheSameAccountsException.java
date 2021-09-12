package com.dotmkey.simplebankingsystem.domain.model.statement.exception;

public class TryingToTransferBetweenTheSameAccountsException extends RuntimeException {

    public TryingToTransferBetweenTheSameAccountsException(String cardNumber) {
        super(String.format("The sender and the receiver accounts have the same number = %s", cardNumber));
    }
}
