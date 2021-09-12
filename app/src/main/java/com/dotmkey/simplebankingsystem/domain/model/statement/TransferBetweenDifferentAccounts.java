package com.dotmkey.simplebankingsystem.domain.model.statement;

import com.dotmkey.simplebankingsystem.domain.Statement;
import com.dotmkey.simplebankingsystem.domain.model.Account;

public class TransferBetweenDifferentAccounts extends Statement {

    private final String cardNumberFrom;
    private final String cardNumberTo;

    public TransferBetweenDifferentAccounts(String cardNumberFrom, String cardNumberTo) {
        this.cardNumberFrom = cardNumberFrom;
        this.cardNumberTo = cardNumberTo;
    }

    public TransferBetweenDifferentAccounts(Account accountFrom, Account accountTo) {
        this.cardNumberFrom = accountFrom.cardNumber();
        this.cardNumberTo = accountTo.cardNumber();
    }

    @Override
    public boolean check() {
        return !this.cardNumberFrom.equals(this.cardNumberTo);
    }

    @Override
    protected RuntimeException exception() {
        return new TryingToTransferBetweenTheSameAccountsException();
    }

    public class TryingToTransferBetweenTheSameAccountsException extends RuntimeException {

        public TryingToTransferBetweenTheSameAccountsException() {
            super(String.format(
                "The sender and the receiver accounts have the same number = %s",
                TransferBetweenDifferentAccounts.this.cardNumberFrom
            ));
        }
    }
}
