package com.education.employee.exception.table;

public class RestTableItemNotFoundException extends RuntimeException {
    public RestTableItemNotFoundException(String message) {
        super(message);
    }
}
