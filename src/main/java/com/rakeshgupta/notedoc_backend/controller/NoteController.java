package com.rakeshgupta.notedoc_backend.controller;

import com.rakeshgupta.notedoc_backend.dto.request.NoteCreateRequestDto;
import com.rakeshgupta.notedoc_backend.dto.request.NoteUpdateRequestDto;
import com.rakeshgupta.notedoc_backend.dto.response.ErrorResponse;
import com.rakeshgupta.notedoc_backend.dto.response.NoteResponseDto;
import com.rakeshgupta.notedoc_backend.service.NoteService;
// Swagger imports removed for lightweight build
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.Parameter;
// import io.swagger.v3.oas.annotations.media.Content;
// import io.swagger.v3.oas.annotations.media.Schema;
// import io.swagger.v3.oas.annotations.responses.ApiResponse;
// import io.swagger.v3.oas.annotations.responses.ApiResponses;
// import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for note management operations.
 * Provides endpoints for CRUD operations, search functionality, and pagination.
 * Uses hardcoded userId until authentication is implemented.
 */
@RestController
@RequestMapping("/api/notes")
// @Tag(name = "Notes", description = "Note management operations") // Swagger annotation removed
public class NoteController {

    /**
     * Hardcoded user ID for all operations until authentication is implemented.
     * This UUID will be replaced with authenticated user ID in future versions.
     */
    private static final UUID HARDCODED_USER_ID = 
        UUID.fromString("11111111-1111-1111-1111-111111111111");

    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    /**
     * Create a new note.
     * 
     * @param request the note creation request with validation
     * @return ResponseEntity with created note data and HTTP 201 status
     */
    @PostMapping
    // Swagger annotations removed for lightweight build
    /*
    @Operation(
        summary = "Create a new note",
        description = "Creates a new note with the provided title and content"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Note created successfully",
            content = @Content(schema = @Schema(implementation = NoteResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    */
    public ResponseEntity<NoteResponseDto> createNote(
            @Valid @RequestBody 
            // @Parameter(description = "Note creation request", required = true) // Swagger annotation removed
            NoteCreateRequestDto request) {
        NoteResponseDto createdNote = noteService.createNote(request, HARDCODED_USER_ID);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNote);
    }

    /**
     * Get all notes with pagination and sorting.
     * 
     * @param page page number (default: 0)
     * @param size page size (default: 10)
     * @param sort sort specification (default: updatedAt,desc)
     * @return ResponseEntity with paginated notes and HTTP 200 status
     */
    @GetMapping
    // Swagger annotations removed for lightweight build
    /*
    @Operation(
        summary = "Get all notes",
        description = "Retrieves all notes with pagination and sorting support"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Notes retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class))
        )
    })
    */
    public ResponseEntity<Page<NoteResponseDto>> getAllNotes(
            @RequestParam(defaultValue = "0") 
            // @Parameter(description = "Page number (0-based)", example = "0") // Swagger annotation removed
            int page,
            @RequestParam(defaultValue = "10") 
            // @Parameter(description = "Number of items per page", example = "10") // Swagger annotation removed
            int size,
            @RequestParam(defaultValue = "updatedAt,desc") 
            // @Parameter(description = "Sort specification (field,direction)", example = "updatedAt,desc") // Swagger annotation removed
            String sort) {
        
        // Parse sort parameter
        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction direction = sortParams.length > 1 && "asc".equalsIgnoreCase(sortParams[1]) 
            ? Sort.Direction.ASC 
            : Sort.Direction.DESC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<NoteResponseDto> notes = noteService.getAllNotes(HARDCODED_USER_ID, pageable);
        
        return ResponseEntity.ok(notes);
    }

    /**
     * Get a specific note by ID.
     * 
     * @param id the note ID
     * @return ResponseEntity with note data and HTTP 200 status
     */
    @GetMapping("/{id}")
    // Swagger annotations removed for lightweight build
    /*
    @Operation(
        summary = "Get note by ID",
        description = "Retrieves a specific note by its unique identifier"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Note found and retrieved successfully",
            content = @Content(schema = @Schema(implementation = NoteResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Note not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    */
    public ResponseEntity<NoteResponseDto> getNoteById(
            @PathVariable 
            // @Parameter(description = "Note unique identifier", required = true) // Swagger annotation removed
            UUID id) {
        NoteResponseDto note = noteService.getNoteById(id, HARDCODED_USER_ID);
        return ResponseEntity.ok(note);
    }

    /**
     * Update an existing note.
     * 
     * @param id the note ID to update
     * @param request the note update request with validation
     * @return ResponseEntity with updated note data and HTTP 200 status
     */
    @PutMapping("/{id}")
    // Swagger annotations removed for lightweight build
    /*
    @Operation(
        summary = "Update a note",
        description = "Updates an existing note with new title and/or content"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Note updated successfully",
            content = @Content(schema = @Schema(implementation = NoteResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Note not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    */
    public ResponseEntity<NoteResponseDto> updateNote(
            @PathVariable 
            // @Parameter(description = "Note unique identifier", required = true) // Swagger annotation removed
            UUID id, 
            @Valid @RequestBody 
            // @Parameter(description = "Note update request", required = true) // Swagger annotation removed
            NoteUpdateRequestDto request) {
        NoteResponseDto updatedNote = noteService.updateNote(id, request, HARDCODED_USER_ID);
        return ResponseEntity.ok(updatedNote);
    }

    /**
     * Soft delete a note.
     * 
     * @param id the note ID to delete
     * @return ResponseEntity with HTTP 204 status (No Content)
     */
    @DeleteMapping("/{id}")
    // Swagger annotations removed for lightweight build
    /*
    @Operation(
        summary = "Delete a note",
        description = "Soft deletes a note (marks as deleted without removing from database)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Note deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Note not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    */
    public ResponseEntity<Void> deleteNote(
            @PathVariable 
            // @Parameter(description = "Note unique identifier", required = true) // Swagger annotation removed
            UUID id) {
        noteService.deleteNote(id, HARDCODED_USER_ID);
        return ResponseEntity.noContent().build();
    }

    /**
     * Search notes by query string.
     * 
     * @param q the search query
     * @param page page number (default: 0)
     * @param size page size (default: 10)
     * @return ResponseEntity with paginated search results and HTTP 200 status
     */
    @GetMapping("/search")
    // Swagger annotations removed for lightweight build
    /*
    @Operation(
        summary = "Search notes",
        description = "Searches notes by title and content using the provided query string"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Search completed successfully",
            content = @Content(schema = @Schema(implementation = Page.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid search query",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    */
    public ResponseEntity<Page<NoteResponseDto>> searchNotes(
            @RequestParam 
            // @Parameter(description = "Search query string", required = true, example = "meeting notes") // Swagger annotation removed
            String q,
            @RequestParam(defaultValue = "0") 
            // @Parameter(description = "Page number (0-based)", example = "0") // Swagger annotation removed
            int page,
            @RequestParam(defaultValue = "10") 
            // @Parameter(description = "Number of items per page", example = "10") // Swagger annotation removed
            int size) {
        
        // Default sort for search is by updatedAt descending
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
        Page<NoteResponseDto> searchResults = noteService.searchNotes(q, HARDCODED_USER_ID, pageable);
        
        return ResponseEntity.ok(searchResults);
    }
}