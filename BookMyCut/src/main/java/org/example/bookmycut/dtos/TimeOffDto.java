package org.example.bookmycut.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeOffDto {
    public static final String EMPLOYEE_ID_REQUIRED =
            "Employee id is required";
    public static final String START_TIME_REQUIRED =
            "Start time is required";
    public static final String END_TIME_REQUIRED =
            "End time is required";
    public static final String REASON_REQUIRED =
            "Reason is required";
    public static final String REASON_LENGTH =
            "Reason must be between 3 and 255 characters";

    private Long id;

    @NotNull(message = EMPLOYEE_ID_REQUIRED)
    private Long employeeId;

    @NotNull(message = START_TIME_REQUIRED)
    private LocalDateTime startDateTime;

    @NotNull(message = END_TIME_REQUIRED)
    private LocalDateTime endDateTime;

    @NotBlank(message = REASON_REQUIRED)
    @Size(min = 3, max = 255, message = REASON_LENGTH)
    private String reason;
}

