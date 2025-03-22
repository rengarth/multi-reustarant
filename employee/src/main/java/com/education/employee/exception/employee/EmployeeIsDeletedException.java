package com.education.employee.exception.employee;

public class EmployeeIsDeletedException extends RuntimeException{

    public EmployeeIsDeletedException(String message) {
        super(message);
    }
}
