package org.example.bookmycut.services.contracts;

import org.example.bookmycut.dtos.AppointmentDto;
import org.example.bookmycut.models.Appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {

    AppointmentDto bookAppointment(
            Long employeeId,
            Long procedureId,
            Long userId,
            LocalDateTime startDateTime
    );

    void cancelAppointment(Long appointmentId);

    List<Appointment> getAppointmentsForEmployee(
            Long employeeId,
            LocalDate date
    );

    List<Appointment> getAppointmentsForUser(Long userId);

    void markPastAppointmentsAsCompleted();
}
