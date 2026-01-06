# Frontend Integration Guide - NoteDoc API

This guide provides comprehensive documentation for integrating with the NoteDoc backend API, including all endpoints, request/response formats, and recommended frontend implementation patterns.

## Base Configuration

### API Base URL
```
# Development
http://localhost:8080/api (or http://localhost:8080 for direct endpoints)

# Production (replace with your actual backend URL)
https://notedoc.onrender.com/api (or https://notedoc.onrender.com for direct endpoints)
```

**Note**: The backend now supports both `/api/notes` and `/notes` endpoints for compatibility.

### Content Type
All requests should use `Content-Type: application/json`

### CORS Configuration
The backend is configured to accept requests from:
- **Development**: `localhost:3000`, `localhost:5173`, `localhost:4200`
- **Production**: `https://notedoc-alpha.vercel.app` and other `*.vercel.app` domains

### Authentication
Currently using hardcoded user ID: `11111111-1111-1111-1111-111111111111`
(Authentication will be implemented in future versions)

## API Endpoints Overview

| Method | Endpoint | Alternative | Description |
|--------|----------|-------------|-------------|
| POST | `/api/notes` | `/notes` | Create a new note |
| GET | `/api/notes` | `/notes` | Get all notes (paginated) |
| GET | `/api/notes/{id}` | `/notes/{id}` | Get specific note by ID |
| PUT | `/api/notes/{id}` | `/notes/{id}` | Update existing note |
| DELETE | `/api/notes/{id}` | `/notes/{id}` | Delete note (soft delete) |
| GET | `/api/notes/search` | `/notes/search` | Search notes by query |
| GET | `/api/health` | `/health` | Health check endpoint |

## Data Models

### Note Response Object
```typescript
interface NoteResponse {
  id: string;                    // UUID
  title: string;                 // Max 255 characters
  content?: string;              // Markdown format
  tags: string[];               // Array of tag strings
  pinned: boolean;              // Default: false
  archived: boolean;            // Default: false
  userId: string;               // UUID
  createdAt: string;            // ISO 8601 datetime
  updatedAt: string;            // ISO 8601 datetime
}
```

### Create Note Request
```typescript
interface CreateNoteRequest {
  title: string;                 // Required, max 255 chars
  content?: string;              // Optional, markdown
  tags?: string[];              // Optional, defaults to []
  pinned?: boolean;             // Optional, defaults to false
  archived?: boolean;           // Optional, defaults to false
}
```

### Update Note Request
```typescript
interface UpdateNoteRequest {
  title?: string;               // Optional, max 255 chars
  content?: string;             // Optional, markdown
  tags?: string[];             // Optional
  pinned?: boolean;            // Optional
  archived?: boolean;          // Optional
}
```

### Paginated Response
```typescript
interface PagedResponse<T> {
  content: T[];                 // Array of items
  pageable: {
    sort: {
      empty: boolean;
      sorted: boolean;
      unsorted: boolean;
    };
    offset: number;
    pageSize: number;
    pageNumber: number;
    paged: boolean;
    unpaged: boolean;
  };
  last: boolean;
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  sort: {
    empty: boolean;
    sorted: boolean;
    unsorted: boolean;
  };
  first: boolean;
  numberOfElements: number;
  empty: boolean;
}
```

### Error Response
```typescript
interface ErrorResponse {
  code: string;
  message: string;
  details?: Record<string, string>;
  timestamp: string;            // ISO 8601 datetime
}
```

## API Implementation Examples

### 1. Create Note
```typescript
async function createNote(noteData: CreateNoteRequest): Promise<NoteResponse> {
  const response = await fetch('http://localhost:8080/api/notes', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(noteData)
  });

  if (!response.ok) {
    const error: ErrorResponse = await response.json();
    throw new Error(`Failed to create note: ${error.message}`);
  }

  return response.json();
}

// Usage example
const newNote = await createNote({
  title: "Meeting Notes",
  content: "## Agenda\n- Discuss project timeline\n- Review budget",
  tags: ["work", "meeting", "project"],
  pinned: false
});
```

### 2. Get All Notes (with Pagination)
```typescript
interface GetNotesParams {
  page?: number;        // Default: 0
  size?: number;        // Default: 10
  sort?: string;        // Default: "updatedAt,desc"
}

async function getAllNotes(params: GetNotesParams = {}): Promise<PagedResponse<NoteResponse>> {
  const searchParams = new URLSearchParams({
    page: (params.page || 0).toString(),
    size: (params.size || 10).toString(),
    sort: params.sort || 'updatedAt,desc'
  });

  const response = await fetch(`http://localhost:8080/api/notes?${searchParams}`);
  
  if (!response.ok) {
    const error: ErrorResponse = await response.json();
    throw new Error(`Failed to fetch notes: ${error.message}`);
  }

  return response.json();
}

