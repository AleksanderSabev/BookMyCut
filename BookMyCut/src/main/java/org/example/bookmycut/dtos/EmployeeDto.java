package org.example.bookmycut.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    public static final String NAME_EMPTY = "Employee name cannot be empty";
    public static final String EMAIL_INVALID = "Email must be valid and not empty";
    public static final String PHONE_INVALID = "Phone must be digits only and not empty";

    private Long id;

    @NotBlank(message = NAME_EMPTY)
    private String name;

    @NotBlank(message = EMAIL_INVALID)
    @Email(message = EMAIL_INVALID)
    private String email;

    @NotBlank(message = PHONE_INVALID)
    @Pattern(regexp = "\\d{7,15}", message = PHONE_INVALID)
    private String phone;

    private List<Long> procedureIds;
}
