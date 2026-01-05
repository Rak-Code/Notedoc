package com.rakeshgupta.notedoc_backend.controller;

// Swagger imports removed for lightweight build
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.responses.ApiResponse;
// import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Health check controller for monitoring application status
 */
@RestController
@RequestMapping("/api/health")
// @Tag(name = "Health", description = "Application health monitoring") // Swagger annotation removed
public class HealthController {

    @GetMapping
    // Swagger annotations removed for lightweight build
    /*
    @Operation(
        summary = "Health check endpoint",
        description = "Returns the current health status of the application"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Application is healthy"
    )
    */
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = Map.of(
            "status", "UP",
            "timestamp", LocalDateTime.now(),
            "service", "notedoc-backend",
            "version", "1.0.0"
        );
        
        return ResponseEntity.ok(health);
    }
}