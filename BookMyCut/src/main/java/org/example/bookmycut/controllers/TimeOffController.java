package org.example.bookmycut.controllers;

import org.example.bookmycut.dtos.TimeOffDto;
import org.example.bookmycut.services.contracts.TimeOffService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/timeoff")
public class TimeOffController {

    private final TimeOffService timeOffService;

    public TimeOffController(TimeOffService service) {
        this.timeOffService = service;
    }

    @PostMapping
    public TimeOffDto addTimeOff(@RequestBody TimeOffDto dto) {
        return timeOffService.addTimeOff(dto);
    }

    @PutMapping
    public TimeOffDto updateTimeOff(@RequestBody TimeOffDto dto) {
        return timeOffService.updateTimeOff(dto);
    }

    @DeleteMapping("/{id}")
    public TimeOffDto removeTimeOff(@PathVariable Long id) {
        return timeOffService.removeTimeOff(id);
    }

    @GetMapping("/employee/{employeeId}")
    public List<TimeOffDto> getTimeOffForEmployee(@PathVariable Long employeeId) {
        return timeOffService.getTimeOffForEmployee(employeeId);
    }
}

