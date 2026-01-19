package org.example.bookmycut.controllers;

import org.example.bookmycut.dtos.EmployeeDto;
import org.example.bookmycut.services.contracts.EmployeeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService service) {
        this.employeeService = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public EmployeeDto createEmployee(@RequestBody EmployeeDto dto) {
        return employeeService.createEmployee(dto);
    }

    @GetMapping("/{id}")
    public EmployeeDto getEmployee(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @GetMapping
    public List<EmployeeDto> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

//    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public EmployeeDto updateEmployee(@PathVariable Long id, @RequestBody EmployeeDto dto) {
//        return employeeService.updateEmployee(dto);
//    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public EmployeeDto removeEmployee(@PathVariable Long id) {
        return null;
    }
}

