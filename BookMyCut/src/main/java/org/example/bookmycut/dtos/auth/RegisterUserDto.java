package org.example.bookmycut.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserDto {

    public static final String USERNAME_REQUIRED = "Name is required";
    public static final String USERNAME_LENGTH = "Username must be between 3 and 20 characters";
    public static final String EMAIL_REQUIRED = "Email is required";
    public static final String EMAIL_INVALID = "Email must be valid";
    public static final String PASSWORD_REQUIRED = "Password is required";
    public static final String PASSWORD_LENGTH = "Password must be between 6 and 20 characters";

    @NotBlank(message = USERNAME_REQUIRED)
    @Size(min = 3, max = 20, message = USERNAME_LENGTH)
    private String username;

    @NotBlank(message = EMAIL_REQUIRED)
    @Email(message = EMAIL_INVALID)
    private String email;

    @NotBlank(message = PASSWORD_REQUIRED)
    @Size(min = 6, max = 20, message = PASSWORD_LENGTH)
    private String password;
}

