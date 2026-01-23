package org.example.bookmycut.controllers;

import jakarta.validation.Valid;
import lombok.NonNull;
import org.example.bookmycut.dtos.appointment.AppointmentRequestDto;
import org.example.bookmycut.dtos.appointment.AppointmentResponseDto;
import org.example.bookmycut.helpers.security.SecurityUtils;
import org.example.bookmycut.services.contracts.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final String SUCCESSFUL_APPOINTMENT_CANCELLATION = "Appointment cancelled successfully.";

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService service) {
        this.appointmentService = service;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public AppointmentResponseDto bookAppointment(@Valid @RequestBody AppointmentRequestDto dto) {
        Long userId = SecurityUtils.getCurrentUserId();
        return appointmentService.bookAppointment(userId, dto);
    }

    // User cancels appointment
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<@NonNull String> cancelAppointment(@PathVariable Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        appointmentService.cancelAppointment(id, userId);
        return ResponseEntity.ok(SUCCESSFUL_APPOINTMENT_CANCELLATION);
    }

    // User updates appointment
    @PutMapping("/{id}")
    public AppointmentResponseDto updateAppointment(@PathVariable Long id, @RequestBody AppointmentResponseDto dto) {
        return null;//TODO Add update method
    }

    // Get appointments for a user
    @GetMapping("/user/{userId}")
    public List<AppointmentResponseDto> getAppointmentsForUser(@PathVariable Long userId) {
        return null;
    }

    // Get appointments for an employee
    @GetMapping("/employee/{employeeId}")
    public List<AppointmentResponseDto> getAppointmentsForEmployee(@PathVariable Long employeeId) {
        return null;
    }
}

