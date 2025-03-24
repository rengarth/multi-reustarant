package com.education.employee.exception.table;

public class RestTableNotFoundException extends RuntimeException {
    public RestTableNotFoundException(String message) {
        super(message);
    }
}
