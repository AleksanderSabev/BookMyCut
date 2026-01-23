package org.example.bookmycut.dtos.appointment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponseDto {

    private Long id;

    private String employeeName;

    private String procedureName;

    private String username;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}

