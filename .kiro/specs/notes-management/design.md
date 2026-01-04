# Design Document: Notes Management System

## Overview

The SmartNotes backend is a production-ready REST API built with Spring Boot 3.x and Java 21, designed using clean layered architecture principles. The system provides comprehensive note management capabilities while maintaining future compatibility for authentication integration. The design emphasizes separation of concerns, testability, and scalability.

## Architecture

### Layered Architecture Pattern

The system follows a strict 4-layer architecture:

```
┌─────────────────┐
│   Controller    │ ← REST endpoints, request/response handling
├─────────────────┤
│    Service      │ ← Business logic, transaction management
├─────────────────┤
│   Repository    │ ← Data access, query operations
├─────────────────┤
│    Database     │ ← PostgreSQL persistence layer
└─────────────────┘
```

**Layer Responsibilities:**
- **Controller Layer**: HTTP request handling, input validation, response formatting
- **Service Layer**: Business logic implementation, transaction boundaries, data transformation
- **Repository Layer**: Database operations, query construction, entity mapping
- **Database Layer**: Data persistence, constraints, indexing

### Package Structure

```
com.rakeshgupta.notedoc_backend/
├── controller/          # REST controllers
├── service/            # Business logic services
├── repository/         # Data access repositories
├── dto/               # Data Transfer Objects
│   ├── request/       # Request DTOs
│   └── response/      # Response DTOs
├── entity/            # JPA entities
├── exception/         # Custom exceptions and handlers
└── config/           # Configuration classes
```

## Components and Interfaces

### Entity Design

**Note Entity:**
```java
@Entity
@Table(name = "notes")
public class Note {
    @Id
    private UUID id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @ElementCollection
    @CollectionTable(name = "note_tags")
    private Set<String> tags;
    
    private Boolean pinned = false;
    private Boolean archived = false;
    private Boolean deleted = false;
    
    @Column(name = "user_id", nullable = false)
    private UUID userId;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
```

### Repository Interface

**NoteRepository:**
```java
@Repository
public interface NoteRepository extends JpaRepository<Note, UUID> {
    
    @Query("SELECT n FROM Note n WHERE n.deleted = false AND n.userId = :userId")
    Page<Note> findActiveNotesByUserId(UUID userId, Pageable pageable);
    
    @Query("SELECT n FROM Note n WHERE n.deleted = false AND n.userId = :userId AND n.id = :id")
    Optional<Note> findActiveNoteByIdAndUserId(UUID id, UUID userId);
    
    @Query("SELECT n FROM Note n WHERE n.deleted = false AND n.userId = :userId AND " +
           "(LOWER(n.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(n.content) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Note> searchActiveNotes(String query, UUID userId, Pageable pageable);
}
```

### Service Interface

**NoteService:**
```java
@Service
public interface NoteService {
    NoteResponseDto createNote(NoteCreateRequestDto request, UUID userId);
    Page<NoteResponseDto> getAllNotes(UUID userId, Pageable pageable);
    NoteResponseDto getNoteById(UUID id, UUID userId);
    NoteResponseDto updateNote(UUID id, NoteUpdateRequestDto request, UUID userId);
    void deleteNote(UUID id, UUID userId);
    Page<NoteResponseDto> searchNotes(String query, UUID userId, Pageable pageable);
}
```

### Controller Design

**NoteController:**
```java
@RestController
@RequestMapping("/api/notes")
public class NoteController {
    
    private static final UUID HARDCODED_USER_ID = 
        UUID.fromString("11111111-1111-1111-1111-111111111111");
    
    @PostMapping
    public ResponseEntity<NoteResponseDto> createNote(@Valid @RequestBody NoteCreateRequestDto request);
    
    @GetMapping
    public ResponseEntity<Page<NoteResponseDto>> getAllNotes(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "updatedAt,desc") String sort);
    
    @GetMapping("/{id}")
    public ResponseEntity<NoteResponseDto> getNoteById(@PathVariable UUID id);
    
    @PutMapping("/{id}")
    public ResponseEntity<NoteResponseDto> updateNote(
        @PathVariable UUID id, 
        @Valid @RequestBody NoteUpdateRequestDto request);
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable UUID id);
    
    @GetMapping("/search")
    public ResponseEntity<Page<NoteResponseDto>> searchNotes(
        @RequestParam String q,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size);
}
```

## Data Models

### Request DTOs

**NoteCreateRequestDto:**
```java
public class NoteCreateRequestDto {
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;
    
    private String content;
    
    private Set<String> tags = new HashSet<>();
    
    private Boolean pinned = false;
    
    private Boolean archived = false;
}
```

