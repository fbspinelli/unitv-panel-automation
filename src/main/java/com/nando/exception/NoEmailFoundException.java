package com.nando.exception;

public class NoEmailFoundException extends RuntimeException {
    public NoEmailFoundException() {
        super("Email code was expected");
    }
}
