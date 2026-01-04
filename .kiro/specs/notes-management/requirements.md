# Requirements Document

## Introduction

SmartNotes is a production-quality notes management backend service built with Spring Boot and PostgreSQL. The system provides comprehensive note management capabilities including creation, retrieval, updating, soft deletion, and search functionality. The architecture is designed to support future authentication integration without requiring database schema changes or major refactoring.

## Glossary

- **Note**: A user-created document containing title, content, tags, and metadata
- **SmartNotes_API**: The REST API service for note management
- **Note_Repository**: The data access layer for note persistence
- **Note_Service**: The business logic layer for note operations
- **Soft_Delete**: Marking records as deleted without physical removal from database
- **Tag**: A string label associated with notes for categorization
- **User_ID**: UUID identifier for future user association (currently hardcoded)

## Requirements

### Requirement 1: Note Creation

**User Story:** As a developer using the API, I want to create new notes with title, content, and tags, so that I can store structured information.

#### Acceptance Criteria

1. WHEN a valid note creation request is submitted, THE SmartNotes_API SHALL create a new note with auto-generated UUID
2. WHEN creating a note, THE SmartNotes_API SHALL require a non-empty title
3. WHEN creating a note, THE SmartNotes_API SHALL accept optional content as TEXT
4. WHEN creating a note, THE SmartNotes_API SHALL accept optional tags as a set of strings
5. WHEN creating a note, THE SmartNotes_API SHALL set default values for pinned (false), archived (false), and deleted (false)
6. WHEN creating a note, THE SmartNotes_API SHALL assign a hardcoded userId for future auth compatibility
7. WHEN creating a note, THE SmartNotes_API SHALL set createdAt and updatedAt timestamps

### Requirement 2: Note Retrieval

**User Story:** As a developer using the API, I want to retrieve notes with pagination and sorting, so that I can display notes efficiently.

#### Acceptance Criteria

1. WHEN requesting all notes, THE SmartNotes_API SHALL return paginated results with default page size of 10
2. WHEN requesting notes, THE SmartNotes_API SHALL support custom page size and page number parameters
3. WHEN requesting notes, THE SmartNotes_API SHALL support sorting by updatedAt in descending order by default
4. WHEN requesting notes, THE SmartNotes_API SHALL exclude soft-deleted notes from results
5. WHEN requesting a specific note by ID, THE SmartNotes_API SHALL return the complete note details
6. IF a requested note ID does not exist or is deleted, THEN THE SmartNotes_API SHALL return appropriate error response

### Requirement 3: Note Updates

**User Story:** As a developer using the API, I want to update existing notes, so that I can modify note content and metadata.

#### Acceptance Criteria

1. WHEN updating a note, THE SmartNotes_API SHALL allow modification of title, content, tags, pinned, and archived fields
2. WHEN updating a note, THE SmartNotes_API SHALL automatically update the updatedAt timestamp
3. WHEN updating a note, THE SmartNotes_API SHALL preserve the original createdAt timestamp
4. WHEN updating a note, THE SmartNotes_API SHALL validate that title remains non-empty if provided
5. IF an update request targets a non-existent or deleted note, THEN THE SmartNotes_API SHALL return appropriate error response

### Requirement 4: Soft Delete Operations

**User Story:** As a developer using the API, I want to soft delete notes, so that data can be recovered if needed without permanent loss.

#### Acceptance Criteria

1. WHEN deleting a note, THE SmartNotes_API SHALL mark the deleted field as true instead of removing the record
2. WHEN soft deleting a note, THE SmartNotes_API SHALL update the updatedAt timestamp
3. WHEN soft deleting a note, THE SmartNotes_API SHALL preserve all other note data
4. WHEN retrieving notes, THE SmartNotes_API SHALL exclude soft-deleted notes from all listing operations
5. IF a delete request targets a non-existent note, THEN THE SmartNotes_API SHALL return appropriate error response

### Requirement 5: Search Functionality

**User Story:** As a developer using the API, I want to search notes by keywords, so that I can find relevant notes quickly.

#### Acceptance Criteria

1. WHEN searching notes, THE SmartNotes_API SHALL search both title and content fields
2. WHEN searching notes, THE SmartNotes_API SHALL perform case-insensitive matching
3. WHEN searching notes, THE SmartNotes_API SHALL exclude soft-deleted notes from search results
4. WHEN searching notes, THE SmartNotes_API SHALL return paginated results
5. WHEN searching with empty or null query, THE SmartNotes_API SHALL return all non-deleted notes

### Requirement 6: Data Persistence and Schema

**User Story:** As a system administrator, I want proper database schema design, so that the system performs well and supports future requirements.

#### Acceptance Criteria

1. THE Note_Repository SHALL use UUID as primary key for all note records
2. THE Note_Repository SHALL store tags in a separate collection table with proper relationships
3. THE Note_Repository SHALL create indexes on userId, updatedAt, and deleted fields for query performance
4. THE Note_Repository SHALL use PostgreSQL TEXT type for content field to support large text
5. THE Note_Repository SHALL enforce NOT NULL constraint on title field
6. THE Note_Repository SHALL store userId as UUID for future authentication integration

### Requirement 7: API Design and Error Handling

**User Story:** As a client application developer, I want consistent REST API design and proper error handling, so that I can integrate reliably.

#### Acceptance Criteria

1. THE SmartNotes_API SHALL use /api/notes as the base path for all note operations
2. THE SmartNotes_API SHALL return appropriate HTTP status codes (200, 201, 404, 400, 500)
3. THE SmartNotes_API SHALL use standard REST verbs (GET, POST, PUT, DELETE)
4. THE SmartNotes_API SHALL return consistent JSON response format for all endpoints
5. WHEN validation errors occur, THE SmartNotes_API SHALL return detailed error messages
6. WHEN server errors occur, THE SmartNotes_API SHALL return appropriate error responses without exposing internal details

### Requirement 8: Architecture and Future Compatibility

**User Story:** As a system architect, I want clean layered architecture and auth-ready design, so that the system is maintainable and extensible.

#### Acceptance Criteria

1. THE SmartNotes_API SHALL implement clear separation between Controller, Service, and Repository layers
2. THE SmartNotes_API SHALL use DTOs for all request and response data transfer
3. THE SmartNotes_API SHALL accept userId parameters in all service methods for future auth integration
4. THE SmartNotes_API SHALL use a hardcoded UUID for userId until authentication is implemented
5. WHEN authentication is later added, THE SmartNotes_API SHALL support extracting userId from request headers without database schema changes
6. THE SmartNotes_API SHALL implement global exception handling for consistent error responses