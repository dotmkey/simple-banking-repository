package com.dotmkey.simplebankingsystem.domain.model.statement;

import com.dotmkey.simplebankingsystem.domain.DomainRegistry;
import com.dotmkey.simplebankingsystem.domain.Statement;

public class AccountOfCardNumberExists extends Statement {

    private final String cardNumber;

    public AccountOfCardNumberExists(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean check() {
        return DomainRegistry.instance().accountRepository().ofCardNumber(this.cardNumber).isPresent();
    }

    @Override
    protected RuntimeException exception() {
        return new AccountOfCardNumberDoesNotExistException();
    }

    public class AccountOfCardNumberDoesNotExistException extends RuntimeException {

        public AccountOfCardNumberDoesNotExistException() {
            super(String.format("Account of card number %s does not exist", AccountOfCardNumberExists.this.cardNumber));
        }
    }
}
