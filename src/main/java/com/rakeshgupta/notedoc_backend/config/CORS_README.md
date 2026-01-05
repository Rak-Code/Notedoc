# CORS Configuration Guide

This document explains the Cross-Origin Resource Sharing (CORS) configuration for the NoteDoc backend API.

## Overview

CORS is configured to allow frontend applications to make requests to the backend API from different domains. The configuration supports both development and production environments.

## Supported Origins

### Development (Localhost)
- `http://localhost:3000` - React (Create React App, Next.js)
- `http://localhost:5173` - Vite (Vue, React, Svelte)
- `http://localhost:4200` - Angular CLI
- `http://localhost:8080` - Backend API (for testing)
- `http://127.0.0.1:*` - Alternative localhost format

### Production (Vercel)
- `https://*.vercel.app` - All Vercel deployment subdomains
- `https://*.vercel.com` - Alternative Vercel domains
- Custom domains (configurable via environment variables)

## Configuration Properties

### Environment Variables

Set these environment variables for production deployment:

```bash
# Specific production domains (comma-separated)
CORS_PRODUCTION_DOMAINS=https://your-app.vercel.app,https://notedoc.yourdomain.com

# Override default allowed origins completely
CORS_ALLOWED_ORIGINS=http://localhost:3000,https://your-production-domain.com
```

### Application Properties

```properties
# Development origins
cors.allowed-origins=http://localhost:3000,http://localhost:5173,http://localhost:4200

# Production domains (set via environment variable)
cors.production-domains=${CORS_PRODUCTION_DOMAINS:}

# Allowed HTTP methods
cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS,PATCH,HEAD

# Allowed headers (* means all headers)
cors.allowed-headers=*

# Allow credentials (cookies, auth headers)
cors.allow-credentials=true

# Preflight cache duration (1 hour)
cors.max-age=3600
```

## Frontend Integration Examples

### React/Next.js (localhost:3000)

```javascript
// API calls from React app
const response = await fetch('http://localhost:8080/api/notes', {
  method: 'GET',
  credentials: 'include', // Include cookies if needed
  headers: {
    'Content-Type': 'application/json',
  }
});
```

### Vue/Vite (localhost:5173)

```javascript
// API calls from Vue app
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  withCredentials: true, // Include cookies if needed
});

const notes = await api.get('/notes');
```

### Angular (localhost:4200)

```typescript
// API calls from Angular app
import { HttpClient } from '@angular/common/http';

constructor(private http: HttpClient) {}

getNotes() {
  return this.http.get('http://localhost:8080/api/notes', {
    withCredentials: true // Include cookies if needed
  });
}
```

## Production Deployment

### Vercel Frontend + Backend

1. **Deploy your frontend to Vercel**
2. **Note your Vercel URL** (e.g., `https://my-notedoc-app.vercel.app`)
3. **Set environment variable** on your backend hosting platform:

```bash
CORS_PRODUCTION_DOMAINS=https://my-notedoc-app.vercel.app
```

### Multiple Domains

For multiple production domains:

```bash
CORS_PRODUCTION_DOMAINS=https://app1.vercel.app,https://app2.vercel.app,https://custom-domain.com
```

### Custom Domain

If using a custom domain with Vercel:

```bash
CORS_PRODUCTION_DOMAINS=https://notedoc.yourdomain.com,https://app.yourdomain.com
```

## Security Considerations

### Allowed Origins
- **Development**: Localhost origins are allowed for development convenience
- **Production**: Only specific domains should be allowed
- **Wildcards**: Use pattern matching for Vercel subdomains (`*.vercel.app`)

### Credentials
- `allowCredentials: true` allows cookies and authorization headers
- Only enable if your frontend needs to send authentication data
- Be cautious with wildcard origins when credentials are enabled

### Headers
- `allowedHeaders: *` allows all headers for flexibility
- In production, consider restricting to specific headers for better security

## Testing CORS

### Browser Developer Tools

1. Open browser developer tools (F12)
2. Go to Network tab
3. Make a request from your frontend
4. Check for CORS errors in console
5. Verify preflight OPTIONS requests

### CORS Preflight Request

For complex requests, browsers send a preflight OPTIONS request:

```http
OPTIONS /api/notes HTTP/1.1
Origin: https://my-app.vercel.app
Access-Control-Request-Method: POST
Access-Control-Request-Headers: content-type
```

Backend response:

```http
HTTP/1.1 200 OK
Access-Control-Allow-Origin: https://my-app.vercel.app
Access-Control-Allow-Methods: GET,POST,PUT,DELETE,OPTIONS,PATCH,HEAD
Access-Control-Allow-Headers: *
Access-Control-Allow-Credentials: true
Access-Control-Max-Age: 3600
```

### Manual Testing with curl

```bash
# Test preflight request
curl -X OPTIONS http://localhost:8080/api/notes \
  -H "Origin: http://localhost:3000" \
  -H "Access-Control-Request-Method: POST" \
  -H "Access-Control-Request-Headers: content-type" \
  -v

# Test actual request
curl -X GET http://localhost:8080/api/notes \
  -H "Origin: http://localhost:3000" \
  -v
```

## Common Issues and Solutions

### Issue: CORS Error in Browser

**Error**: `Access to fetch at 'http://localhost:8080/api/notes' from origin 'http://localhost:3000' has been blocked by CORS policy`

**Solutions**:
1. Verify the frontend origin is in `cors.allowed-origins`
2. Check if the backend is running on the correct port
3. Ensure CORS configuration is loaded properly

### Issue: Credentials Not Working

**Error**: Cookies or authorization headers not being sent

**Solutions**:
1. Set `withCredentials: true` in frontend requests
2. Ensure `cors.allow-credentials=true` in backend
3. Don't use wildcard origins with credentials

### Issue: Vercel Deployment CORS Error

**Error**: CORS works locally but fails on Vercel

**Solutions**:
1. Set `CORS_PRODUCTION_DOMAINS` environment variable
2. Use the exact Vercel URL (including https://)
3. Check for trailing slashes in URLs

### Issue: Custom Headers Blocked

**Error**: Custom headers are being blocked

**Solutions**:
1. Add specific headers to `cors.allowed-headers`
2. Or keep `cors.allowed-headers=*` for development
3. Ensure headers are properly formatted

## Environment-Specific Configuration

### Development (.env.local)

```properties
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:5173
```

### Staging

```properties
CORS_PRODUCTION_DOMAINS=https://staging-app.vercel.app
```

### Production

```properties
CORS_PRODUCTION_DOMAINS=https://notedoc-app.vercel.app,https://notedoc.com
```

This CORS configuration provides a secure and flexible setup for both development and production environments while supporting the dynamic nature of Vercel deployments.