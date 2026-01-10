package org.example.bookmycut.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bookmycut.enums.AppointmentStatus;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Procedure procedure;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(name = "start_datetime", nullable = false)
    private LocalDateTime startDatetime;

    @Column(name = "end_datetime", nullable = false)
    private LocalDateTime endDatetime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Appointment(Employee employee,
                       Procedure procedure,
                       AppUser user,
                       LocalDateTime startDatetime,
                       LocalDateTime endDatetime) {
        this.employee = employee;
        this.procedure = procedure;
        this.user = user;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
    }
}