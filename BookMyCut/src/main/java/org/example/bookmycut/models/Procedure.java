package org.example.bookmycut.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "service")
public class Procedure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "duration_minutes")
    private int durationMinutes;

    @Column(name = "price")
    private double price;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "procedures")
    private List<Employee> employees;

    @OneToMany(mappedBy = "procedure", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    public Procedure(String name,
                     int durationMinutes,
                     double price) {
        this.name = name;
        this.durationMinutes = durationMinutes;
        this.price = price;
    }
}
