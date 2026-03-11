# Database Keep-Alive System

## Overview
This system keeps your free-tier database connection alive by performing automated health checks every 30 minutes.

## Components

### 1. HealthCheck Entity
- Table: `health_checks`
- Fields: id, status, check_time, message
- Stores all database health check records

### 2. HealthCheckRepository
- JPA repository for HealthCheck entity
- Provides method to fetch recent 10 checks

### 3. HealthCheckController
- Endpoint: `GET /api/db-health`
- Performs manual database health check
- Returns status, timestamp, and recent checks

### 4. DatabaseKeepAliveService
- Scheduled task running every 30 minutes
- Automatically pings database to keep connection alive
- Logs all activities for monitoring

### 5. DataInitializer
- Runs on application startup
- Inserts 5 initial health check records if table is empty
- Ensures database has baseline data

## How It Works

1. **On Startup**: DataInitializer creates 5 initial records
2. **Every 30 Minutes**: DatabaseKeepAliveService automatically pings the database
3. **Manual Check**: Hit `/api/db-health` endpoint anytime to verify connection

## API Usage

```bash
# Check database health
curl http://localhost:8080/api/db-health
```

Response:
```json
{
  "status": "UP",
  "timestamp": "2026-03-12T10:30:00",
  "database": "CONNECTED",
  "totalChecks": 15,
  "recentChecks": [...]
}
```

## Benefits
- Prevents free-tier database from going idle
- Monitors database connectivity
- Provides health check history
- Zero manual intervention required
