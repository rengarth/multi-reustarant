package com.education.waiter.exception.worker;

public class IdenticalPasswordException extends RuntimeException {
    public IdenticalPasswordException(String message) {
        super(message);
    }
}
