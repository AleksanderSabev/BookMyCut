package org.example.bookmycut.helpers.mappers;

import org.example.bookmycut.dtos.AppointmentDto;
import org.example.bookmycut.models.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public AppointmentDto toDto(Appointment appointment){
        return new AppointmentDto(appointment.getId(),
                appointment.getEmployee().getName(),
                appointment.getProcedure().getName(),
                appointment.getUser().getUsername(),
                appointment.getStartDatetime(),
                appointment.getEndDatetime());
    }
}
