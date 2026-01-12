package org.example.bookmycut.repositories;

import lombok.NonNull;
import org.example.bookmycut.models.EmployeeTimeOff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EmployeeTimeOffRepository
        extends JpaRepository<@NonNull EmployeeTimeOff,@NonNull Long> {

    List<EmployeeTimeOff> findByEmployeeId(Long employeeId);

    boolean existsByEmployeeIdAndStartDatetimeLessThanAndEndDatetimeGreaterThan(
            Long employeeId,
            LocalDateTime end,
            LocalDateTime start
    );
}