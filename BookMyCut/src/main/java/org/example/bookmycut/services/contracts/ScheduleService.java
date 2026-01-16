package org.example.bookmycut.services.contracts;

import org.example.bookmycut.dtos.ScheduleDto;

import java.util.List;

public interface ScheduleService {

    ScheduleDto addSchedule(ScheduleDto scheduleDTO);

    List<ScheduleDto> getScheduleForEmployee(Long employeeId);

    ScheduleDto removeSchedule(ScheduleDto scheduleDTO);


    ScheduleDto updateSchedule(ScheduleDto scheduleDTO);


    List<ScheduleDto> getScheduleForEmployeeByDay(Long employeeId, int dayOfWeek);


    List<ScheduleDto> getAllSchedules();


}
