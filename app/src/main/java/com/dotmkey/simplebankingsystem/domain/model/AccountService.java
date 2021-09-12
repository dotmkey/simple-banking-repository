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
        } while (this.accountRepository.ofCardNumber(cardNumber).isPresent());

        var cardPIN = String.valueOf(random.nextInt(9999 - 1000 + 1) + 1000);

        var account = new Account(cardNumber, cardPIN);
        this.accountRepository.save(account);

        return new RawCredentials(cardNumber, cardPIN);
    }

    public void addIncome(String cardNumber, long income) {
        Assertion.assertA(new AccountOfCardNumberExists(cardNumber));

        this.addIncome(this.accountRepository.ofCardNumber(cardNumber).orElseThrow(), income);
    }

    public void addIncome(Account account, long income) {
        account.updateBalance(account.balance() + income);

        this.accountRepository.save(account);
    }

    public void transfer(String cardNumberFrom, String cardNumberTo, long amount) {
        Assertion.assertA(new TransferBetweenDifferentAccounts(cardNumberFrom, cardNumberTo));
        Assertion.assertA(new AccountOfCardNumberExists(cardNumberFrom));
        Assertion.assertA(new AccountOfCardNumberExists(cardNumberTo));

        this.transfer(
            this.accountRepository.ofCardNumber(cardNumberFrom).orElseThrow(),
            this.accountRepository.ofCardNumber(cardNumberTo).orElseThrow(),
            amount
        );
    }

    public void transfer(Account accountFrom, Account accountTo, long amount) {
        Assertion.assertA(new AccountCanAffordDebit(accountFrom, amount));

        accountFrom.updateBalance(accountFrom.balance() - amount);
        accountTo.updateBalance(accountTo.balance() + amount);

        this.accountRepository.save(accountFrom);
        this.accountRepository.save(accountTo);
    }

    public void removeAccount(String cardNumber) {
        Assertion.assertA(new AccountOfCardNumberExists(cardNumber));

        this.removeAccount(this.accountRepository.ofCardNumber(cardNumber).orElseThrow());
    }

    public void removeAccount(Account account) {
        this.accountRepository.remove(account);
    }
}
