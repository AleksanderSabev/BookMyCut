package org.example.bookmycut.services;

import org.example.bookmycut.dtos.appointment.AppointmentRequestDto;
import org.example.bookmycut.dtos.appointment.AppointmentResponseDto;
import org.example.bookmycut.enums.AppointmentStatus;
import org.example.bookmycut.exceptions.EmployeeUnavailableException;
import org.example.bookmycut.exceptions.EntityNotFoundException;
import org.example.bookmycut.exceptions.UnauthorizedOperationException;
import org.example.bookmycut.helpers.mappers.AppointmentMapper;
import org.example.bookmycut.models.AppUser;
import org.example.bookmycut.models.Appointment;
import org.example.bookmycut.models.Employee;
import org.example.bookmycut.models.Procedure;
import org.example.bookmycut.repositories.AppUserRepository;
import org.example.bookmycut.repositories.AppointmentRepository;
import org.example.bookmycut.repositories.EmployeeRepository;
import org.example.bookmycut.repositories.ProcedureRepository;
import org.example.bookmycut.services.contracts.AppointmentService;
import org.example.bookmycut.services.contracts.AvailabilityService;
import org.example.bookmycut.services.contracts.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private static final String PROCEDURE_NOT_IN_THE_EMPLOYEES_LIST = "This employee can't perform this procedure!";
    private static final String EMPLOYEE_UNAVAILABLE = "Employee is not available this time!";
    private static final String INCORRECT_STATUS_MESSAGE = "Only scheduled appointments can be cancelled.";
    private static final String EMPLOYEE_NOT_WORKING = "Employee is working on this day!";

    private final AppointmentRepository appointmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ProcedureRepository procedureRepository;
    private final AvailabilityService availabilityService;
    private final AppUserRepository userRepository;
    private final AppointmentMapper appointmentMapper;
    private final ScheduleService scheduleService;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, EmployeeRepository employeeRepository, ProcedureRepository procedureRepository, AvailabilityService availabilityService, AppUserRepository userRepository, AppointmentMapper appointmentMapper, ScheduleService scheduleService) {
        this.appointmentRepository = appointmentRepository;
        this.employeeRepository = employeeRepository;
        this.procedureRepository = procedureRepository;
        this.availabilityService = availabilityService;
        this.userRepository = userRepository;
        this.appointmentMapper = appointmentMapper;
        this.scheduleService = scheduleService;
    }

    @Transactional
    @Override
    public AppointmentResponseDto bookAppointment(Long userId, AppointmentRequestDto dto) {
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee", dto.getEmployeeId()));

        if(!scheduleService.isEmployeeWorkingOnDay(employee.getId(), dto.getStartDateTime().getDayOfWeek())){
            throw new EmployeeUnavailableException(EMPLOYEE_NOT_WORKING);
        }

        Procedure procedure = procedureRepository.findById(dto.getProcedureId())
                .orElseThrow(() -> new EntityNotFoundException("Procedure", dto.getProcedureId()));

        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));

        if(!employee.getProcedures().contains(procedure)){
            throw new EmployeeUnavailableException(PROCEDURE_NOT_IN_THE_EMPLOYEES_LIST);
        }
        LocalDateTime endDateTime = dto.getStartDateTime().plusMinutes(procedure.getDurationMinutes());
        if(!availabilityService.isEmployeeAvailable(dto.getEmployeeId(),
                dto.getStartDateTime(),
                endDateTime)){

            throw new EmployeeUnavailableException(EMPLOYEE_UNAVAILABLE);
        }
        Appointment appointment = new Appointment(employee, procedure, user, dto.getStartDateTime(), endDateTime);
        employee.addAppointment(appointment);
        appointmentRepository.save(appointment);

        return appointmentMapper.toDto(appointment);
    }

    @Transactional
    @Override
    public void cancelAppointment(Long appointmentId, Long userId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment", appointmentId));

        if (!appointment.getStatus().equals(AppointmentStatus.SCHEDULED)) {
            throw new IllegalStateException(INCORRECT_STATUS_MESSAGE);
        }

        if(!appointment.getUser().getId().equals(userId)){
            throw new UnauthorizedOperationException();
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.getEmployee().cancelAppointment(appointment);

        appointmentRepository.save(appointment);
    }

    @Override
    public List<AppointmentResponseDto> getAppointmentsForEmployee(Long employeeId, LocalDate date) {
        if(!employeeRepository.existsById(employeeId)) {
            throw new EntityNotFoundException("Employee", employeeId);
        }
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23,59,59);
        return appointmentRepository
                .findByEmployeeIdAndStartDatetimeBetween(employeeId,startOfDay,endOfDay)
                .stream().filter(a -> !a.getStatus().equals(AppointmentStatus.CANCELLED))
                .map(appointmentMapper::toDto)
                .toList();
    }

    @Override
    public List<AppointmentResponseDto> getAppointmentsForUser(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));
        return appointmentRepository.findByUserAndStatus(user, AppointmentStatus.SCHEDULED).stream()
                .map(appointmentMapper::toDto)
                .toList();
    }

    @Transactional
    @Scheduled(fixedRate = 60000) // every minute
    public void markPastAppointmentsAsCompleted() {
        List<Appointment> pastAppointments = appointmentRepository
                .findByStatusAndEndDatetimeBefore(AppointmentStatus.SCHEDULED, LocalDateTime.now());

        if (!pastAppointments.isEmpty()) {
            pastAppointments.forEach(a -> a.setStatus(AppointmentStatus.COMPLETED));
            appointmentRepository.saveAll(pastAppointments);
        }
    }
}
