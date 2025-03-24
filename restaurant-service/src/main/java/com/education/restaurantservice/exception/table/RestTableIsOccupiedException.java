package com.education.restaurantservice.exception.table;

public class RestTableIsOccupiedException extends RuntimeException {
    public RestTableIsOccupiedException(String message) {
        super(message);
    }
}
