package com.education.restaurantservice.exception.employee;

public class IdenticalPasswordException extends RuntimeException {
    public IdenticalPasswordException(String message) {
        super(message);
    }
}
