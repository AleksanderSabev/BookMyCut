package org.example.bookmycut.repositories;

import lombok.NonNull;
import org.example.bookmycut.enums.AppointmentStatus;
import org.example.bookmycut.models.AppUser;
import org.example.bookmycut.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends
        JpaRepository<@NonNull Appointment, @NonNull Long> {

    List<Appointment> findByEmployeeId(Long employeeId);

    List<Appointment> findByEmployeeIdAndStartDatetimeBetween(
            Long employeeId,
            LocalDateTime start,
            LocalDateTime end
    );

    boolean existsByEmployeeIdAndStartDatetimeLessThanAndEndDatetimeGreaterThan(
            Long employeeId,
            LocalDateTime end,
            LocalDateTime start
    );

    List<Appointment >findByStatusAndEndDatetimeBefore(AppointmentStatus status, LocalDateTime dateTime);

    List<Appointment> findByUserAndStatus(AppUser user, AppointmentStatus status);

    boolean existsByProcedureId(Long procedureId);
}
