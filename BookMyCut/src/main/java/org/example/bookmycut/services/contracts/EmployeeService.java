package org.example.bookmycut.services.contracts;

import org.example.bookmycut.dtos.EmployeeDto;
import org.example.bookmycut.dtos.ProcedureDto;

import java.util.List;

public interface EmployeeService {

    EmployeeDto createEmployee(EmployeeDto employeeDto);

    List<EmployeeDto> getAllEmployees();

    EmployeeDto getEmployeeById(Long id);

    void updateEmployee(Long id, EmployeeDto dto);

    void assignProcedureToEmployee(Long employeeId, Long procedureId);

    List<ProcedureDto> getProceduresForEmployee(Long employeeId);

    void removeEmployee(Long id);
}
