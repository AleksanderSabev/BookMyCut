package org.example.bookmycut.exceptions;

public class EmployeeNotWorkingThisDayException extends RuntimeException {
    public EmployeeNotWorkingThisDayException(String message) {
        super(message);
    }
}