// Usage examples
const firstPage = await getAllNotes();
const secondPage = await getAllNotes({ page: 1, size: 20 });
const sortedByTitle = await getAllNotes({ sort: 'title,asc' });
```

### 3. Get Note by ID
```typescript
async function getNoteById(id: string): Promise<NoteResponse> {
  const response = await fetch(`http://localhost:8080/api/notes/${id}`);
  
  if (!response.ok) {
    if (response.status === 404) {
      throw new Error('Note not found');
    }
    const error: ErrorResponse = await response.json();
    throw new Error(`Failed to fetch note: ${error.message}`);
  }

  return response.json();
}
```

### 4. Update Note
```typescript
async function updateNote(id: string, updates: UpdateNoteRequest): Promise<NoteResponse> {
  const response = await fetch(`http://localhost:8080/api/notes/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(updates)
  });

  if (!response.ok) {
    const error: ErrorResponse = await response.json();
    throw new Error(`Failed to update note: ${error.message}`);
  }

  return response.json();
}

// Usage example
const updatedNote = await updateNote('123e4567-e89b-12d3-a456-426614174000', {
  title: "Updated Meeting Notes",
  pinned: true
});
```

### 5. Delete Note
```typescript
async function deleteNote(id: string): Promise<void> {
  const response = await fetch(`http://localhost:8080/api/notes/${id}`, {
    method: 'DELETE'
  });

  if (!response.ok) {
    if (response.status === 404) {
      throw new Error('Note not found');
    }
    const error: ErrorResponse = await response.json();
    throw new Error(`Failed to delete note: ${error.message}`);
  }
}
```

### 6. Search Notes
```typescript
interface SearchNotesParams {
  q: string;           // Required search query
  page?: number;       // Default: 0
  size?: number;       // Default: 10
}

async function searchNotes(params: SearchNotesParams): Promise<PagedResponse<NoteResponse>> {
  const searchParams = new URLSearchParams({
    q: params.q,
    page: (params.page || 0).toString(),
    size: (params.size || 10).toString()
  });

  const response = await fetch(`http://localhost:8080/api/notes/search?${searchParams}`);
  
  if (!response.ok) {
    const error: ErrorResponse = await response.json();
    throw new Error(`Search failed: ${error.message}`);
  }

  return response.json();
}

// Usage example
const searchResults = await searchNotes({ 
  q: "meeting notes", 
  page: 0, 
  size: 20 
});
```

### 7. Health Check
```typescript
interface HealthResponse {
  status: string;
  timestamp: string;
  service: string;
  version: string;
}

async function healthCheck(): Promise<HealthResponse> {
  const response = await fetch('http://localhost:8080/api/health');
  
  if (!response.ok) {
    throw new Error('Health check failed');
  }

  return response.json();
}

// Usage example
const health = await healthCheck();
console.log('Service status:', health.status); // "UP"
```

## Frontend Implementation Recommendations

### 1. API Service Class
```typescript
class NoteService {
  private baseUrl: string;

  constructor() {
    // Automatically detect environment and use direct endpoints (without /api prefix)
    this.baseUrl = process.env.NODE_ENV === 'production' 
      ? 'https://notedoc.onrender.com/notes'  // Direct endpoint for production
      : 'http://localhost:8080/notes';        // Direct endpoint for development
  }

  // Default fetch options with CORS support
  private getDefaultOptions(): RequestInit {
    return {
      credentials: 'include', // Include cookies for CORS
      headers: {
        'Content-Type': 'application/json',
      },
    };
  }

  async createNote(noteData: CreateNoteRequest): Promise<NoteResponse> {
    const response = await fetch(this.baseUrl, {
      ...this.getDefaultOptions(),
      method: 'POST',
      body: JSON.stringify(noteData)
    });

    if (!response.ok) {
      const error: ErrorResponse = await response.json();
      throw new Error(`Failed to create note: ${error.message}`);
    }

    return response.json();
  }

  async getAllNotes(params: GetNotesParams = {}): Promise<PagedResponse<NoteResponse>> {
    const searchParams = new URLSearchParams({
      page: (params.page || 0).toString(),
      size: (params.size || 10).toString(),
      sort: params.sort || 'updatedAt,desc'
    });

    const response = await fetch(`${this.baseUrl}?${searchParams}`, {
      ...this.getDefaultOptions(),
      method: 'GET'
    });
    
    if (!response.ok) {
      const error: ErrorResponse = await response.json();
      throw new Error(`Failed to fetch notes: ${error.message}`);
    }

    return response.json();
  }

