package com.dotmkey.simplebankingsystem.domain;

abstract public class Statement {

    abstract public boolean check();
    abstract protected RuntimeException exception();

    public void checkOrThrow() {
        if (!this.check()) {
            throw this.exception();
        }
    }
}
