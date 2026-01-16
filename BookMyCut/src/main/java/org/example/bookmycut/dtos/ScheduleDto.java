package org.example.bookmycut.dtos;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScheduleDto {

    // Validation messages as constants
    public static final String MSG_EMPLOYEE_ID_REQUIRED = "Employee ID is required";
    public static final String MSG_DAY_REQUIRED = "Day of week is required";
    public static final String MSG_DAY_MIN = "Day of week must be at least 1 (Monday)";
    public static final String MSG_DAY_MAX = "Day of week cannot exceed 7 (Sunday)";
    public static final String MSG_START_REQUIRED = "Start time is required";
    public static final String MSG_END_REQUIRED = "End time is required";

    private Long id;

    @NotNull(message = MSG_EMPLOYEE_ID_REQUIRED)
    private EmployeeDto employee;

    @NotNull(message = MSG_DAY_REQUIRED)
    @Min(value = 1, message = MSG_DAY_MIN)
    @Max(value = 7, message = MSG_DAY_MAX)
    private Integer dayOfWeek;

    @NotNull(message = MSG_START_REQUIRED)
    private LocalTime startTime;

    @NotNull(message = MSG_END_REQUIRED)
    private LocalTime endTime;
}



