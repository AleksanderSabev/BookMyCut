package org.example.bookmycut.helpers.mappers;

import org.example.bookmycut.dtos.appointment.AppointmentRequestDto;
import org.example.bookmycut.dtos.appointment.AppointmentResponseDto;
import org.example.bookmycut.models.AppUser;
import org.example.bookmycut.models.Appointment;
import org.example.bookmycut.models.Employee;
import org.example.bookmycut.models.Procedure;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public AppointmentResponseDto toDto(Appointment appointment){
        return new AppointmentResponseDto(appointment.getId(),
                appointment.getEmployee().getName(),
                appointment.getProcedure().getName(),
                appointment.getUser().getUsername(),
                appointment.getStartDatetime(),
                appointment.getEndDatetime());
    }

    public Appointment toEntity(AppointmentRequestDto dto, AppUser user, Employee employee, Procedure procedure) {
        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setEmployee(employee);
        appointment.setProcedure(procedure);
        appointment.setStartDatetime(dto.getStartDateTime());
        return appointment;
    }
}
