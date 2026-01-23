package org.example.bookmycut.services.contracts;

import org.example.bookmycut.dtos.appointment.AppointmentRequestDto;
import org.example.bookmycut.dtos.appointment.AppointmentResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

    AppointmentResponseDto bookAppointment(
            Long userId, AppointmentRequestDto dto
    );

    void cancelAppointment(Long appointmentId, Long userId);

    List<AppointmentResponseDto> getAppointmentsForEmployee(
            Long employeeId,
            LocalDate date
    );

    List<AppointmentResponseDto> getAppointmentsForUser(Long userId);

    void markPastAppointmentsAsCompleted();
}
