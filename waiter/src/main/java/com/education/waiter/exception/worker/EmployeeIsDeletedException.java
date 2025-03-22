package com.education.waiter.exception.worker;

public class EmployeeIsDeletedException extends RuntimeException{

    public EmployeeIsDeletedException(String message) {
        super(message);
    }
}
