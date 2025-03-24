package com.education.employee.exception.menu;

public class DishIsUnavailableException extends RuntimeException {
    public DishIsUnavailableException(String message) {
        super(message);
    }
}