  async getNoteById(id: string): Promise<NoteResponse> {
    const response = await fetch(`${this.baseUrl}/${id}`, {
      ...this.getDefaultOptions(),
      method: 'GET'
    });
    
    if (!response.ok) {
      if (response.status === 404) {
        throw new Error('Note not found');
      }
      const error: ErrorResponse = await response.json();
      throw new Error(`Failed to fetch note: ${error.message}`);
    }

    return response.json();
  }

  async updateNote(id: string, updates: UpdateNoteRequest): Promise<NoteResponse> {
    const response = await fetch(`${this.baseUrl}/${id}`, {
      ...this.getDefaultOptions(),
      method: 'PUT',
      body: JSON.stringify(updates)
    });

    if (!response.ok) {
      const error: ErrorResponse = await response.json();
      throw new Error(`Failed to update note: ${error.message}`);
    }

    return response.json();
  }

  async deleteNote(id: string): Promise<void> {
    const response = await fetch(`${this.baseUrl}/${id}`, {
      ...this.getDefaultOptions(),
      method: 'DELETE'
    });

    if (!response.ok) {
      if (response.status === 404) {
        throw new Error('Note not found');
      }
      const error: ErrorResponse = await response.json();
      throw new Error(`Failed to delete note: ${error.message}`);
    }
  }

  async searchNotes(params: SearchNotesParams): Promise<PagedResponse<NoteResponse>> {
    const searchParams = new URLSearchParams({
      q: params.q,
      page: (params.page || 0).toString(),
      size: (params.size || 10).toString()
    });

    const response = await fetch(`${this.baseUrl}/search?${searchParams}`, {
      ...this.getDefaultOptions(),
      method: 'GET'
    });
    
    if (!response.ok) {
      const error: ErrorResponse = await response.json();
      throw new Error(`Search failed: ${error.message}`);
    }

    return response.json();
  }

  async healthCheck(): Promise<HealthResponse> {
    const healthUrl = this.baseUrl.replace('/notes', '/health');
    const response = await fetch(healthUrl, {
      ...this.getDefaultOptions(),
      method: 'GET'
    });
    
    if (!response.ok) {
      throw new Error('Health check failed');
    }

    return response.json();
  }
}

export const noteService = new NoteService();
```

### 2. Error Handling
```typescript
class ApiError extends Error {
  constructor(
    message: string,
    public status: number,
    public code?: string,
    public details?: Record<string, string>
  ) {
    super(message);
    this.name = 'ApiError';
  }
}

async function handleApiResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    const errorData: ErrorResponse = await response.json().catch(() => ({
      code: 'UNKNOWN_ERROR',
      message: 'An unknown error occurred',
      timestamp: new Date().toISOString()
    }));
    
    throw new ApiError(
      errorData.message,
      response.status,
      errorData.code,
      errorData.details
    );
  }
  
  return response.json();
}
```

### 3. React Hook Examples
```typescript
// Custom hook for notes
import { useState, useEffect } from 'react';

export function useNotes(params: GetNotesParams = {}) {
  const [notes, setNotes] = useState<PagedResponse<NoteResponse> | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchNotes() {
      try {
        setLoading(true);
        const data = await noteService.getAllNotes(params);
        setNotes(data);
        setError(null);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to fetch notes');
      } finally {
        setLoading(false);
      }
    }

    fetchNotes();
  }, [params.page, params.size, params.sort]);

  return { notes, loading, error, refetch: () => fetchNotes() };
}

