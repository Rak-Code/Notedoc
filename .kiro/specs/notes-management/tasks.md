# Implementation Plan: Notes Management System

## Overview

This implementation plan breaks down the SmartNotes backend development into discrete, incremental tasks. Each task builds upon previous work and includes comprehensive testing to ensure correctness. The plan follows the layered architecture design and implements all requirements systematically.

## Tasks

- [ ] 1. Set up project dependencies and configuration
  - Add missing Spring Boot dependencies (validation, web)
  - Configure application.yml for PostgreSQL connection
  - Set up database connection properties
  - _Requirements: 6.1, 6.6_

- [-] 2. Create core entity and database schema
  - [x] 2.1 Implement Note entity with JPA annotations
    - Create Note entity with all required fields (id, title, content, tags, pinned, archived, deleted, userId, createdAt, updatedAt)
    - Configure UUID primary key generation
    - Set up tag collection mapping with separate table
    - _Requirements: 6.1, 6.2, 6.4, 6.5, 6.6_

  - [ ]* 2.2 Write property test for Note entity creation
    - **Property 1: Note Creation with Valid Data**
    - **Validates: Requirements 1.1, 1.5, 1.6, 1.7**

  - [ ]* 2.3 Write property test for UUID primary key consistency
    - **Property 13: UUID Primary Key Consistency**
    - **Validates: Requirements 6.1**

- [-] 3. Implement data access layer
  - [x] 3.1 Create NoteRepository interface with custom queries
    - Implement JpaRepository extension with custom query methods
    - Add queries for active notes filtering (excluding soft-deleted)
    - Add search queries for title and content
    - _Requirements: 2.4, 5.1, 5.2_

  - [ ]* 3.2 Write property test for soft delete filtering
    - **Property 5: Soft Delete Filtering**
    - **Validates: Requirements 2.4, 4.4, 5.3**

  - [ ]* 3.3 Write property test for search field coverage
    - **Property 11: Search Field Coverage**
    - **Validates: Requirements 5.1, 5.2**

- [x] 4. Create DTOs and validation
  - [x] 4.1 Implement request DTOs with validation annotations
    - Create NoteCreateRequestDto with validation constraints
    - Create NoteUpdateRequestDto with optional field validation
    - Add proper validation messages
    - _Requirements: 1.2, 3.4, 7.5_

  - [x] 4.2 Implement response DTOs
    - Create NoteResponseDto with all note fields
    - Create ErrorResponse DTO for consistent error handling
    - _Requirements: 7.4, 7.6_

  - [ ]* 4.3 Write property test for title validation enforcement
    - **Property 2: Title Validation Enforcement**
    - **Validates: Requirements 1.2, 3.4**

  - [ ]* 4.4 Write property test for content and tags acceptance
    - **Property 3: Content and Tags Acceptance**
    - **Validates: Requirements 1.3, 1.4**

- [-] 5. Implement business logic layer
  - [x] 5.1 Create NoteService interface and implementation
    - Implement all CRUD operations with userId parameter support
    - Add business logic for soft delete operations
    - Implement search functionality with case-insensitive matching
    - _Requirements: 3.1, 4.1, 4.2, 4.3, 5.1, 5.2, 8.4_

  - [ ]* 5.2 Write property test for field update capability
    - **Property 8: Field Update Capability**
    - **Validates: Requirements 3.1**

  - [ ]* 5.3 Write property test for timestamp behavior on updates
    - **Property 9: Timestamp Behavior on Updates**
    - **Validates: Requirements 3.2, 3.3**

  - [ ]* 5.4 Write property test for soft delete implementation
    - **Property 10: Soft Delete Implementation**
    - **Validates: Requirements 4.1, 4.2, 4.3**

- [ ] 6. Checkpoint - Ensure core business logic tests pass
  - Ensure all tests pass, ask the user if questions arise.

