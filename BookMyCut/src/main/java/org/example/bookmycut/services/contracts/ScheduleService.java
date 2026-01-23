package org.example.bookmycut.services.contracts;

import org.example.bookmycut.dtos.ScheduleDto;

import java.util.List;

public interface ScheduleService {

    ScheduleDto addSchedule(ScheduleDto scheduleDTO);

    List<ScheduleDto> getScheduleForEmployee(Long employeeId);

    void removeSchedule(ScheduleDto scheduleDTO);


    void updateSchedule(ScheduleDto scheduleDTO);


    List<ScheduleDto> getScheduleForEmployeeByDay(Long employeeId, int dayOfWeek);


    List<ScheduleDto> getAllSchedules();


}
