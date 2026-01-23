package org.example.bookmycut.exceptions;

public class UnauthorizedOperationException extends RuntimeException {
    public UnauthorizedOperationException() {
        super("You are not allowed to perform this action!");
    }
}
