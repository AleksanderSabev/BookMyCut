package org.example.bookmycut.services.contracts;

import org.example.bookmycut.models.EmployeeSchedule;

import java.time.LocalTime;
import java.util.List;

public interface ScheduleService {

    void addSchedule(
            Long employeeId,
            int dayOfWeek,
            LocalTime startTime,
            LocalTime endTime
    );

    List<EmployeeSchedule> getScheduleForEmployee(Long employeeId);
}
