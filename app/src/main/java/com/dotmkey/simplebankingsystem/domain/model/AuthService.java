package com.dotmkey.simplebankingsystem.domain.model;

import com.dotmkey.simplebankingsystem.domain.Assertion;
import com.dotmkey.simplebankingsystem.domain.model.statement.AccountOfCardNumberExists;
import com.dotmkey.simplebankingsystem.domain.model.statement.CardPINIsCorrectForAccount;

public class AuthService {

    private final AccountRepository accountRepository;

    public AuthService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account authenticate(String cardNumber, String cardPIN) {
        Assertion.assertA(new AccountOfCardNumberExists(cardNumber));

        Account account = this.accountRepository.ofCardNumber(cardNumber);

        Assertion.assertA(new CardPINIsCorrectForAccount(account, cardPIN));

        return account;
    }
}