// Custom hook for single note
export function useNote(id: string) {
  const [note, setNote] = useState<NoteResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchNote() {
      try {
        setLoading(true);
        const data = await noteService.getNoteById(id);
        setNote(data);
        setError(null);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to fetch note');
      } finally {
        setLoading(false);
      }
    }

    if (id) {
      fetchNote();
    }
  }, [id]);

  return { note, loading, error };
}
```

## Recommended Frontend Pages/Components

### 1. Notes List Page
- Display paginated list of notes
- Search functionality
- Filter by tags, pinned, archived status
- Sort options (title, created date, updated date)
- Quick actions (pin, archive, delete)

### 2. Note Detail/Edit Page
- View and edit note content
- Markdown preview
- Tag management
- Pin/archive toggles
- Save/cancel actions

### 3. Create Note Page
- Form for creating new notes
- Title and content fields
- Tag input
- Pin/archive options
- Save/cancel actions

### 4. Search Results Page
- Display search results with highlighting
- Pagination for search results
- Filter search results

## Validation Rules

### Frontend Validation
- **Title**: Required, max 255 characters
- **Content**: Optional, no length limit (but consider UX)
- **Tags**: Optional array of strings
- **Pinned/Archived**: Optional booleans, default to false

### Backend Validation Errors
The API returns validation errors in this format:
```json
{
  "code": "VALIDATION_ERROR",
  "message": "Validation failed",
  "details": {
    "title": "Title is required",
    "title.size": "Title must not exceed 255 characters"
  },
  "timestamp": "2024-01-15T10:30:00"
}
```

## Sorting Options

Available sort fields:
- `title` - Sort by note title
- `createdAt` - Sort by creation date
- `updatedAt` - Sort by last update date (default)

Sort directions:
- `asc` - Ascending order
- `desc` - Descending order (default)

Format: `{field},{direction}` (e.g., `title,asc`, `updatedAt,desc`)

## Pagination Best Practices

1. **Default Page Size**: Use 10-20 items per page for optimal UX
2. **Page Navigation**: Implement first, previous, next, last buttons
3. **Page Size Options**: Allow users to choose page size (10, 20, 50)
4. **Total Count**: Display total number of notes
5. **Loading States**: Show loading indicators during pagination

## Development Tips

1. **Environment Variables**: Use environment variables for API base URL
2. **Error Boundaries**: Implement React error boundaries for API errors
3. **Loading States**: Always show loading indicators for async operations
4. **Optimistic Updates**: Consider optimistic updates for better UX
5. **Caching**: Implement client-side caching for frequently accessed notes
6. **Debouncing**: Debounce search input to avoid excessive API calls

## CORS and Deployment

### Development Setup
The backend is configured to accept requests from common development ports:
- React (Create React App): `http://localhost:3000`
- Vite (Vue/React/Svelte): `http://localhost:5173`
- Angular CLI: `http://localhost:4200`

### Production Deployment

#### Frontend on Vercel
1. Deploy your frontend to Vercel at `https://notedoc-alpha.vercel.app`
2. The backend is already configured to accept requests from this URL
3. No additional CORS configuration needed

#### Custom Domain
If using a custom domain, set the `CORS_PRODUCTION_DOMAINS` environment variable on your backend:

```bash
CORS_PRODUCTION_DOMAINS=https://notedoc.yourdomain.com,https://app.yourdomain.com
```

#### Environment Variables for Frontend

Create environment files for your frontend:

**.env.local** (Development)
```bash
REACT_APP_API_URL=http://localhost:8080
# or for Vite
VITE_API_URL=http://localhost:8080
```

**.env.production** (Production)
```bash
REACT_APP_API_URL=https://notedoc.onrender.com
# or for Vite
VITE_API_URL=https://notedoc.onrender.com
```

**Note**: The production frontend URL `https://notedoc-alpha.vercel.app` is already hardcoded in the backend CORS configuration.

#### Updated Service Class with Environment Variables

```typescript
class NoteService {
  private baseUrl: string;

  constructor() {
    // React
    const apiUrl = process.env.REACT_APP_API_URL;
    // or Vite
    // const apiUrl = import.meta.env.VITE_API_URL;
    
    this.baseUrl = apiUrl || 'http://localhost:8080/api';
  }

  // ... rest of the methods
}
```

## Testing the API

### Using curl Examples
```bash
# Create a note
curl -X POST https://notedoc.onrender.com/notes \
  -H "Content-Type: application/json" \
  -H "Origin: https://notedoc-alpha.vercel.app" \
  -d '{
    "title": "Test Note",
    "content": "This is a test note",
    "tags": ["test"],
    "pinned": false
  }'

# Get all notes
curl https://notedoc.onrender.com/notes \
  -H "Origin: https://notedoc-alpha.vercel.app"

# Search notes
curl "https://notedoc.onrender.com/notes/search?q=test&page=0&size=10" \
  -H "Origin: https://notedoc-alpha.vercel.app"

# Health check
curl https://notedoc.onrender.com/health \
  -H "Origin: https://notedoc-alpha.vercel.app"
```

### Using Postman or Insomnia
Import the following endpoints into your API testing tool:
- `POST http://localhost:8080/api/notes` - Create note
- `GET http://localhost:8080/api/notes` - Get all notes
- `GET http://localhost:8080/api/notes/{id}` - Get note by ID
- `PUT http://localhost:8080/api/notes/{id}` - Update note
- `DELETE http://localhost:8080/api/notes/{id}` - Delete note
- `GET http://localhost:8080/api/notes/search?q={query}` - Search notes
- `GET http://localhost:8080/api/health` - Health check

This guide provides everything needed to integrate with the NoteDoc backend API. The API follows REST conventions and provides comprehensive error handling and validation.