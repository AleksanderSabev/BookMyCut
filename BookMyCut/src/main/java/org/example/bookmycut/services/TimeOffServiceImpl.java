package org.example.bookmycut.services;

import org.example.bookmycut.dtos.TimeOffDto;
import org.example.bookmycut.services.contracts.TimeOffService;

import java.util.List;

public class TimeOffServiceImpl implements TimeOffService {
    @Override
    public TimeOffDto addTimeOff(TimeOffDto timeOffDto) {
        return null;
    }

    @Override
    public void removeTimeOff(Long timeOffId) {

    }

    @Override
    public List<TimeOffDto> getTimeOffForEmployee(Long employeeId) {
        return List.of();
    }

    @Override
    public boolean isTimeOffOverlapping(TimeOffDto timeOffDto) {
        return false;
    }
}