- [ ] 7. Implement exception handling
  - [ ] 7.1 Create custom exception classes
    - Implement NoteNotFoundException for missing notes
    - Implement InvalidNoteDataException for validation errors
    - _Requirements: 2.6, 3.5, 4.5_

  - [ ] 7.2 Implement global exception handler
    - Create @RestControllerAdvice for centralized error handling
    - Handle validation errors with detailed messages
    - Handle generic exceptions without exposing internals
    - _Requirements: 7.5, 7.6, 8.6_

  - [ ]* 7.3 Write property test for error handling
    - **Property 7: Error Handling for Invalid Operations**
    - **Validates: Requirements 2.6, 3.5, 4.5**

  - [ ]* 7.4 Write property test for validation error detail
    - **Property 19: Validation Error Detail**
    - **Validates: Requirements 7.5**

- [x] 8. Implement REST controllers
  - [x] 8.1 Create NoteController with all endpoints
    - Implement POST /api/notes for note creation
    - Implement GET /api/notes for paginated note listing
    - Implement GET /api/notes/{id} for individual note retrieval
    - Implement PUT /api/notes/{id} for note updates
    - Implement DELETE /api/notes/{id} for soft delete
    - Implement GET /api/notes/search for note search
    - Use hardcoded userId constant
    - _Requirements: 7.1, 7.3, 8.4_

  - [ ]* 8.2 Write property test for pagination consistency
    - **Property 4: Pagination Consistency**
    - **Validates: Requirements 2.1, 2.2, 2.3**

  - [ ]* 8.3 Write property test for complete note retrieval
    - **Property 6: Complete Note Retrieval**
    - **Validates: Requirements 2.5**

  - [ ]* 8.4 Write property test for HTTP status code correctness
    - **Property 17: HTTP Status Code Correctness**
    - **Validates: Requirements 7.2**

- [ ] 9. Add remaining correctness properties
  - [ ]* 9.1 Write property test for empty search handling
    - **Property 12: Empty Search Handling**
    - **Validates: Requirements 5.5**

  - [ ]* 9.2 Write property test for tag relationship storage
    - **Property 14: Tag Relationship Storage**
    - **Validates: Requirements 6.2**

  - [ ]* 9.3 Write property test for large content support
    - **Property 15: Large Content Support**
    - **Validates: Requirements 6.4**

  - [ ]* 9.4 Write property test for JSON response consistency
    - **Property 18: JSON Response Consistency**
    - **Validates: Requirements 7.4**

  - [ ]* 9.5 Write property test for hardcoded user ID consistency
    - **Property 20: Hardcoded User ID Consistency**
    - **Validates: Requirements 8.4**

- [ ] 10. Database configuration and schema
  - [ ] 10.1 Configure database connection and JPA settings
    - Set up PostgreSQL connection in application.yml
    - Configure JPA/Hibernate properties for development
    - Set up database initialization strategy
    - _Requirements: 6.3, 6.4, 6.5_

  - [ ]* 10.2 Write property test for database title constraint
    - **Property 16: Database Title Constraint**
    - **Validates: Requirements 6.5**

- [ ] 11. Integration testing and final validation
  - [ ]* 11.1 Write integration tests for complete API flows
    - Test end-to-end note creation, retrieval, update, and deletion flows
    - Test search functionality with various query types
    - Test error scenarios and edge cases
    - _Requirements: All requirements_

  - [ ]* 11.2 Write property test for global exception handling
    - **Property 21: Global Exception Handling**
    - **Validates: Requirements 7.6, 8.6**

- [ ] 12. Final checkpoint - Ensure all tests pass and system is ready
  - Ensure all tests pass, ask the user if questions arise.
  - Verify all API endpoints work correctly
  - Confirm database schema is properly created
  - Validate error handling works as expected

## Notes

- Tasks marked with `*` are optional and can be skipped for faster MVP
- Each task references specific requirements for traceability
- Property tests validate universal correctness properties from the design document
- Unit tests validate specific examples and edge cases
- The hardcoded userId (11111111-1111-1111-1111-111111111111) will be used throughout until authentication is implemented
- All property tests should run minimum 100 iterations and be tagged with their corresponding design property