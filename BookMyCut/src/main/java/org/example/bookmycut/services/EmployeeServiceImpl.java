package org.example.bookmycut.services;

import jakarta.persistence.EntityNotFoundException;
import org.example.bookmycut.dtos.EmployeeDto;
import org.example.bookmycut.dtos.ProcedureDto;
import org.example.bookmycut.helpers.mappers.EmployeeMapper;
import org.example.bookmycut.models.Employee;
import org.example.bookmycut.models.Procedure;
import org.example.bookmycut.repositories.EmployeeRepository;
import org.example.bookmycut.repositories.ProcedureRepository;
import org.example.bookmycut.services.contracts.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final ProcedureRepository procedureRepository;

    private final EmployeeMapper employeeMapper;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               ProcedureRepository procedureRepository,
                               EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.procedureRepository = procedureRepository;
        this.employeeMapper = employeeMapper;
    }


    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        List<Procedure> procedures = Collections.emptyList();
        if (employeeDto.getProcedureIds() != null && !employeeDto.getProcedureIds().isEmpty()) {
            procedures = procedureRepository.findAllById(employeeDto.getProcedureIds());

            if (procedures.size() != employeeDto.getProcedureIds().size()) {
                List<Long> foundIds = procedures.stream().map(Procedure::getId).toList();
                List<Long> missingIds = employeeDto.getProcedureIds().stream()
                        .filter(id -> !foundIds.contains(id))
                        .toList();
                throw new EntityNotFoundException("Procedures not found with ids: " + missingIds);
            }
        }

        Employee employee = employeeMapper.toEntity(employeeDto, procedures);

        Employee saved = employeeRepository.save(employee);

        return employeeMapper.toDto(saved);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        return List.of();
    }

    @Override
    public EmployeeDto getEmployeeById(Long id) {
        return null;
    }

    @Override
    public void assignProcedureToEmployee(Long employeeId, Long procedureId) {

    }

    @Override
    public List<ProcedureDto> getProceduresForEmployee(Long employeeId) {
        return List.of();
    }

    @Override
    public void deleteEmployee(Long id) {

    }
}
