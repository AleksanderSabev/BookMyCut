package org.example.bookmycut.helpers;

import org.example.bookmycut.dtos.AppointmentDto;
import org.example.bookmycut.exceptions.EntityNotFoundException;
import org.example.bookmycut.models.AppUser;
import org.example.bookmycut.models.Appointment;
import org.example.bookmycut.models.Employee;
import org.example.bookmycut.models.Procedure;
import org.example.bookmycut.repositories.AppUserRepository;
import org.example.bookmycut.repositories.EmployeeRepository;
import org.example.bookmycut.repositories.ProcedureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    private final EmployeeRepository employeeRepository;
    private final ProcedureRepository procedureRepository;
    private final AppUserRepository userRepository;

    @Autowired
    public AppointmentMapper(EmployeeRepository employeeRepository, ProcedureRepository procedureRepository, AppUserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.procedureRepository = procedureRepository;
        this.userRepository = userRepository;
    }

    public Appointment fromDto(AppointmentDto dto) {
        Appointment appointment = new Appointment();

        appointment.setStartDatetime(dto.getStartTime());
        appointment.setEndDatetime(dto.getEndTime());

        if (dto.getEmployeeName() != null) {
            Employee employee = employeeRepository.findByName(dto.getEmployeeName())
                    .orElseThrow(() -> new EntityNotFoundException("Employee", "name", dto.getEmployeeName()));
            appointment.setEmployee(employee);
        }

        if (dto.getProcedureName() != null) {
            Procedure procedure = procedureRepository.findByName(dto.getProcedureName())
                    .orElseThrow(() -> new EntityNotFoundException("Procedure", "name", dto.getProcedureName()));
            appointment.setProcedure(procedure);
        }

        if (dto.getUsername() != null) {
            AppUser user = userRepository.findByUsername(dto.getUsername())
                    .orElseThrow(() -> new EntityNotFoundException("User", "username", dto.getUsername()));
            appointment.setUser(user);
        }

        return appointment;
    }

    public AppointmentDto toDto(Appointment appointment){
        return new AppointmentDto(appointment.getId(),
                appointment.getEmployee().getName(),
                appointment.getProcedure().getName(),
                appointment.getUser().getUsername(),
                appointment.getStartDatetime(),
                appointment.getEndDatetime());
    }
}
