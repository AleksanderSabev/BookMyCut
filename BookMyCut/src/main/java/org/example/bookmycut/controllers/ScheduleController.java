package org.example.bookmycut.controllers;

import jakarta.validation.Valid;
import lombok.NonNull;
import org.example.bookmycut.dtos.ScheduleDto;
import org.example.bookmycut.services.contracts.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    private static final String UPDATE_SUCCESSFUL = "Schedule updated successfully.";
    private static final String DELETE_SUCCESSFUL = "Schedule deleted successfully.";

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService service) {
        this.scheduleService = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<@NonNull ScheduleDto> createSchedule(@Valid  @RequestBody ScheduleDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(scheduleService.addSchedule(dto));
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<@NonNull String> updateSchedule(@RequestBody ScheduleDto dto) {
        scheduleService.updateSchedule(dto);
        return ResponseEntity.ok(UPDATE_SUCCESSFUL);
    }

    @DeleteMapping("/{employeeId}/{day}")
    @PreAuthorize("hasRole('ADMIN')")
    public  ResponseEntity<@NonNull String> removeSchedule(@PathVariable Long employeeId,
                                                           @PathVariable DayOfWeek day) {
        scheduleService.removeSchedule(employeeId, day);
        return ResponseEntity.ok(DELETE_SUCCESSFUL);
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("isAuthenticated()")
    public List<ScheduleDto> getScheduleForEmployee(@PathVariable Long employeeId) {
        return scheduleService.getScheduleForEmployee(employeeId);
    }

    @GetMapping("/employee/{employeeId}/{dayOfWeek}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<@NonNull ScheduleDto> getScheduleForEmployeeByDay(@PathVariable Long employeeId, @PathVariable DayOfWeek dayOfWeek) {
        return ResponseEntity.ok(scheduleService.getScheduleForEmployeeByDay(employeeId, dayOfWeek));
    }

    @GetMapping
    public List<ScheduleDto> getAllSchedules() {
        return scheduleService.getAllSchedules();
    }
}

