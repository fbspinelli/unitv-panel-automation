package com.nando.exception;

public class ExtractCodeException extends RuntimeException {
    public ExtractCodeException() {
        super("No find code in body email");
    }
}
