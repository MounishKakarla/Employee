package com.employee.exception;

public class DuplicateEmployeeException extends Exception {
    public DuplicateEmployeeException(String msg) {
        super(msg);
    }
}
