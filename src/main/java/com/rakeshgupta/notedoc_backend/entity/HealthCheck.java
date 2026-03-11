package com.rakeshgupta.notedoc_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "health_checks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthCheck {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String status;
    
    @Column(name = "check_time", nullable = false)
    private LocalDateTime checkTime;
    
    @Column(name = "message")
    private String message;
    
    public HealthCheck(String status, LocalDateTime checkTime, String message) {
        this.status = status;
        this.checkTime = checkTime;
        this.message = message;
    }
}
