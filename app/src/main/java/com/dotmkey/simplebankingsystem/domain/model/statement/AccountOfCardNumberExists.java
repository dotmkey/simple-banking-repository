package com.dotmkey.simplebankingsystem.domain.model.statement;

import com.dotmkey.simplebankingsystem.domain.DomainRegistry;
import com.dotmkey.simplebankingsystem.domain.Statement;
import com.dotmkey.simplebankingsystem.domain.model.statement.exception.AccountOfCardNumberDoesNotExistException;

public class AccountOfCardNumberExists extends Statement {

    private final String cardNumber;

    public AccountOfCardNumberExists(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean check() {
        return DomainRegistry.instance().accountRepository().ofCardNumber(this.cardNumber) != null;
    }

    @Override
    protected RuntimeException exception() {
        return new AccountOfCardNumberDoesNotExistException(this.cardNumber);
    }
}
