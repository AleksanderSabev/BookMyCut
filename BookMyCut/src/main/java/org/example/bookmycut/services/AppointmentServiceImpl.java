package org.example.bookmycut.services;

import org.example.bookmycut.dtos.AppointmentDto;
import org.example.bookmycut.enums.AppointmentStatus;
import org.example.bookmycut.exceptions.EmployeeUnavailableException;
import org.example.bookmycut.exceptions.EntityNotFoundException;
import org.example.bookmycut.helpers.AppointmentMapper;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ProcedureRepository procedureRepository;
    private final AvailabilityService availabilityService;
    private final AppUserRepository userRepository;
    private final AppointmentMapper appointmentMapper;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, EmployeeRepository employeeRepository, ProcedureRepository procedureRepository, AvailabilityService availabilityService, AppUserRepository userRepository, AppointmentMapper appointmentMapper) {
        this.appointmentRepository = appointmentRepository;
        this.employeeRepository = employeeRepository;
        this.procedureRepository = procedureRepository;
        this.availabilityService = availabilityService;
        this.userRepository = userRepository;
        this.appointmentMapper = appointmentMapper;
    }

    @Transactional
    @Override
    public AppointmentDto bookAppointment(Long employeeId, Long procedureId, Long userId, LocalDateTime startDateTime) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee", employeeId));

        Procedure procedure = procedureRepository.findById(procedureId)
                .orElseThrow(() -> new EntityNotFoundException("Procedure",procedureId));

        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));

        LocalDateTime endDateTime = startDateTime.plusMinutes(procedure.getDurationMinutes());
        if(!availabilityService.isEmployeeAvailable(employeeId,
                startDateTime,
                endDateTime)){

            throw new EmployeeUnavailableException("Employee is not available this time!");
        }
        Appointment appointment = new Appointment(employee, procedure, user, startDateTime, endDateTime);
        appointmentRepository.save(appointment);

        return appointmentMapper.toDto(appointment);
    }

    @Transactional
    @Override
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment", appointmentId));

        //TODO check appointment's ownership by the user who wants to cancel it

        appointment.setStatus(AppointmentStatus.CANCELLED);

        appointmentRepository.save(appointment);
    }

    @Override
    public List<Appointment> getAppointmentsForEmployee(Long employeeId, LocalDate date) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee", employeeId));
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23,59,59);
        return appointmentRepository
                .findByEmployeeIdAndStartDatetimeBetween(employeeId,startOfDay,endOfDay)
                .stream().filter(a -> !a.getStatus().equals(AppointmentStatus.CANCELLED))
                .toList();
    }

    @Override
    public List<Appointment> getAppointmentsForUser(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));
        return appointmentRepository.findByUserAndStatus(user, AppointmentStatus.SCHEDULED);
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
