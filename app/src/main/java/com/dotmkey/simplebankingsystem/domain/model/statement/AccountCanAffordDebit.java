package com.dotmkey.simplebankingsystem.domain.model.statement;

import com.dotmkey.simplebankingsystem.domain.Statement;
import com.dotmkey.simplebankingsystem.domain.model.Account;
import com.dotmkey.simplebankingsystem.domain.model.statement.exception.InsufficientFundsException;

public class AccountCanAffordDebit extends Statement {

    private final Account account;
    private final long amount;

    public AccountCanAffordDebit(Account account, long amount) {
        this.account = account;
        this.amount = amount;
    }

    @Override
    public boolean check() {
        return this.account.balance() >= this.amount;
    }

    @Override
    protected RuntimeException exception() {
        return new InsufficientFundsException(this.account.cardNumber(), this.amount);
    }
}
