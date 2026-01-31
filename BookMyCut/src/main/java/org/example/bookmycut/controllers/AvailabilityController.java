package org.example.bookmycut.controllers;

import lombok.NonNull;
import org.example.bookmycut.services.contracts.AvailabilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/availability")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @GetMapping("/slots")
    public ResponseEntity<@NonNull List<LocalTime>> getAvailableSlots(
            @RequestParam Long employeeId,
            @RequestParam LocalDate date,
            @RequestParam int durationMinutes) {

        List<LocalTime> slots = availabilityService.getAvailableStartTimes(employeeId, date, durationMinutes);
        return ResponseEntity.ok(slots);
    }
}
