package org.example.bookmycut.controllers;

import jakarta.validation.Valid;
import lombok.NonNull;
import org.example.bookmycut.dtos.TimeOffDto;
import org.example.bookmycut.services.contracts.TimeOffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( "/time-offs")
public class TimeOffController {

    private static final String UPDATE_SUCCESSFUL = "Time-off updated successfully.";
    private static final String DELETE_SUCCESSFUL = "Time-off deleted successfully.";

    private final TimeOffService timeOffService;

    @Autowired
    public TimeOffController(TimeOffService timeOffService) {
        this.timeOffService = timeOffService;
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<@NonNull TimeOffDto> createTimeOff(@Valid @RequestBody TimeOffDto dto) {
        TimeOffDto created = timeOffService.addTimeOff(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("isAuthenticated()")
    public List<TimeOffDto> getTimeOffsForEmployee(@PathVariable Long employeeId) {
        return timeOffService.getTimeOffForEmployee(employeeId);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<@NonNull String> updateTimeOff(@PathVariable Long id, @Valid @RequestBody TimeOffDto dto) {
        dto.setId(id);
        timeOffService.updateTimeOff(dto);
        return ResponseEntity.ok(UPDATE_SUCCESSFUL);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<@NonNull String> removeTimeOff(@PathVariable Long id) {
        timeOffService.removeTimeOff(id);
        return ResponseEntity.ok(DELETE_SUCCESSFUL);
    }
}