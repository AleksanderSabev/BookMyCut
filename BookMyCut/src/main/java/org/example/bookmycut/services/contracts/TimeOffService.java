package org.example.bookmycut.services.contracts;

import org.example.bookmycut.dtos.TimeOffDto;

import java.util.List;

public interface TimeOffService {

    TimeOffDto addTimeOff(TimeOffDto timeOffDto);

    void removeTimeOff(Long timeOffId);

    List<TimeOffDto> getTimeOffForEmployee(Long employeeId);

    boolean isTimeOffOverlapping(TimeOffDto timeOffDto);
}
