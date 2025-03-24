package com.education.restaurantservice.exception.employee;

public class EmployeeIsDeletedException extends RuntimeException{

    public EmployeeIsDeletedException(String message) {
        super(message);
    }
}
