package org.example.bookmycut.services.contracts;

import org.example.bookmycut.models.Employee;
import org.example.bookmycut.models.Procedure;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    Employee createEmployee(Employee employee);

    List<Employee> getAllEmployees();

    Optional<Employee> getEmployeeById(Long id);

    void assignServiceToEmployee(Long employeeId, Long serviceId);

    List<Procedure> getServicesForEmployee(Long employeeId);
}
