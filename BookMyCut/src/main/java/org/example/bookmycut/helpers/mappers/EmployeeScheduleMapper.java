package org.example.bookmycut.helpers.mappers;

import org.example.bookmycut.dtos.EmployeeDto;
import org.example.bookmycut.dtos.ScheduleDto;
import org.example.bookmycut.models.Employee;
import org.example.bookmycut.models.EmployeeSchedule;
import org.example.bookmycut.models.Procedure;
import org.springframework.stereotype.Component;

@Component
public class EmployeeScheduleMapper {

    public EmployeeSchedule toEntity(ScheduleDto dto, Employee employee) {
        if (dto == null || employee == null) return null;

        return new EmployeeSchedule(
                employee,
                dto.getDayOfWeek(),
                dto.getStartTime(),
                dto.getEndTime()
        );
    }

    public ScheduleDto toDTO(EmployeeSchedule entity) {
        if (entity == null) return null;

        EmployeeDto employeeDTO = entity.getEmployee() != null
                ? new EmployeeDto(entity.getEmployee().getId(),
                entity.getEmployee().getName(),
                entity.getEmployee().getEmail(),
                entity.getEmployee().getPhone(),
                entity.getEmployee().getProcedures().stream().map(Procedure::getId).toList())
                : null;

        return new ScheduleDto(
                entity.getId(),
                employeeDTO,
                entity.getDayOfWeek(),
                entity.getStartTime(),
                entity.getEndTime()

        );
    }
}
