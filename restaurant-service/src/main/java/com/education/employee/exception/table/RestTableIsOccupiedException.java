package com.education.employee.exception.table;

public class RestTableIsOccupiedException extends RuntimeException {
    public RestTableIsOccupiedException(String message) {
        super(message);
    }
}
