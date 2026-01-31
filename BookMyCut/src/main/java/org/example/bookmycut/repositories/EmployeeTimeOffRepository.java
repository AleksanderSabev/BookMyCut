package org.example.bookmycut.repositories;

import lombok.NonNull;
import org.example.bookmycut.models.EmployeeTimeOff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmployeeTimeOffRepository
        extends JpaRepository<@NonNull EmployeeTimeOff,@NonNull Long> {

    boolean existsByEmployeeIdAndStartDateTimeLessThanAndEndDateTimeGreaterThan(
            Long employeeId,
            LocalDateTime end,
            LocalDateTime start
    );

    List<EmployeeTimeOff> findByEmployeeIdOrderByStartDateTimeAsc(Long employeeId);

    boolean existsByEmployeeIdAndStartDateTimeLessThanAndEndDateTimeGreaterThanAndIdNot(
            Long employeeId,
            LocalDateTime end,
            LocalDateTime start,
            Long excludeId
    );


}