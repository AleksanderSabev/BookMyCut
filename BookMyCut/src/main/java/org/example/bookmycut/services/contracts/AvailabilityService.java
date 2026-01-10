package org.example.bookmycut.services.contracts;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface AvailabilityService {

    boolean isEmployeeAvailable(
            Long employeeId,
            LocalDateTime start,
            LocalDateTime end
    );

    List<LocalTime> getAvailableStartTimes(
            Long employeeId,
            LocalDate date,
            int serviceDurationMinutes
    );
}
