package com.rakeshgupta.notedoc_backend.service;

import com.rakeshgupta.notedoc_backend.entity.HealthCheck;
import com.rakeshgupta.notedoc_backend.repository.HealthCheckRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseKeepAliveService {
    
    private final HealthCheckRepository healthCheckRepository;
    
    /**
     * Scheduled task to keep database connection alive
     * Runs every 30 minutes (1800000 milliseconds)
     */
    @Scheduled(fixedRate = 1800000) // 30 minutes
    public void keepDatabaseAlive() {
        try {
            log.info("🔄 Running scheduled database keep-alive check...");
            
            HealthCheck healthCheck = new HealthCheck(
                "SCHEDULED_CHECK",
                LocalDateTime.now(),
                "Automated keep-alive ping"
            );
            
            healthCheckRepository.save(healthCheck);
            
            long totalChecks = healthCheckRepository.count();
            log.info("✅ Database keep-alive successful. Total checks: {}", totalChecks);
            
        } catch (Exception e) {
            log.error("❌ Database keep-alive failed: {}", e.getMessage(), e);
        }
    }
}
