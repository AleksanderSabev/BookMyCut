package org.example.bookmycut.repositories;

import lombok.NonNull;
import org.example.bookmycut.models.EmployeeSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface EmployeeScheduleRepository
        extends JpaRepository<@NonNull EmployeeSchedule, @NonNull Long> {


    Optional<EmployeeSchedule> findByEmployeeIdAndDayOfWeek(
            Long employeeId, int dayOfWeek
    );

    @Query("""
                SELECT CASE WHEN COUNT(es) > 0 THEN true ELSE false END
                FROM EmployeeSchedule es
                WHERE es.employee.id = :employeeId
                  AND es.dayOfWeek = :dayOfWeek
                  AND (
                        (es.startTime < :endTime AND es.endTime > :startTime)
                      )
            """)
    boolean existsByEmployeeIdAndDayOfWeekAndTimeOverlap(
            @Param("employeeId") Long employeeId,
            @Param("dayOfWeek") int dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    @Query("""
    SELECT CASE WHEN COUNT(es) > 0 THEN true ELSE false END
    FROM EmployeeSchedule es
    WHERE es.employee.id = :employeeId
      AND es.dayOfWeek = :dayOfWeek
      AND es.startTime < :newEndTime
      AND es.endTime > :newStartTime
      AND es.id <> :scheduleId
""")
    boolean existsByEmployeeIdAndDayOfWeekAndTimeOverlapExcludingId(
            @Param("employeeId") Long employeeId,
            @Param("dayOfWeek") int dayOfWeek,
            @Param("newStartTime") LocalTime newStartTime,
            @Param("newEndTime") LocalTime newEndTime,
            @Param("scheduleId") Long scheduleId
    );


    List<EmployeeSchedule> findByEmployeeIdOrderByDayOfWeekAscStartTimeAsc(Long employeeId);
}
