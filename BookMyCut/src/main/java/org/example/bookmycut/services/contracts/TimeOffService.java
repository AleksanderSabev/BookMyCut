package org.example.bookmycut.services.contracts;

import org.example.bookmycut.models.EmployeeTimeOff;

import java.time.LocalDateTime;
import java.util.List;

public interface TimeOffService {

    void addTimeOff(
            Long employeeId,
            LocalDateTime start,
            LocalDateTime end,
            String reason
    );

    List<EmployeeTimeOff> getTimeOffForEmployee(Long employeeId);
}
