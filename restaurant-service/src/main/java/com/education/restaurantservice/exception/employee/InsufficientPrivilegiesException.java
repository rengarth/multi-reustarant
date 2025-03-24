package com.education.restaurantservice.exception.employee;

public class InsufficientPrivilegiesException extends RuntimeException {
    public InsufficientPrivilegiesException(String message) {
        super(message);
    }
}
