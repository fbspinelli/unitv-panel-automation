package com.nando.exception;

public class ErrorReadEmailException extends RuntimeException {
    public ErrorReadEmailException(Exception e) {
        super("Error get messages email", e);
    }

}
