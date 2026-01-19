package org.example.bookmycut.controllers;

import org.example.bookmycut.dtos.ScheduleDto;
import org.example.bookmycut.services.contracts.ScheduleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService service) {
        this.scheduleService = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ScheduleDto addSchedule(@RequestBody ScheduleDto dto) {
        return scheduleService.addSchedule(dto);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ScheduleDto updateSchedule(@RequestBody ScheduleDto dto) {
        return scheduleService.updateSchedule(dto);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ScheduleDto removeSchedule(@RequestBody ScheduleDto dto) {
        return scheduleService.removeSchedule(dto);
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDto> getScheduleForEmployee(@PathVariable Long employeeId) {
        return scheduleService.getScheduleForEmployee(employeeId);
    }

    @GetMapping("/employee/{employeeId}/day/{dayOfWeek}")
    public List<ScheduleDto> getScheduleForEmployeeByDay(@PathVariable Long employeeId, @PathVariable int dayOfWeek) {
        return scheduleService.getScheduleForEmployeeByDay(employeeId, dayOfWeek);
    }

    @GetMapping
    public List<ScheduleDto> getAllSchedules() {
        return scheduleService.getAllSchedules();
    }
}

