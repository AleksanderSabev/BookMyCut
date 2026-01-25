package org.example.bookmycut.exceptions;

import org.example.bookmycut.enums.Role;

public class AlreadyHasThisRoleException extends RuntimeException {
    public AlreadyHasThisRoleException(Role role) {
        super(String.format("User already is %s.",role));
    }
}
