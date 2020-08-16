package com.mkcode.simplebanking.service;

public class OperationValidationException extends RuntimeException {
    public OperationValidationException(String message) {
        super(message);
    }
}
