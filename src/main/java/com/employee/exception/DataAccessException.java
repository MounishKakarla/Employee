package com.employee.exception;

public class DataAccessException extends Exception {
    public DataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
