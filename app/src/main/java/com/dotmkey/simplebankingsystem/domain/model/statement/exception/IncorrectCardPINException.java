package com.dotmkey.simplebankingsystem.domain.model.statement.exception;

public class IncorrectCardPINException extends RuntimeException {

    public IncorrectCardPINException() {
        super("Card PIN is incorrect");
    }
}
