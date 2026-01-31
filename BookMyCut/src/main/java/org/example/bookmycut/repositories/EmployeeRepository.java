package org.example.bookmycut.repositories;

import lombok.NonNull;
import org.example.bookmycut.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends
        JpaRepository<@NonNull Employee, @NonNull Long> {

    boolean existsByEmail(String email);

    Optional<Employee> findByNameContainingIgnoreCase(String name);

    @Query("SELECT e FROM Employee e JOIN e.procedures p WHERE p.id = :procedureId")
    List<Employee> findAllByProcedureId(@Param("procedureId") Long procedureId);
}
