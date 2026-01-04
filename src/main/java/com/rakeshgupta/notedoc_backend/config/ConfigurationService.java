package com.rakeshgupta.notedoc_backend.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service to demonstrate usage of configuration properties
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigurationService {
    
    private final DatabaseProperties databaseProperties;
    
    /**
     * Log the current database configuration (without password for security)
     */
    public void logDatabaseConfig() {
        log.info("Database Configuration:");
        log.info("Host: {}", databaseProperties.getHost());
        log.info("Port: {}", databaseProperties.getPort());
        log.info("Database: {}", databaseProperties.getName());
        log.info("Username: {}", databaseProperties.getUsername());
        log.info("JDBC URL: {}", databaseProperties.getJdbcUrl());
    }
    
    /**
     * Get database properties for programmatic access
     */
    public DatabaseProperties getDatabaseProperties() {
        return databaseProperties;
    }
}