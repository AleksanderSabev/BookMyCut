package org.example.bookmycut.controllers;

import jakarta.validation.Valid;
import lombok.NonNull;
import org.example.bookmycut.dtos.appointment.AppointmentRequestDto;
import org.example.bookmycut.dtos.appointment.AppointmentResponseDto;
import org.example.bookmycut.helpers.security.SecurityUtils;
import org.example.bookmycut.services.contracts.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private static final String SUCCESSFUL_APPOINTMENT_CANCELLATION = "Appointment cancelled successfully.";

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService service) {
        this.appointmentService = service;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<@NonNull AppointmentResponseDto> bookAppointment(@Valid @RequestBody AppointmentRequestDto dto) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(appointmentService.bookAppointment(userId, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<@NonNull String> cancelAppointment(@PathVariable Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        appointmentService.cancelAppointment(id, userId);
        return ResponseEntity.ok(SUCCESSFUL_APPOINTMENT_CANCELLATION);
    }

    @GetMapping("/my-appointments")
    @PreAuthorize("isAuthenticated()")
    public List<AppointmentResponseDto> getAppointmentsForUser() {
        Long userId = SecurityUtils.getCurrentUserId();

        return appointmentService.getAppointmentsForUser(userId);
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AppointmentResponseDto> getAppointmentsForEmployee(@PathVariable Long employeeId,
                                                                   @RequestParam(required = false) LocalDate date) {
        LocalDate queryDate = (date != null) ? date : LocalDate.now();
        return appointmentService.getAppointmentsForEmployee(employeeId, queryDate);
    }
}

