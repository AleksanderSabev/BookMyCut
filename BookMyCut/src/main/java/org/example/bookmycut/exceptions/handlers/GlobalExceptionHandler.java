package org.example.bookmycut.exceptions.handlers;

import org.example.bookmycut.dtos.error.ApiError;
import org.example.bookmycut.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    // Дефиниране на константи за съобщенията
    private static final String MSG_INVALID_CREDENTIALS = "Invalid username or password.";
    private static final String MSG_ACCESS_DENIED = "Access denied: You do not have permission to access this resource.";
    private static final String MSG_UNEXPECTED_ERROR = "An unexpected server error occurred.";
    private static final String MSG_AUTH_REQUIRED = "Authentication is required to access this resource.";

    // ---------- 404 Not Found ----------
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(EntityNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // ---------- 409 Conflict ----------
    @ExceptionHandler({
            DuplicateEntityException.class,
            EmployeeUnavailableException.class,
            EntityHasAppointmentsException.class,
            TimeOverlapException.class,
            AlreadyHasThisRoleException.class
    })
    public ResponseEntity<ApiError> handleConflict(RuntimeException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    // ---------- 403 Forbidden ----------
    @ExceptionHandler({
            AccessDeniedException.class,
            UnauthorizedOperationException.class
    })
    public ResponseEntity<ApiError> handleForbidden(Exception ex) {
        String message = (ex instanceof UnauthorizedOperationException)
                ? ex.getMessage()
                : MSG_ACCESS_DENIED;
        return buildError(HttpStatus.FORBIDDEN, message);
    }

    // ---------- 400 Bad Request ----------
    @ExceptionHandler({
            IllegalArgumentException.class,
            IllegalStateException.class
    })
    public ResponseEntity<ApiError> handleBadRequest(RuntimeException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // Валидация на полетата от DTO-тата (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    // ---------- 401 Unauthorized ----------
    @ExceptionHandler({ BadCredentialsException.class, AuthenticationException.class })
    public ResponseEntity<ApiError> handleUnauthorized(Exception ex) {
        String message = (ex instanceof BadCredentialsException) ? MSG_INVALID_CREDENTIALS : MSG_AUTH_REQUIRED;
        return buildError(HttpStatus.UNAUTHORIZED, message);
    }

    // ---------- 500 Internal Server Error ----------
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex) {
        ex.printStackTrace(); // Важно за логване в конзолата
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, MSG_UNEXPECTED_ERROR);
    }

    // ---------- Helper Method ----------
    private ResponseEntity<ApiError> buildError(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(
                new ApiError(
                        status.value(),
                        message,
                        LocalDateTime.now()
                )
        );
    }
}