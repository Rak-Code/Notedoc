package com.rakeshgupta.notedoc_backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom initializer to load .env file before Spring Boot starts
 * This ensures environment variables are available during application startup
 */
@Slf4j
@Component
public class DotEnvConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        loadDotEnvFile(environment);
    }

    private void loadDotEnvFile(ConfigurableEnvironment environment) {
        try {
            Path envFile = Paths.get(".env");
            if (!Files.exists(envFile)) {
                log.warn("⚠️ .env file not found at: {}", envFile.toAbsolutePath());
                return;
            }

            Map<String, Object> envVars = new HashMap<>();
            Files.lines(envFile)
                    .filter(line -> !line.trim().isEmpty())
                    .filter(line -> !line.trim().startsWith("#"))
                    .filter(line -> line.contains("="))
                    .forEach(line -> {
                        String[] parts = line.split("=", 2);
                        if (parts.length == 2) {
                            String key = parts[0].trim();
                            String value = parts[1].trim();
                            // Remove quotes if present
                            if (value.startsWith("\"") && value.endsWith("\"")) {
                                value = value.substring(1, value.length() - 1);
                            }
                            envVars.put(key, value);
                            log.debug("Loaded env var: {} = {}", key, key.contains("PASSWORD") ? "***" : value);
                        }
                    });

            if (!envVars.isEmpty()) {
                environment.getPropertySources().addFirst(new MapPropertySource("dotenv", envVars));
                log.info("✅ Successfully loaded {} environment variables from .env file", envVars.size());
                
                // Validate critical DB variables
                String dbHost = (String) envVars.get("DB_HOST");
                String dbPort = (String) envVars.get("DB_PORT");
                if (dbHost != null && dbPort != null) {
                    log.info("🔗 Database connection configured: {}:{}", dbHost, dbPort);
                } else {
                    log.error("❌ Missing critical database environment variables!");
                }
            } else {
                log.warn("⚠️ No valid environment variables found in .env file");
            }

        } catch (IOException e) {
            log.error("❌ Failed to load .env file: {}", e.getMessage());
        }
    }
}