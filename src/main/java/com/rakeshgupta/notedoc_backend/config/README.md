# Environment Configuration

This package contains configuration classes for handling environment variables from the `.env` file using Spring Boot's native support.

## Components

### DotEnvConfig
- Validates that environment variables are loaded correctly at startup
- Logs configuration status for debugging
- Uses Spring Boot's native `.env` loading via `spring.config.import`

### DatabaseProperties
- Type-safe configuration properties for database connection
- Maps environment variables with `DB_` prefix to Java properties
- Provides utility method to generate JDBC URL

### ConfigurationService
- Demonstrates usage of configuration properties
- Provides logging and programmatic access to database config

## Setup

### 1. Environment Variables
Place your environment variables in `.env` file at project root:
```
DB_HOST=your-host
DB_PORT=5432
DB_NAME=your-database
DB_USERNAME=your-username
DB_PASSWORD=your-password
```

### 2. Spring Boot Configuration
Add this line to `application.properties`:
```properties
spring.config.import=optional:file:.env
```

### 3. Usage in Properties
Variables are automatically available in `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

### 4. Programmatic Access
Inject `DatabaseProperties` or use `@Value`:
```java
@Autowired
private DatabaseProperties dbProperties;

@Value("${DB_HOST}")
private String dbHost;
```

## Troubleshooting

### Windows PowerShell Issues
If variables aren't loading, you can manually set them:
```powershell
$env:DB_HOST="your-host"
$env:DB_PORT="5432"
# ... etc
mvn spring-boot:run
```

### Validation
Check the startup logs for:
- ✅ Environment variables loaded successfully
- ❌ Environment variables not loaded (check .env file location)

## Key Features

- **Native Spring Boot Support**: Uses `spring.config.import` (no external dependencies)
- **Graceful Fallback**: `optional:file:.env` won't fail if file is missing
- **Validation**: Startup logging shows if variables are loaded correctly
- **Type Safety**: `DatabaseProperties` provides compile-time safety