package org.example.bookmycut.services.contracts;

import org.example.bookmycut.dtos.ScheduleDto;

import java.time.DayOfWeek;
import java.util.List;

public interface ScheduleService {

    ScheduleDto addSchedule(ScheduleDto scheduleDTO);

    List<ScheduleDto> getScheduleForEmployee(Long employeeId);

    void removeSchedule(Long id, DayOfWeek dayOfWeek);


    void updateSchedule(ScheduleDto scheduleDTO);

    boolean isEmployeeWorkingOnDay(Long employeeId, DayOfWeek dayOfWeek);

    ScheduleDto getScheduleForEmployeeByDay(Long employeeId, DayOfWeek dayOfWeek);


    List<ScheduleDto> getAllSchedules();


}
