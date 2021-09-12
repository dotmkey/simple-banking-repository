package com.dotmkey.simplebankingsystem.domain;

import com.dotmkey.simplebankingsystem.domain.model.AccountRepository;
import com.dotmkey.simplebankingsystem.domain.model.CardNumberService;
import com.dotmkey.simplebankingsystem.domain.model.Hasher;

public final class DomainRegistry {

    private static DomainRegistry instance;
    private final Hasher hasher;
    private final CardNumberService cardNumberService;
    private final AccountRepository accountRepository;

    private DomainRegistry(Hasher hasher, CardNumberService cardNumberService, AccountRepository accountRepository) {
        this.hasher = hasher;
        this.cardNumberService = cardNumberService;
        this.accountRepository = accountRepository;
    }

    // Auto-wiring analog
    public static void configure(
        Hasher hasher,
        CardNumberService cardNumberService,
        AccountRepository accountRepository
    ) {
        if (instance != null) {
            return;
        }

        instance = new DomainRegistry(hasher, cardNumberService, accountRepository);
    }

    public static DomainRegistry instance() {
        return instance;
    }

    public Hasher hasher() {
        return this.hasher;
    }

    public CardNumberService cardNumberService() {
        return this.cardNumberService;
    }

    public AccountRepository accountRepository() {
        return this.accountRepository;
    }
}
