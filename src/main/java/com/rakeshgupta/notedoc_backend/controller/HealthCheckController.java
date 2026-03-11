package com.rakeshgupta.notedoc_backend.controller;

import com.rakeshgupta.notedoc_backend.entity.HealthCheck;
import com.rakeshgupta.notedoc_backend.repository.HealthCheckRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/db-health")
@RequiredArgsConstructor
@Slf4j
public class HealthCheckController {
    
    private final HealthCheckRepository healthCheckRepository;
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> checkDatabaseHealth() {
        try {
            // Perform a database operation
            HealthCheck healthCheck = new HealthCheck(
                "ACTIVE",
                LocalDateTime.now(),
                "Database connection verified"
            );
            healthCheckRepository.save(healthCheck);
            
            // Get recent checks
            List<HealthCheck> recentChecks = healthCheckRepository.findTop10ByOrderByCheckTimeDesc();
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "UP");
            response.put("timestamp", LocalDateTime.now());
            response.put("database", "CONNECTED");
            response.put("totalChecks", healthCheckRepository.count());
            response.put("recentChecks", recentChecks);
            
            log.info("Database health check successful");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Database health check failed: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("status", "DOWN");
            response.put("timestamp", LocalDateTime.now());
            response.put("error", e.getMessage());
            return ResponseEntity.status(503).body(response);
        }
    }
}
