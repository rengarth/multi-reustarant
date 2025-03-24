package com.education.restaurantservice.exception.menu;

public class DishIsUnavailableException extends RuntimeException {
    public DishIsUnavailableException(String message) {
        super(message);
    }
}
