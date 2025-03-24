package com.education.employee.exception.employee;

public class InsufficientPrivilegiesException extends RuntimeException {
    public InsufficientPrivilegiesException(String message) {
        super(message);
    }
}
