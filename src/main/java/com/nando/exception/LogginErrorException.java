package com.nando.exception;

public class LogginErrorException extends RuntimeException {
    public LogginErrorException(Exception e) {
        super("Error logging into the panel", e);
    }
}
