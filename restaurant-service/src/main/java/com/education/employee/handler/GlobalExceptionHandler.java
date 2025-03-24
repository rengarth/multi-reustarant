package com.education.employee.handler;

import com.education.employee.exception.employee.EmployeeIsDeletedException;
import com.education.employee.exception.employee.EmployeeNotFoundException;
import com.education.employee.exception.employee.IdenticalPasswordException;
import com.education.employee.exception.employee.InsufficientPrivilegiesException;
import com.education.employee.exception.menu.CategoryNotFoundException;
import com.education.employee.exception.menu.DishIsDeletedException;
import com.education.employee.exception.menu.DishIsUnavailableException;
import com.education.employee.exception.menu.DishNotFoundException;
import com.education.employee.exception.order.OrderNotFoundException;
import com.education.employee.exception.table.RestTableIsNotAssignedException;
import com.education.employee.exception.table.RestTableIsOccupiedException;
import com.education.employee.exception.table.RestTableItemNotFoundException;
import com.education.employee.exception.table.RestTableNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            EmployeeIsDeletedException.class,
            DishIsDeletedException.class
    })
    public ResponseEntity<String> handleDeletedEntityException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.GONE).body(ex.getMessage());
    }

    @ExceptionHandler({
            EmployeeNotFoundException.class,
            CategoryNotFoundException.class,
            DishNotFoundException.class,
            OrderNotFoundException.class,
            RestTableItemNotFoundException.class,
            RestTableNotFoundException.class
    })
    public ResponseEntity<String> handleNotFoundException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler({
            IdenticalPasswordException.class,
            RestTableIsOccupiedException.class,
            DishIsUnavailableException.class
    })
    public ResponseEntity<String> handleConflictException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler({
            InsufficientPrivilegiesException.class,
            RestTableIsNotAssignedException.class
    })
    public ResponseEntity<String> handleForbiddenException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected exception: " + ex.getMessage());
    }
}

