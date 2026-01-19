package org.example.bookmycut.services;

import org.example.bookmycut.dtos.TimeOffDto;
import org.example.bookmycut.exceptions.EntityHasAppointmentsException;
import org.example.bookmycut.exceptions.EntityNotFoundException;
import org.example.bookmycut.exceptions.TimeOverlapException;
import org.example.bookmycut.helpers.mappers.TimeOffMapper;
import org.example.bookmycut.models.Employee;
import org.example.bookmycut.models.EmployeeTimeOff;
import org.example.bookmycut.repositories.AppointmentRepository;
import org.example.bookmycut.repositories.EmployeeRepository;
import org.example.bookmycut.repositories.EmployeeTimeOffRepository;
import org.example.bookmycut.services.contracts.TimeOffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TimeOffServiceImpl implements TimeOffService {

    private final String START_BEFORE_END_MESSAGE = "Start time must be before end time";
    private final String OVERLAP_MESSAGE = "This employee already has time off in this range";

    private final EmployeeTimeOffRepository timeOffRepository;
    private final AppointmentRepository appointmentRepository;
    private final EmployeeRepository employeeRepository;
    private final TimeOffMapper timeOffMapper;

    @Autowired
    public TimeOffServiceImpl(EmployeeTimeOffRepository timeOffRepository, AppointmentRepository appointmentRepository, EmployeeRepository employeeRepository, TimeOffMapper timeOffMapper) {
        this.timeOffRepository = timeOffRepository;
        this.appointmentRepository = appointmentRepository;
        this.employeeRepository = employeeRepository;
        this.timeOffMapper = timeOffMapper;
    }


    @Transactional
    @Override
    public TimeOffDto addTimeOff(TimeOffDto timeOffDto) {
        checkStartBeforeEnd(timeOffDto.getStartDateTime(), timeOffDto.getEndDateTime());

        Employee employee = employeeRepository.findById(timeOffDto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee", timeOffDto.getEmployeeId()));

        if(isTimeOffOverlapping(timeOffDto)){
            throw new TimeOverlapException(OVERLAP_MESSAGE);
        }

        if(hasAppointmentsDuringTimeOff(timeOffDto,employee)){
            throw new EntityHasAppointmentsException(employee.getId());
        }

        EmployeeTimeOff timeOff = timeOffMapper.toEntity(timeOffDto,employee);

        EmployeeTimeOff saved = timeOffRepository.save(timeOff);
        return timeOffMapper.toDto(saved);
    }

    @Transactional
    @Override
    public TimeOffDto removeTimeOff(Long timeOffId) {
        EmployeeTimeOff deleted = timeOffRepository.findById(timeOffId)
                .orElseThrow(() -> new EntityNotFoundException("Time off", timeOffId));

        timeOffRepository.delete(deleted);

        return timeOffMapper.toDto(deleted);

    }

    @Transactional
    @Override
    public TimeOffDto updateTimeOff(TimeOffDto timeOffDto) {
        checkStartBeforeEnd(timeOffDto.getStartDateTime(), timeOffDto.getEndDateTime());

        EmployeeTimeOff existing = timeOffRepository.findById(timeOffDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Time off", timeOffDto.getId()));

        Employee employee = employeeRepository.findById(timeOffDto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee", timeOffDto.getEmployeeId()));

        if (isTimeOffOverlappingExcluding(timeOffDto, existing.getId())) {
            throw new TimeOverlapException(OVERLAP_MESSAGE);
        }

        if (hasAppointmentsDuringTimeOff(timeOffDto, employee)) {
            throw new EntityHasAppointmentsException(employee.getId());
        }

        existing.setStartDateTime(timeOffDto.getStartDateTime());
        existing.setEndDateTime(timeOffDto.getEndDateTime());
        existing.setReason(timeOffDto.getReason());

        EmployeeTimeOff updated = timeOffRepository.save(existing);

        return timeOffMapper.toDto(updated);
    }



    @Transactional(readOnly = true)
    @Override
    public List<TimeOffDto> getTimeOffForEmployee(Long employeeId) {
        if(!employeeRepository.existsById(employeeId)){
            throw new EntityNotFoundException("Employee", employeeId);
        }

        return timeOffRepository.findByEmployeeId(employeeId)
                .stream()
                .map(timeOffMapper::toDto)
                .toList();
    }

    protected void checkStartBeforeEnd(LocalDateTime start, LocalDateTime end){
        if (!start.isBefore(end)) {
            throw new IllegalArgumentException(START_BEFORE_END_MESSAGE);
        }
    }


    protected boolean isTimeOffOverlapping(TimeOffDto dto){
        return timeOffRepository.existsByEmployeeIdAndStartDateTimeLessThanAndEndDateTimeGreaterThan(
                dto.getEmployeeId(),
                dto.getEndDateTime(),
                dto.getStartDateTime()
        );
    }

    protected boolean isTimeOffOverlappingExcluding(TimeOffDto dto, Long excludeId){
        return timeOffRepository.existsByEmployeeIdAndStartDateTimeLessThanAndEndDateTimeGreaterThanAndIdNot(
                dto.getEmployeeId(),
                dto.getEndDateTime(),
                dto.getStartDateTime(),
                excludeId
        );
    }

    protected boolean hasAppointmentsDuringTimeOff(TimeOffDto dto, Employee employee){
        return appointmentRepository.existsByEmployeeIdAndStartDatetimeLessThanAndEndDatetimeGreaterThan(
                employee.getId(),
                dto.getEndDateTime(),
                dto.getStartDateTime()
        );
    }



}
