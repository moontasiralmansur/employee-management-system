package com.ems.model.exceptions;

// Custom checked exception for invalid employee data.
// Extends Exception so callers must handle validation failures explicitly.
public class InvalidEmployeeDataException extends Exception {
    public InvalidEmployeeDataException(String message) {
        super(message);
    }

    public InvalidEmployeeDataException(String message, Throwable cause) {
        super(message, cause);
    }
}