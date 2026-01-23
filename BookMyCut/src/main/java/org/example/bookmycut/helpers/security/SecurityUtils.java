package org.example.bookmycut.helpers.security;

import org.example.bookmycut.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

public class SecurityUtils {

    private final static String INVALID_AUTHENTICATION = "Invalid user authentication";
    private final static String LOG_IN_REQUIRED = "You must be logged in";

    private SecurityUtils() {}

    public static Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, LOG_IN_REQUIRED);
        }

        Object principal = auth.getPrincipal();
        if (!(principal instanceof CustomUserDetails userDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,  INVALID_AUTHENTICATION);
        }

        return userDetails.getId();
    }
}

