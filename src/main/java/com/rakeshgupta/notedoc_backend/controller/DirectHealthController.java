package com.rakeshgupta.notedoc_backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Direct health check controller without /api prefix
 */
@RestController
@RequestMapping("/health")
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:4200", "https://notedoc-alpha.vercel.app"})
public class DirectHealthController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> healthCheck() {
        log.info("Health check requested");
        
        Map<String, Object> health = Map.of(
            "status", "UP",
            "timestamp", LocalDateTime.now(),
            "service", "notedoc-backend",
            "version", "1.0.0"
        );
        
        return ResponseEntity.ok(health);
    }

    /**
     * Handle preflight OPTIONS requests
     */
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> handleOptions() {
        return ResponseEntity.ok().build();
    }
}