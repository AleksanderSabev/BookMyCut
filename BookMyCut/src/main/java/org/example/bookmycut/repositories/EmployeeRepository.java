package org.example.bookmycut.repositories;

import lombok.NonNull;
import org.example.bookmycut.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends
        JpaRepository<@NonNull Employee, @NonNull Long> {

    Optional<Employee> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Employee> findByName(String name);
}
