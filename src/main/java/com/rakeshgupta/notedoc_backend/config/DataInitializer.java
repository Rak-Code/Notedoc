package com.rakeshgupta.notedoc_backend.config;

import com.rakeshgupta.notedoc_backend.entity.HealthCheck;
import com.rakeshgupta.notedoc_backend.repository.HealthCheckRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final HealthCheckRepository healthCheckRepository;
    
    @Override
    public void run(String... args) {
        // Only initialize if table is empty
        if (healthCheckRepository.count() == 0) {
            log.info("🌱 Initializing health check data...");
            
            List<HealthCheck> initialData = Arrays.asList(
                new HealthCheck("INITIALIZED", LocalDateTime.now().minusHours(5), "System startup check 1"),
                new HealthCheck("INITIALIZED", LocalDateTime.now().minusHours(4), "System startup check 2"),
                new HealthCheck("INITIALIZED", LocalDateTime.now().minusHours(3), "System startup check 3"),
                new HealthCheck("INITIALIZED", LocalDateTime.now().minusHours(2), "System startup check 4"),
                new HealthCheck("INITIALIZED", LocalDateTime.now().minusHours(1), "System startup check 5")
            );
            
            healthCheckRepository.saveAll(initialData);
            log.info("✅ Successfully initialized {} health check records", initialData.size());
        } else {
            log.info("ℹ️ Health check data already exists. Skipping initialization.");
        }
    }
}
