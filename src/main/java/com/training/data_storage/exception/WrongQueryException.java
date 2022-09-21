package com.training.data_storage.exception;

public class WrongQueryException extends RuntimeException {
    public WrongQueryException(String message) {
        super(message);
    }
}
