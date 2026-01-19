package org.example.bookmycut.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "employee_time_off")
public class EmployeeTimeOff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "start_datetime")
    private LocalDateTime startDateTime;

    @Column(name = "end_datetime")
    private LocalDateTime endDateTime;

    @Column(name = "reason")
    private String reason;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public EmployeeTimeOff(Employee employee,
                           LocalDateTime startDatetime,
                           LocalDateTime endDatetime,
                           String reason) {
        this.employee = employee;
        this.startDateTime = startDatetime;
        this.endDateTime = endDatetime;
        this.reason = reason;
    }
}