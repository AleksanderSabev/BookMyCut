package org.example.bookmycut.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bookmycut.exceptions.DuplicateEntityException;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(
            name = "employee_service",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<Procedure> procedures = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<EmployeeSchedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<EmployeeTimeOff> timeOffs = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Appointment> appointments = new ArrayList<>();

    public Employee(String name,
                    String email,
                    String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public void addProcedure(Procedure procedure) {
        if (procedures.contains(procedure)) {
            throw new DuplicateEntityException("Procedure");
        }
        procedures.add(procedure);
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
        appointment.setEmployee(this);
    }

    public void cancelAppointment(Appointment appointment) {
        appointments.remove(appointment);
        appointment.setEmployee(null);
    }
}
