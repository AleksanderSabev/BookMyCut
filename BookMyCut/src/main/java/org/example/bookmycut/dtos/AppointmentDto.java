package org.example.bookmycut.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {
    public static final String EMPLOYEE_REQUIRED = "Employee must be selected";
    public static final String PROCEDURE_REQUIRED = "Procedure must be selected";
    public static final String USERNAME_REQUIRED = "Username must be provided";
    public static final String START_TIME_REQUIRED = "Start time is required";
    public static final String START_TIME_FUTURE = "Start time must be in the future";
    public static final String END_TIME_REQUIRED = "End time is required";
    public static final String END_AFTER_START = "End time must be after start time";

    private Long id;

    @NotBlank(message = EMPLOYEE_REQUIRED)
    private String employeeName;

    @NotBlank(message = PROCEDURE_REQUIRED)
    private String procedureName;

    @NotBlank(message = USERNAME_REQUIRED)
    private String username;

    @NotNull(message = START_TIME_REQUIRED)
    @Future(message = START_TIME_FUTURE)
    private LocalDateTime startTime;

    @NotNull(message = END_TIME_REQUIRED)
    private LocalDateTime endTime;
}

