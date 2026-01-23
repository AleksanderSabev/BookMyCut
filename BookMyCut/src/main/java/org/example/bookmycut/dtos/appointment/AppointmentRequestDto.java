package org.example.bookmycut.dtos.appointment;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequestDto {
    public static final String EMPLOYEE_REQUIRED = "Employee must be selected";
    public static final String PROCEDURE_REQUIRED = "Procedure must be selected";
    public static final String USERNAME_REQUIRED = "Username must be provided";
    public static final String START_TIME_REQUIRED = "Start time is required";
    public static final String START_TIME_FUTURE = "Start time must be in the future";

    @NotBlank(message = EMPLOYEE_REQUIRED)
    private Long employeeId;

    @NotBlank(message = PROCEDURE_REQUIRED)
    private Long procedureId;

    @NotBlank(message = START_TIME_REQUIRED)
    @Future(message = START_TIME_FUTURE)
    private LocalDateTime startDateTime;
}

