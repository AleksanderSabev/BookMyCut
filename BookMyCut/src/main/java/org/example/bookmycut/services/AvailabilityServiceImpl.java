package org.example.bookmycut.services;

import org.example.bookmycut.exceptions.EmployeeUnavailableException;
import org.example.bookmycut.models.EmployeeSchedule;
import org.example.bookmycut.repositories.AppointmentRepository;
import org.example.bookmycut.repositories.EmployeeScheduleRepository;
import org.example.bookmycut.repositories.EmployeeTimeOffRepository;
import org.example.bookmycut.services.contracts.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {

    private final int TIME_SLOT_MINUTES = 15;
    private final String NOT_WORKING = "Not working this day!";
    private final String START_AFTER_END = "Start time must be before end time!";

    private final EmployeeScheduleRepository scheduleRepository;
    private final EmployeeTimeOffRepository timeOffRepository;
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AvailabilityServiceImpl(EmployeeScheduleRepository scheduleRepository,
                                   EmployeeTimeOffRepository timeOffRepository,
                                   AppointmentRepository appointmentRepository) {
        this.scheduleRepository = scheduleRepository;
        this.timeOffRepository = timeOffRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public boolean isEmployeeAvailable(Long employeeId, LocalDateTime start, LocalDateTime end) {
        if(!start.isBefore(end)){
            throw new IllegalArgumentException(START_AFTER_END);
        }
        DayOfWeek dayOfWeek = start.getDayOfWeek();

        EmployeeSchedule daySchedule = scheduleRepository.findByEmployeeIdAndDayOfWeek(employeeId, dayOfWeek.getValue())
                .orElseThrow(() -> new EmployeeUnavailableException(NOT_WORKING));

        if (start.toLocalTime().isBefore(daySchedule.getStartTime()) ||
                end.toLocalTime().isAfter(daySchedule.getEndTime())) return false;

        if(timeOffRepository
                .existsByEmployeeIdAndStartDatetimeLessThanAndEndDatetimeGreaterThan(employeeId, end, start)) return false;

        if(appointmentRepository
                .existsByEmployeeIdAndStartDatetimeLessThanAndEndDatetimeGreaterThan(employeeId, end, start)) return false;

        return true;
    }

    @Override
    public List<LocalTime> getAvailableStartTimes(Long employeeId, LocalDate date, int serviceDurationMinutes) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        EmployeeSchedule daySchedule = scheduleRepository.findByEmployeeIdAndDayOfWeek(employeeId, dayOfWeek.getValue())
                .orElseThrow(() -> new EmployeeUnavailableException(NOT_WORKING));

        LocalTime startTime = daySchedule.getStartTime();
        LocalTime endTime = daySchedule.getEndTime();

        List<LocalTime> availableStartTimes = new ArrayList<>();

        LocalTime currentTime = startTime;

        while (!currentTime.plusMinutes(serviceDurationMinutes).isAfter(endTime)){
            LocalDateTime startDateTime = LocalDateTime.of(date, currentTime);
            LocalDateTime endDateTime = startDateTime.plusMinutes(serviceDurationMinutes);
            if(isEmployeeAvailable(employeeId, startDateTime, endDateTime) &&
                    (!date.equals(LocalDate.now()) || currentTime.isAfter(LocalTime.now()) )){
                availableStartTimes.add(currentTime);
            }

            currentTime=currentTime.plusMinutes(TIME_SLOT_MINUTES);
        }

        return availableStartTimes;

    }
}
