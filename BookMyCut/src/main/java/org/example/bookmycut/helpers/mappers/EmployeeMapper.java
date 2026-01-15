package org.example.bookmycut.helpers.mappers;

import org.example.bookmycut.models.Employee;
import org.example.bookmycut.models.Procedure;
import org.example.bookmycut.dtos.EmployeeDto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class EmployeeMapper {

    public EmployeeDto toDto(Employee employee) {
        if (employee == null) return null;

        List<Long> procedureIds = employee.getProcedures() != null
                ? employee.getProcedures().stream().map(Procedure::getId).toList()
                : Collections.emptyList();

        return new EmployeeDto(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getPhone(),
                procedureIds
        );
    }

    public Employee toEntity(EmployeeDto dto, List<Procedure> procedures) {
        if (dto == null) return null;

        Employee employee = new Employee(
                dto.getName(),
                dto.getEmail(),
                dto.getPhone()
        );

        if (procedures != null) {
            employee.setProcedures(procedures);
        }

        if (dto.getId() != null) {
            employee.setId(dto.getId());
        }

        return employee;
    }
}
