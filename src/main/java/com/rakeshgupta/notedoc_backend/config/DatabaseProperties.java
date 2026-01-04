package com.rakeshgupta.notedoc_backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for database connection
 * Maps to environment variables loaded from .env file
 */
@Data
@Component
@ConfigurationProperties(prefix = "db")
public class DatabaseProperties {
    
    private String host;
    private String port;
    private String name;
    private String username;
    private String password;
    
    /**
     * Get the complete JDBC URL for PostgreSQL
     */
    public String getJdbcUrl() {
        return String.format("jdbc:postgresql://%s:%s/%s?sslmode=require", 
                host, port, name);
    }
}