**NoteUpdateRequestDto:**
```java
public class NoteUpdateRequestDto {
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;
    
    private String content;
    
    private Set<String> tags;
    
    private Boolean pinned;
    
    private Boolean archived;
}
```

### Response DTOs

**NoteResponseDto:**
```java
public class NoteResponseDto {
    private UUID id;
    private String title;
    private String content;
    private Set<String> tags;
    private Boolean pinned;
    private Boolean archived;
    private UUID userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

### Database Schema

**Notes Table:**
```sql
CREATE TABLE notes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    content TEXT,
    pinned BOOLEAN DEFAULT FALSE,
    archived BOOLEAN DEFAULT FALSE,
    deleted BOOLEAN DEFAULT FALSE,
    user_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE note_tags (
    note_id UUID REFERENCES notes(id),
    tags VARCHAR(255),
    PRIMARY KEY (note_id, tags)
);

-- Performance indexes
CREATE INDEX idx_notes_user_id ON notes(user_id);
CREATE INDEX idx_notes_updated_at ON notes(updated_at DESC);
CREATE INDEX idx_notes_deleted ON notes(deleted);
CREATE INDEX idx_notes_user_deleted ON notes(user_id, deleted);
```

## Correctness Properties

*A property is a characteristic or behavior that should hold true across all valid executions of a system—essentially, a formal statement about what the system should do. Properties serve as the bridge between human-readable specifications and machine-verifiable correctness guarantees.*

<function_calls>
<invoke name="prework">
<parameter name="featureName">notes-management

After reviewing the prework analysis, I'll consolidate redundant properties and focus on the most valuable correctness properties:

**Property Reflection:**
- Properties 2.4 and 4.4 are identical (soft-delete filtering) - consolidating into one
- Properties 2.6, 3.5, and 4.5 all test error handling for non-existent notes - consolidating into one comprehensive property
- Properties 1.5, 1.6, and 1.7 can be combined into a single note creation defaults property
- Properties 3.2 and 3.3 can be combined into a single timestamp behavior property
- Properties 5.3 and 5.4 are covered by existing filtering and pagination properties

### Core Correctness Properties

**Property 1: Note Creation with Valid Data**
*For any* valid note creation request with non-empty title, the system should create a note with a unique UUID, proper defaults (pinned=false, archived=false, deleted=false), hardcoded userId, and both createdAt and updatedAt timestamps set
**Validates: Requirements 1.1, 1.5, 1.6, 1.7**

**Property 2: Title Validation Enforcement**
*For any* note creation or update request with invalid title (null, empty, or whitespace-only), the system should reject the request with appropriate validation error
**Validates: Requirements 1.2, 3.4**

**Property 3: Content and Tags Acceptance**
*For any* note creation request with valid title, the system should accept optional content (including null, empty, or large text) and optional tags (including empty sets or large collections)
**Validates: Requirements 1.3, 1.4**

**Property 4: Pagination Consistency**
*For any* valid page size and page number parameters, the system should return paginated results with correct page metadata and default sorting by updatedAt descending
**Validates: Requirements 2.1, 2.2, 2.3**

**Property 5: Soft Delete Filtering**
*For any* note retrieval operation (listing, searching, individual lookup), the system should exclude all notes where deleted=true from the results
**Validates: Requirements 2.4, 4.4, 5.3**

**Property 6: Complete Note Retrieval**
*For any* existing non-deleted note ID, the system should return all note fields (id, title, content, tags, pinned, archived, userId, createdAt, updatedAt) in the response
**Validates: Requirements 2.5**

**Property 7: Error Handling for Invalid Operations**
*For any* operation (get, update, delete) targeting a non-existent or soft-deleted note ID, the system should return appropriate HTTP error status (404) with descriptive error message
**Validates: Requirements 2.6, 3.5, 4.5**

**Property 8: Field Update Capability**
*For any* valid note update request, the system should allow modification of title, content, tags, pinned, and archived fields while preserving id, userId, createdAt, and deleted fields
**Validates: Requirements 3.1**

**Property 9: Timestamp Behavior on Updates**
*For any* note update operation, the system should update the updatedAt timestamp to current time while preserving the original createdAt timestamp
**Validates: Requirements 3.2, 3.3**

**Property 10: Soft Delete Implementation**
*For any* note deletion request, the system should set deleted=true, update updatedAt timestamp, and preserve all other note data without physical record removal
**Validates: Requirements 4.1, 4.2, 4.3**

**Property 11: Search Field Coverage**
*For any* search query containing text that exists in either title or content fields of non-deleted notes, the search should return those notes regardless of case sensitivity
**Validates: Requirements 5.1, 5.2**

**Property 12: Empty Search Handling**
*For any* empty or null search query, the system should return all non-deleted notes with proper pagination
**Validates: Requirements 5.5**

**Property 13: UUID Primary Key Consistency**
*For any* note creation, the system should assign a valid UUID as primary key that is unique across all notes
**Validates: Requirements 6.1**

**Property 14: Tag Relationship Storage**
*For any* note with tags, the system should store tag relationships in separate collection table while maintaining proper foreign key relationships
**Validates: Requirements 6.2**

**Property 15: Large Content Support**
*For any* note with content exceeding standard VARCHAR limits (>4000 characters), the system should store and retrieve the content without truncation
**Validates: Requirements 6.4**

**Property 16: Database Title Constraint**
*For any* attempt to persist a note with null title at the database level, the system should enforce NOT NULL constraint and reject the operation
**Validates: Requirements 6.5**

**Property 17: HTTP Status Code Correctness**
*For any* API operation, the system should return appropriate HTTP status codes: 201 for creation, 200 for successful retrieval/update, 404 for not found, 400 for validation errors
**Validates: Requirements 7.2**

**Property 18: JSON Response Consistency**
*For any* API response, the system should return well-formed JSON with consistent field naming and structure across all endpoints
**Validates: Requirements 7.4**

**Property 19: Validation Error Detail**
*For any* request that fails validation, the system should return detailed error messages indicating which fields failed validation and why
**Validates: Requirements 7.5**

**Property 20: Hardcoded User ID Consistency**
*For any* note operation, the system should use the same hardcoded UUID (11111111-1111-1111-1111-111111111111) for userId field
**Validates: Requirements 8.4**

**Property 21: Global Exception Handling**
*For any* unhandled exception during API operations, the system should return consistent error response format without exposing internal implementation details
**Validates: Requirements 7.6, 8.6**

## Error Handling

### Exception Hierarchy

```java
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoteNotFoundException extends RuntimeException {
    public NoteNotFoundException(UUID id) {
        super("Note not found with id: " + id);
    }
}

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidNoteDataException extends RuntimeException {
    public InvalidNoteDataException(String message) {
        super(message);
    }
}
```

### Global Exception Handler

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(NoteNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoteNotFound(NoteNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage()));
        
        ErrorResponse errorResponse = new ErrorResponse("VALIDATION_ERROR", "Invalid input data", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse("INTERNAL_ERROR", "An unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
```

