package org.example.bookmycut.services.contracts;

import org.example.bookmycut.dtos.TimeOffDto;

import java.util.List;

public interface TimeOffService {

    TimeOffDto addTimeOff(TimeOffDto timeOffDto);

    TimeOffDto removeTimeOff(Long timeOffId);

    TimeOffDto updateTimeOff(TimeOffDto updatedDto);

    List<TimeOffDto> getTimeOffForEmployee(Long employeeId);
}
