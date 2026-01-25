package org.example.bookmycut.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdatePasswordDto {

    private final String PASS_LENGTH_SIZE = "New password length must be between 6 and 20 characters";

    @NotBlank
    @Size(min = 6, max = 20, message = PASS_LENGTH_SIZE)
    private String newPassword;

    @NotBlank
    private String confirmPassword;
}