### Error Response Format

```java
public class ErrorResponse {
    private String code;
    private String message;
    private Map<String, String> details;
    private LocalDateTime timestamp;
}
```

## Testing Strategy

### Dual Testing Approach

The system will use both unit testing and property-based testing to ensure comprehensive coverage:

**Unit Tests:**
- Specific examples demonstrating correct behavior
- Edge cases and boundary conditions
- Integration points between layers
- Error condition handling

**Property-Based Tests:**
- Universal properties that hold for all inputs
- Comprehensive input coverage through randomization
- Minimum 100 iterations per property test
- Each test tagged with corresponding design property

### Property-Based Testing Configuration

**Framework:** We will use **jqwik** for property-based testing in Java, which integrates well with JUnit 5.

**Test Configuration:**
- Minimum 100 iterations per property test
- Custom generators for Note entities, DTOs, and UUIDs
- Shrinking enabled for minimal counterexamples
- Each property test tagged with format: **Feature: notes-management, Property {number}: {property_text}**

**Example Property Test Structure:**
```java
@Property
@Label("Feature: notes-management, Property 1: Note Creation with Valid Data")
void noteCreationWithValidData(@ForAll @ValidNoteRequest NoteCreateRequestDto request) {
    // Test implementation
}
```

### Testing Layers

**Controller Tests:**
- HTTP request/response handling
- Input validation
- Status code verification
- JSON serialization/deserialization

**Service Tests:**
- Business logic validation
- Transaction behavior
- Data transformation
- Exception handling

**Repository Tests:**
- Database operations
- Query correctness
- Constraint enforcement
- Index utilization

**Integration Tests:**
- End-to-end API flows
- Database transaction rollback
- Error propagation
- Performance characteristics

### Test Data Management

**Test Database:**
- H2 in-memory database for unit tests
- TestContainers with PostgreSQL for integration tests
- Database migrations applied automatically
- Test data cleanup between tests

**Data Generators:**
- Custom generators for valid/invalid note data
- UUID generators for consistent test data
- Large content generators for performance testing
- Edge case generators for boundary testing