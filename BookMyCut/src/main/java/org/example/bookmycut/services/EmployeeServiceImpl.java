package org.example.bookmycut.services;

import org.example.bookmycut.enums.AppointmentStatus;
import org.example.bookmycut.exceptions.EntityHasAppointmentsException;
import org.example.bookmycut.exceptions.EntityNotFoundException;
import org.example.bookmycut.dtos.EmployeeDto;
import org.example.bookmycut.dtos.ProcedureDto;
import org.example.bookmycut.helpers.mappers.EmployeeMapper;
import org.example.bookmycut.helpers.mappers.ProcedureMapper;
import org.example.bookmycut.models.Employee;
import org.example.bookmycut.models.Procedure;
import org.example.bookmycut.repositories.EmployeeRepository;
import org.example.bookmycut.repositories.ProcedureRepository;
import org.example.bookmycut.services.contracts.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final ProcedureRepository procedureRepository;

    private final EmployeeMapper employeeMapper;

    private final ProcedureMapper procedureMapper;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               ProcedureRepository procedureRepository,
                               EmployeeMapper employeeMapper, ProcedureMapper procedureMapper) {
        this.employeeRepository = employeeRepository;
        this.procedureRepository = procedureRepository;
        this.employeeMapper = employeeMapper;
        this.procedureMapper = procedureMapper;
    }

    @Transactional
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
                throw new EntityNotFoundException("Procedures", missingIds);
            }
        }

        Employee employee = employeeMapper.toEntity(employeeDto, procedures);

        Employee saved = employeeRepository.save(employee);

        return employeeMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public EmployeeDto getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee", id));

        return employeeMapper.toDto(employee);
    }

    @Transactional
    @Override
    public void assignProcedureToEmployee(Long employeeId, Long procedureId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee", employeeId));
        Procedure procedure = procedureRepository.findById(procedureId)
                .orElseThrow(() -> new EntityNotFoundException("Procedure", procedureId));
        employee.addProcedure(procedure);

        employeeRepository.save(employee);

    }

    @Transactional(readOnly = true)
    @Override
    public List<ProcedureDto> getProceduresForEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee", employeeId));

        return employee.getProcedures().stream()
                .map(procedureMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee", id));

        boolean hasActiveAppointments = employee.getAppointments().stream()
                .anyMatch(a -> a.getStatus() == AppointmentStatus.SCHEDULED);

        if (hasActiveAppointments) {
            throw new EntityHasAppointmentsException("Employee", id);
        }

        employee.getProcedures().clear();

        employeeRepository.delete(employee);
    }
}
