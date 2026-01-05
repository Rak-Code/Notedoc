package com.rakeshgupta.notedoc_backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * CORS configuration for allowing cross-origin requests from frontend applications
 * Supports both development (localhost) and production (Vercel) environments
 * Includes security considerations and flexible domain matching
 */
@Configuration
@Slf4j
public class CorsConfig implements WebMvcConfigurer {

    @Value("${cors.allowed-origins:http://localhost:3000,http://localhost:5173,http://localhost:4200}")
    private String allowedOrigins;

    @Value("${cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}")
    private String allowedMethods;

    @Value("${cors.allowed-headers:*}")
    private String allowedHeaders;

    @Value("${cors.allow-credentials:true}")
    private boolean allowCredentials;

    @Value("${cors.max-age:3600}")
    private long maxAge;

    @Value("${cors.vercel-domain-pattern:.*\\.vercel\\.app}")
    private String vercelDomainPattern;

    @Value("${cors.production-domains:}")
    private String productionDomains;

    /**
     * Global CORS configuration using WebMvcConfigurer
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        List<String> origins = getAllowedOriginsList();
        List<String> methods = Arrays.asList(allowedMethods.split(","));
        List<String> headers = Arrays.asList(allowedHeaders.split(","));

        log.info("Configuring CORS with allowed origins: {}", origins);

        registry.addMapping("/api/**")
                .allowedOrigins(origins.toArray(new String[0]))
                .allowedMethods(methods.toArray(new String[0]))
                .allowedHeaders(headers.toArray(new String[0]))
                .allowCredentials(allowCredentials)
                .maxAge(maxAge);

        // Swagger UI CORS mappings removed for lightweight build
        /*
        registry.addMapping("/swagger-ui/**")
                .allowedOrigins(origins.toArray(new String[0]))
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(allowCredentials)
                .maxAge(maxAge);

        registry.addMapping("/api-docs/**")
                .allowedOrigins(origins.toArray(new String[0]))
                .allowedMethods("GET", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(allowCredentials)
                .maxAge(maxAge);
        */
    }

    /**
     * Bean-based CORS configuration with custom origin validation
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Custom CORS configuration that validates origins dynamically
        configuration.setAllowedOriginPatterns(getAllowedOriginPatterns());
        
        // Parse allowed methods from configuration
        List<String> methods = Arrays.asList(allowedMethods.split(","));
        configuration.setAllowedMethods(methods);
        
        // Parse allowed headers from configuration
        if ("*".equals(allowedHeaders)) {
            configuration.addAllowedHeader("*");
        } else {
            List<String> headers = Arrays.asList(allowedHeaders.split(","));
            configuration.setAllowedHeaders(headers);
        }
        
        // Set other CORS properties
        configuration.setAllowCredentials(allowCredentials);
        configuration.setMaxAge(maxAge);
        
        // Expose common headers that frontend might need
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "X-Total-Count",
            "X-Page-Number",
            "X-Page-Size",
            "Location"
        ));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        // Swagger CORS configurations removed for lightweight build
        // source.registerCorsConfiguration("/swagger-ui/**", configuration);
        // source.registerCorsConfiguration("/api-docs/**", configuration);
        
        return source;
    }

    /**
     * Get list of allowed origins including localhost and production domains
     */
    private List<String> getAllowedOriginsList() {
        List<String> origins = Arrays.asList(allowedOrigins.split(","));
        
        // Add production domains if specified
        if (!productionDomains.isEmpty()) {
            List<String> prodDomains = Arrays.asList(productionDomains.split(","));
            origins.addAll(prodDomains);
        }
        
        return origins;
    }

    /**
     * Get allowed origin patterns for more flexible matching
     * This allows for dynamic Vercel subdomains and other patterns
     */
    private List<String> getAllowedOriginPatterns() {
        List<String> patterns = Arrays.asList(
            // Localhost patterns for development
            "http://localhost:*",
            "http://127.0.0.1:*",
            "https://localhost:*",
            "https://127.0.0.1:*",
            
            // Specific production Vercel URL
            "https://notedoc-alpha.vercel.app",
            
            // General Vercel patterns
            "https://*.vercel.app",
            "https://*.vercel.com",
            
            // Custom domain patterns (if you have custom domains)
            "https://*.notedoc.app",
            "https://notedoc.app"
        );

        // Add production domains if specified
        if (!productionDomains.isEmpty()) {
            List<String> prodDomains = Arrays.asList(productionDomains.split(","));
            patterns.addAll(prodDomains);
        }

        log.info("CORS origin patterns configured: {}", patterns);
        return patterns;
    }

    /**
     * Validate if an origin matches allowed patterns
     * This method can be used for custom origin validation if needed
     */
    public boolean isOriginAllowed(String origin) {
        if (origin == null) {
            return false;
        }

        // Check exact matches first
        List<String> exactOrigins = getAllowedOriginsList();
        if (exactOrigins.contains(origin)) {
            return true;
        }

        // Check specific production URL
        if ("https://notedoc-alpha.vercel.app".equals(origin)) {
            return true;
        }

        // Check pattern matches
        Pattern vercelPattern = Pattern.compile("https://.*\\.vercel\\.app");
        if (vercelPattern.matcher(origin).matches()) {
            return true;
        }

        // Check localhost patterns
        Pattern localhostPattern = Pattern.compile("https?://localhost:\\d+");
        if (localhostPattern.matcher(origin).matches()) {
            return true;
        }

        Pattern localhostIpPattern = Pattern.compile("https?://127\\.0\\.0\\.1:\\d+");
        return localhostIpPattern.matcher(origin).matches();
    }
}