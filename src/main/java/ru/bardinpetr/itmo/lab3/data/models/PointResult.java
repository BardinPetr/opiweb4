package ru.bardinpetr.itmo.lab3.data.models;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "point_result")
public class PointResult implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Point point;

    @Embedded
    private AreaConfig area;

    @Column(nullable = false)
    private Boolean isInside;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private Duration executionTime;
}
