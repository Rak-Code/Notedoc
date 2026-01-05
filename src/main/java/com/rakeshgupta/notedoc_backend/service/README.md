# Services Documentation

## KeepAliveService

The `KeepAliveService` is designed to prevent the application from going to sleep on free hosting platforms like Render, Heroku, or Railway.

### How it works

- **Scheduled Task**: Runs every 5 minutes (300,000 milliseconds)
- **Health Check**: Makes an HTTP GET request to the application's health endpoint
- **Self-Ping**: Keeps the application active by generating internal traffic

### Configuration

The service uses the following configuration properties:

```properties
# Application URL (set via environment variable for production)
app.url=${APP_URL:http://localhost:8080}

# Health check endpoint path
app.health-check.endpoint=/api/health
```

### Environment Variables

For production deployment, set the `APP_URL` environment variable:

```bash
# For Render deployment
APP_URL=https://your-app-name.onrender.com

# For Heroku deployment  
APP_URL=https://your-app-name.herokuapp.com

# For Railway deployment
APP_URL=https://your-app-name.up.railway.app
```

### Benefits

1. **Prevents Cold Starts**: Keeps the application warm and responsive
2. **Improved User Experience**: Reduces initial load times for users
3. **Cost Effective**: Works with free hosting tiers that have sleep policies
4. **Automatic**: No manual intervention required once configured

### Monitoring

The service logs its activity:

- **Success**: `Keep-alive health check executed successfully: {response}`
- **Failure**: `Keep-alive health check failed: {error message}`

### Health Check Endpoint

The service pings `/api/health` which returns:

```json
{
  "status": "UP",
  "timestamp": "2024-01-15T10:30:00",
  "service": "notedoc-backend", 
  "version": "1.0.0"
}
```

### Disabling the Service

To disable the keep-alive service in development or if not needed:

1. Remove the `@EnableScheduling` annotation from the main application class
2. Or exclude the service from component scanning
3. Or set a profile-specific configuration to disable scheduling

### Notes

- The service only runs when the application is active
- It uses minimal resources (single HTTP request every 5 minutes)
- The health check endpoint is lightweight and doesn't perform heavy operations
- Compatible with all major cloud hosting platforms