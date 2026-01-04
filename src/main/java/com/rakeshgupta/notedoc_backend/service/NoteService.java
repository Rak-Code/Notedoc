package com.rakeshgupta.notedoc_backend.service;

import com.rakeshgupta.notedoc_backend.dto.request.NoteCreateRequestDto;
import com.rakeshgupta.notedoc_backend.dto.request.NoteUpdateRequestDto;
import com.rakeshgupta.notedoc_backend.dto.response.NoteResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Service interface for note management operations.
 * Provides business logic for CRUD operations, search functionality, and soft delete operations.
 * All operations include userId parameter support for future authentication integration.
 */
public interface NoteService {

    /**
     * Create a new note with the provided data.
     * Sets default values for pinned, archived, and deleted fields.
     * Assigns the provided userId and generates timestamps.
     * 
     * @param request the note creation request containing title, content, tags, etc.
     * @param userId the user ID to associate with the note
     * @return the created note as a response DTO
     */
    NoteResponseDto createNote(NoteCreateRequestDto request, UUID userId);

    /**
     * Retrieve all active (non-deleted) notes for a user with pagination.
     * Results are sorted by updatedAt in descending order by default.
     * 
     * @param userId the user ID to filter notes by
     * @param pageable pagination and sorting parameters
     * @return paginated list of active notes for the user
     */
    Page<NoteResponseDto> getAllNotes(UUID userId, Pageable pageable);

    /**
     * Retrieve a specific note by ID for a user.
     * Returns only active (non-deleted) notes.
     * 
     * @param id the note ID to retrieve
     * @param userId the user ID to filter by
     * @return the note as a response DTO
     * @throws com.rakeshgupta.notedoc_backend.exception.NoteNotFoundException if note not found or deleted
     */
    NoteResponseDto getNoteById(UUID id, UUID userId);

    /**
     * Update an existing note with the provided data.
     * Only updates fields that are provided (non-null) in the request.
     * Automatically updates the updatedAt timestamp.
     * 
     * @param id the note ID to update
     * @param request the note update request containing fields to update
     * @param userId the user ID to filter by
     * @return the updated note as a response DTO
     * @throws com.rakeshgupta.notedoc_backend.exception.NoteNotFoundException if note not found or deleted
     */
    NoteResponseDto updateNote(UUID id, NoteUpdateRequestDto request, UUID userId);

    /**
     * Soft delete a note by setting the deleted flag to true.
     * Preserves all note data and updates the updatedAt timestamp.
     * 
     * @param id the note ID to delete
     * @param userId the user ID to filter by
     * @throws com.rakeshgupta.notedoc_backend.exception.NoteNotFoundException if note not found or already deleted
     */
    void deleteNote(UUID id, UUID userId);

    /**
     * Search active (non-deleted) notes by query string in title and content fields.
     * Performs case-insensitive search and returns paginated results.
     * If query is empty or null, returns all active notes for the user.
     * 
     * @param query the search query string
     * @param userId the user ID to filter notes by
     * @param pageable pagination and sorting parameters
     * @return paginated list of notes matching the search query
     */
    Page<NoteResponseDto> searchNotes(String query, UUID userId, Pageable pageable);
}