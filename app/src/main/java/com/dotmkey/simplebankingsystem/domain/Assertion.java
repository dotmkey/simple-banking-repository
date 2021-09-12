package com.dotmkey.simplebankingsystem.domain;

public class Assertion {

    public static void assertA(Statement statement) {
        statement.checkOrThrow();
    }
}
