package org.example.bookmycut.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginUserDto {

    public static final String USERNAME_REQUIRED = "Name is required";
    public static final String PASSWORD_REQUIRED = "Password is required";

    @NotBlank(message = USERNAME_REQUIRED)
    private String username;

    @NotBlank(message = PASSWORD_REQUIRED)
    private String password;
}

