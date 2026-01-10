package org.example.bookmycut.repositories;

import lombok.NonNull;
import org.example.bookmycut.models.EmployeeSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeScheduleRepository
        extends JpaRepository<@NonNull EmployeeSchedule,@NonNull Long> {

    List<EmployeeSchedule> findByEmployeeId(Long employeeId);

    Optional<EmployeeSchedule> findByEmployeeIdAndDayOfWeek(
            Long employeeId, int dayOfWeek
    );
}