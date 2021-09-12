package com.dotmkey.simplebankingsystem.port.cmd;

import com.dotmkey.simplebankingsystem.application.usecase.*;
import com.dotmkey.simplebankingsystem.domain.model.Account;
import com.dotmkey.simplebankingsystem.domain.model.CardNumberService;
import com.dotmkey.simplebankingsystem.domain.model.RawCredentials;
import com.dotmkey.simplebankingsystem.domain.model.statement.AccountCanAffordDebit;
import com.dotmkey.simplebankingsystem.domain.model.statement.AccountOfCardNumberExists;
import com.dotmkey.simplebankingsystem.domain.model.statement.CardPINIsCorrectForAccount;
import com.dotmkey.simplebankingsystem.domain.model.statement.TransferBetweenDifferentAccounts;

import java.util.Scanner;

public class Interaction {

    private final GenerateAccount generateAccountUseCase;
    private final GetCurrentAccount getCurrentAccountUseCase;
    private final Login loginUseCase;
    private final Logout logoutUseCase;
    private final AddIncome addIncomeUseCase;
    private final GetAccount getAccountUseCase;
    private final Transfer transferUseCase;
    private final CardNumberService cardNumberService;
    private final RemoveAccount removeAccountUseCase;

    public Interaction(
        GenerateAccount generateAccountUseCase,
        GetCurrentAccount getCurrentAccountUseCase,
        Login loginUseCase,
        Logout logoutUseCase,
        AddIncome addIncomeUseCase,
        GetAccount getAccountUseCase,
        Transfer transferUseCase,
        CardNumberService cardNumberService,
        RemoveAccount removeAccountUseCase
    ) {
        this.generateAccountUseCase = generateAccountUseCase;
        this.getCurrentAccountUseCase = getCurrentAccountUseCase;
        this.loginUseCase = loginUseCase;
        this.logoutUseCase = logoutUseCase;
        this.addIncomeUseCase = addIncomeUseCase;
        this.getAccountUseCase = getAccountUseCase;
        this.transferUseCase = transferUseCase;
        this.cardNumberService = cardNumberService;
        this.removeAccountUseCase = removeAccountUseCase;
    }

    public void execute() {
        this.startMenu();
    }

    private void startMenu() {
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");

        Scanner scanner = new Scanner(System.in);

        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                System.out.println();
                this.creatingAccount();
                this.startMenu();
                break;
            case 2:
                System.out.println();
                this.enteringIntoAccount();
                break;
            case 0:
                System.out.println();
                this.exiting();
                break;
            default:
                System.out.println();
                System.out.println("Unsupported choice!");
                System.out.println();
                this.startMenu();
                break;
        }
    }

    private void creatingAccount() {
        RawCredentials rawCredentials = this.generateAccountUseCase.execute();

        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(rawCredentials.cardNumber());
        System.out.println("Your card PIN:");
        System.out.println(rawCredentials.cardPIN());
        System.out.println();
    }

    private void enteringIntoAccount() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your card number:");
        String cardNumber = scanner.next();
        System.out.println("Enter your PIN:");
        String cardPIN = scanner.next();

        try {
            this.loginUseCase.execute(cardNumber, cardPIN);
        } catch (
            AccountOfCardNumberExists.AccountOfCardNumberDoesNotExistException
                | CardPINIsCorrectForAccount.IncorrectCardPINException e) {
            System.out.println("Wrong card number or PIN!");
            System.out.println();
            this.startMenu();
            return;
        }

        System.out.println("You have successfully logged in!");
        System.out.println();

        this.accountMenu();
    }

    private void accountMenu() {
        Account account = this.getCurrentAccountUseCase.execute();
        if (account == null) {
            throw new RuntimeException("Unauthorized");
        }

        System.out.println("1. Balance");
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");

        Scanner scanner = new Scanner(System.in);

        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                System.out.println("Balance: " + account.balance());
                System.out.println();
                this.accountMenu();
                break;
            case 2:
                this.addingIncome();
                System.out.println();
                this.accountMenu();
                break;
            case 3:
                this.transfering();
                System.out.println();
                this.accountMenu();
                break;
            case 4:
                this.closingAccount();
                System.out.println();
                this.startMenu();
                break;
            case 5:
                this.logoutUseCase.execute();
                System.out.println("You have successfully logged out!");
                System.out.println();
                this.startMenu();
                break;
            case 0:
                System.out.println();
                this.exiting();
                break;
            default:
                System.out.println("Unsupported choice!");
                System.out.println();
                this.accountMenu();
                break;
        }
    }

    private void addingIncome() {
        System.out.println("Enter income:");

        Scanner scanner = new Scanner(System.in);
        long income = scanner.nextLong();

        this.addIncomeUseCase.execute(this.getCurrentAccountUseCase.execute().cardNumber(), income);

        System.out.println("Income was added!");
    }

    private void transfering() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Transfer");
        System.out.println("Enter card number:");

        String cardNumber = scanner.next();

        if (!this.cardNumberService.isValid(cardNumber)) {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
            System.out.println();
            return;
        }

        try {
            this.getAccountUseCase.execute(cardNumber);
        } catch (AccountOfCardNumberExists.AccountOfCardNumberDoesNotExistException e) {
            System.out.println("Such a card does not exist.");
            System.out.println();
            return;
        }

        System.out.println("Enter how much money you want to transfer:");

        long amount = scanner.nextLong();

        try {
            this.transferUseCase.execute(this.getCurrentAccountUseCase.execute().cardNumber(), cardNumber, amount);
        } catch (AccountCanAffordDebit.InsufficientFundsException e) {
            System.out.println("Not enough money!");
            System.out.println();
            return;
        } catch (TransferBetweenDifferentAccounts.TryingToTransferBetweenTheSameAccountsException e) {
            System.out.println("It is your card number. Enter another one.");
            System.out.println();
            return;
        }

        System.out.println("Success!");
    }

    private void closingAccount() {
        this.removeAccountUseCase.execute(this.getCurrentAccountUseCase.execute().cardNumber());
        this.logoutUseCase.execute();
        System.out.println("The account has been closed!");
    }

    private void exiting() {
        this.logoutUseCase.execute();
        System.out.println("Bye!");
    }
}
