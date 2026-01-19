package org.example.bookmycut.repositories;

import lombok.NonNull;
import org.example.bookmycut.models.EmployeeTimeOff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmployeeTimeOffRepository
        extends JpaRepository<@NonNull EmployeeTimeOff,@NonNull Long> {

    List<EmployeeTimeOff> findByEmployeeId(Long employeeId);

    boolean existsByEmployeeIdAndStartDateTimeLessThanAndEndDateTimeGreaterThan(
            Long employeeId,
            LocalDateTime end,
            LocalDateTime start
    );

    Optional<EmployeeTimeOff> findByIdAndEmployeeId(Long id, Long employeeId);

    List<EmployeeTimeOff> findByEmployeeIdOrderByStartDateTimeAsc(Long employeeId);

    boolean existsByEmployeeIdAndStartDateTimeLessThanAndEndDateTimeGreaterThanAndIdNot(
            Long employeeId,
            LocalDateTime end,
            LocalDateTime start,
            Long excludeId
    );


}