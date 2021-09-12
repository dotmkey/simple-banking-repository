package com.dotmkey.simplebankingsystem.domain.model;

import com.dotmkey.simplebankingsystem.domain.Assertion;
import com.dotmkey.simplebankingsystem.domain.DomainRegistry;
import com.dotmkey.simplebankingsystem.domain.model.statement.AccountCanAffordDebit;
import com.dotmkey.simplebankingsystem.domain.model.statement.AccountOfCardNumberExists;
import com.dotmkey.simplebankingsystem.domain.model.statement.TransferBetweenDifferentAccounts;

import java.util.Random;

public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public RawCredentials generate() {
        var random = new Random();

        String cardNumber;
        do {
            cardNumber = DomainRegistry.instance().cardNumberService().generate();
        } while (this.accountRepository.ofCardNumber(cardNumber) != null);

        var cardPIN = String.valueOf(random.nextInt(9999 - 1000 + 1) + 1000);

        var account = new Account(cardNumber, cardPIN);
        this.accountRepository.save(account);

        return new RawCredentials(cardNumber, cardPIN);
    }

    public void addIncome(String cardNumber, long income) {
        Assertion.assertA(new AccountOfCardNumberExists(cardNumber));

        var account = this.accountRepository.ofCardNumber(cardNumber);
        account.updateBalance(account.balance() + income);

        this.accountRepository.save(account);
    }

    public void transfer(String cardNumberFrom, String cardNumberTo, long amount) {
        Assertion.assertA(new TransferBetweenDifferentAccounts(cardNumberFrom, cardNumberTo));
        Assertion.assertA(new AccountOfCardNumberExists(cardNumberFrom));
        Assertion.assertA(new AccountOfCardNumberExists(cardNumberTo));

        var accountFrom = this.accountRepository.ofCardNumber(cardNumberFrom);
        var accountTo = this.accountRepository.ofCardNumber(cardNumberTo);

        Assertion.assertA(new AccountCanAffordDebit(accountFrom, amount));

        accountFrom.updateBalance(accountFrom.balance() - amount);
        accountTo.updateBalance(accountTo.balance() + amount);

        this.accountRepository.save(accountFrom);
        this.accountRepository.save(accountTo);
    }

    public void removeAccount(String cardNumber) {
        Assertion.assertA(new AccountOfCardNumberExists(cardNumber));

        this.accountRepository.remove(this.accountRepository.ofCardNumber(cardNumber));
    }
}
