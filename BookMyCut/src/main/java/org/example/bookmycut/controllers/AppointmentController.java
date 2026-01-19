package org.example.bookmycut.controllers;

import org.example.bookmycut.dtos.AppointmentDto;
import org.example.bookmycut.services.contracts.AppointmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService service) {
        this.appointmentService = service;
    }

    // User books an appointment
    @PostMapping
    public AppointmentDto bookAppointment(@RequestBody AppointmentDto dto) {
        return null;//TODO check the dto and the method params
    }

    // User cancels appointment
    @DeleteMapping("/{id}")
    public AppointmentDto cancelAppointment(@PathVariable Long id) {
        return null;//TODO fix consistency for return types on delete methods
    }

    // User updates appointment
    @PutMapping("/{id}")
    public AppointmentDto updateAppointment(@PathVariable Long id, @RequestBody AppointmentDto dto) {
        return null;//TODO Add update method
    }

    // Get appointments for a user
    @GetMapping("/user/{userId}")
    public List<AppointmentDto> getAppointmentsForUser(@PathVariable Long userId) {
        return null;
    }

    // Get appointments for an employee
    @GetMapping("/employee/{employeeId}")
    public List<AppointmentDto> getAppointmentsForEmployee(@PathVariable Long employeeId) {
        return null;
    }
}

