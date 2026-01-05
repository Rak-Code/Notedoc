package com.rakeshgupta.notedoc_backend.controller;

import com.rakeshgupta.notedoc_backend.dto.request.NoteCreateRequestDto;
import com.rakeshgupta.notedoc_backend.dto.request.NoteUpdateRequestDto;
import com.rakeshgupta.notedoc_backend.dto.response.NoteResponseDto;
import com.rakeshgupta.notedoc_backend.service.NoteService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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
 * Direct REST controller for note management operations without /api prefix.
 * This controller handles requests directly to /notes for frontend compatibility.
 */
@RestController
@RequestMapping("/notes")
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:4200", "https://notedoc-alpha.vercel.app"})
public class DirectNoteController {

    /**
     * Hardcoded user ID for all operations until authentication is implemented.
     */
    private static final UUID HARDCODED_USER_ID = 
        UUID.fromString("11111111-1111-1111-1111-111111111111");

    private final NoteService noteService;

    @Autowired
    public DirectNoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    /**
     * Create a new note.
     */
    @PostMapping
    public ResponseEntity<NoteResponseDto> createNote(@Valid @RequestBody NoteCreateRequestDto request) {
        log.info("Creating note with title: {}", request.getTitle());
        NoteResponseDto createdNote = noteService.createNote(request, HARDCODED_USER_ID);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNote);
    }

    /**
     * Get all notes with pagination and sorting.
     */
    @GetMapping
    public ResponseEntity<Page<NoteResponseDto>> getAllNotes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "updatedAt") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "false") boolean archived) {
        
        log.info("Getting notes - page: {}, size: {}, sort: {}, direction: {}, archived: {}", 
                page, size, sort, direction, archived);
        
        // Parse sort direction
        Sort.Direction sortDirection = "asc".equalsIgnoreCase(direction) 
            ? Sort.Direction.ASC 
            : Sort.Direction.DESC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        Page<NoteResponseDto> notes = noteService.getAllNotes(HARDCODED_USER_ID, pageable);
        
        return ResponseEntity.ok(notes);
    }

    /**
     * Get a specific note by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NoteResponseDto> getNoteById(@PathVariable UUID id) {
        log.info("Getting note by ID: {}", id);
        NoteResponseDto note = noteService.getNoteById(id, HARDCODED_USER_ID);
        return ResponseEntity.ok(note);
    }

    /**
     * Update an existing note.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NoteResponseDto> updateNote(
            @PathVariable UUID id, 
            @Valid @RequestBody NoteUpdateRequestDto request) {
        log.info("Updating note with ID: {}", id);
        NoteResponseDto updatedNote = noteService.updateNote(id, request, HARDCODED_USER_ID);
        return ResponseEntity.ok(updatedNote);
    }

    /**
     * Soft delete a note.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable UUID id) {
        log.info("Deleting note with ID: {}", id);
        noteService.deleteNote(id, HARDCODED_USER_ID);
        return ResponseEntity.noContent().build();
    }

    /**
     * Search notes by query string.
     */
    @GetMapping("/search")
    public ResponseEntity<Page<NoteResponseDto>> searchNotes(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("Searching notes with query: {}, page: {}, size: {}", q, page, size);
        
        // Default sort for search is by updatedAt descending
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
        Page<NoteResponseDto> searchResults = noteService.searchNotes(q, HARDCODED_USER_ID, pageable);
        
        return ResponseEntity.ok(searchResults);
    }

    /**
     * Handle preflight OPTIONS requests
     */
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> handleOptions() {
        return ResponseEntity.ok().build();
    }
}