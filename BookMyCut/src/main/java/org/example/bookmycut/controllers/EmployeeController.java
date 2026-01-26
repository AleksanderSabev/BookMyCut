package org.example.bookmycut.controllers;

import jakarta.validation.Valid;
import lombok.NonNull;
import org.example.bookmycut.dtos.EmployeeDto;
import org.example.bookmycut.dtos.ProcedureDto;
import org.example.bookmycut.services.contracts.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final String UPDATE_SUCCESSFUL = "Employee updated successfully.";
    private final String DELETE_SUCCESSFUL = "Employee deleted successfully.";
    private final String PROCEDURE_ASSIGNED = "Procedure assigned to employee successfully.";

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService service) {
        this.employeeService = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<@NonNull EmployeeDto> createEmployee(@Valid @RequestBody EmployeeDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeService.createEmployee(dto));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<@NonNull EmployeeDto> getEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<EmployeeDto> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<@NonNull String> updateEmployee(@PathVariable Long id,
                                                          @Valid @RequestBody EmployeeDto dto) {
        employeeService.updateEmployee(id, dto);
        return ResponseEntity.ok(UPDATE_SUCCESSFUL);
    }

    @PostMapping("/{employeeId}/procedures/{procedureId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<@NonNull String> assignProcedureToEmployee(
            @PathVariable Long employeeId,
            @PathVariable Long procedureId) {

        employeeService.assignProcedureToEmployee(employeeId, procedureId);
        return ResponseEntity.ok(PROCEDURE_ASSIGNED);
    }

    @GetMapping("/{employeeId}/procedures")
    @PreAuthorize("isAuthenticated()")
    public List<ProcedureDto> getProceduresForEmployee(@PathVariable Long employeeId) {
        return employeeService.getProceduresForEmployee(employeeId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<@NonNull String> removeEmployee(@PathVariable Long id) {
        employeeService.removeEmployee(id);
        return ResponseEntity.ok(DELETE_SUCCESSFUL);
    }
}

