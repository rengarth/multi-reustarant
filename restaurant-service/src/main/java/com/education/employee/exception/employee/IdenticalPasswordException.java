package com.education.employee.exception.employee;

public class IdenticalPasswordException extends RuntimeException {
    public IdenticalPasswordException(String message) {
        super(message);
    }
}
