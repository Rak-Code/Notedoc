package com.rakeshgupta.notedoc_backend.service.impl;

import com.rakeshgupta.notedoc_backend.dto.request.NoteCreateRequestDto;
import com.rakeshgupta.notedoc_backend.dto.request.NoteUpdateRequestDto;
import com.rakeshgupta.notedoc_backend.dto.response.NoteResponseDto;
import com.rakeshgupta.notedoc_backend.entity.Note;
import com.rakeshgupta.notedoc_backend.exception.InvalidNoteDataException;
import com.rakeshgupta.notedoc_backend.exception.NoteNotFoundException;
import com.rakeshgupta.notedoc_backend.repository.NoteRepository;
import com.rakeshgupta.notedoc_backend.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.UUID;

/**
 * Implementation of NoteService providing business logic for note management operations.
 * Handles CRUD operations, search functionality, and soft delete operations with userId support.
 */
@Service
@Transactional
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public NoteResponseDto createNote(NoteCreateRequestDto request, UUID userId) {
        // Validate input
        if (request == null) {
            throw new InvalidNoteDataException("Note creation request cannot be null");
        }
        if (userId == null) {
            throw new InvalidNoteDataException("User ID cannot be null");
        }
        if (!StringUtils.hasText(request.getTitle())) {
            throw new InvalidNoteDataException("Title is required and cannot be empty");
        }

        // Create new note entity
        Note note = new Note();
        note.setTitle(request.getTitle().trim());
        note.setContent(request.getContent());
        note.setTags(request.getTags() != null ? new HashSet<>(request.getTags()) : new HashSet<>());
        note.setPinned(request.getPinned() != null ? request.getPinned() : false);
        note.setArchived(request.getArchived() != null ? request.getArchived() : false);
        note.setDeleted(false); // Always false for new notes
        note.setUserId(userId);
        // createdAt and updatedAt are set automatically by @CreationTimestamp and @UpdateTimestamp

        // Save the note
        Note savedNote = noteRepository.save(note);

        // Convert to response DTO
        return convertToResponseDto(savedNote);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NoteResponseDto> getAllNotes(UUID userId, Pageable pageable) {
        if (userId == null) {
            throw new InvalidNoteDataException("User ID cannot be null");
        }

        Page<Note> notes = noteRepository.findActiveNotesByUserId(userId, pageable);
        return notes.map(this::convertToResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public NoteResponseDto getNoteById(UUID id, UUID userId) {
        if (id == null) {
            throw new InvalidNoteDataException("Note ID cannot be null");
        }
        if (userId == null) {
            throw new InvalidNoteDataException("User ID cannot be null");
        }

        Note note = noteRepository.findActiveNoteByIdAndUserId(id, userId)
                .orElseThrow(() -> new NoteNotFoundException(id));

        return convertToResponseDto(note);
    }

    @Override
    public NoteResponseDto updateNote(UUID id, NoteUpdateRequestDto request, UUID userId) {
        // Validate input
        if (id == null) {
            throw new InvalidNoteDataException("Note ID cannot be null");
        }
        if (request == null) {
            throw new InvalidNoteDataException("Note update request cannot be null");
        }
        if (userId == null) {
            throw new InvalidNoteDataException("User ID cannot be null");
        }

        // Find existing note
        Note existingNote = noteRepository.findActiveNoteByIdAndUserId(id, userId)
                .orElseThrow(() -> new NoteNotFoundException(id));

        // Update fields only if they are provided (non-null)
        if (request.getTitle() != null) {
            if (!StringUtils.hasText(request.getTitle())) {
                throw new InvalidNoteDataException("Title cannot be empty");
            }
            existingNote.setTitle(request.getTitle().trim());
        }

        if (request.getContent() != null) {
            existingNote.setContent(request.getContent());
        }

        if (request.getTags() != null) {
            existingNote.setTags(new HashSet<>(request.getTags()));
        }

        if (request.getPinned() != null) {
            existingNote.setPinned(request.getPinned());
        }

        if (request.getArchived() != null) {
            existingNote.setArchived(request.getArchived());
        }

        // updatedAt is automatically updated by @UpdateTimestamp
        Note updatedNote = noteRepository.save(existingNote);

        return convertToResponseDto(updatedNote);
    }

    @Override
    public void deleteNote(UUID id, UUID userId) {
        if (id == null) {
            throw new InvalidNoteDataException("Note ID cannot be null");
        }
        if (userId == null) {
            throw new InvalidNoteDataException("User ID cannot be null");
        }

        // Find existing note
        Note existingNote = noteRepository.findActiveNoteByIdAndUserId(id, userId)
                .orElseThrow(() -> new NoteNotFoundException(id));

        // Perform soft delete
        existingNote.setDeleted(true);
        // updatedAt is automatically updated by @UpdateTimestamp

        noteRepository.save(existingNote);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NoteResponseDto> searchNotes(String query, UUID userId, Pageable pageable) {
        if (userId == null) {
            throw new InvalidNoteDataException("User ID cannot be null");
        }

        Page<Note> notes;
        
        // If query is empty or null, return all active notes
        if (!StringUtils.hasText(query)) {
            notes = noteRepository.findActiveNotesByUserId(userId, pageable);
        } else {
            // Perform case-insensitive search in title and content
            notes = noteRepository.searchActiveNotes(query.trim(), userId, pageable);
        }

        return notes.map(this::convertToResponseDto);
    }

    /**
     * Convert Note entity to NoteResponseDto.
     * 
     * @param note the note entity to convert
     * @return the converted response DTO
     */
    private NoteResponseDto convertToResponseDto(Note note) {
        NoteResponseDto dto = new NoteResponseDto();
        dto.setId(note.getId());
        dto.setTitle(note.getTitle());
        dto.setContent(note.getContent());
        dto.setTags(note.getTags() != null ? new HashSet<>(note.getTags()) : new HashSet<>());
        dto.setPinned(note.getPinned());
        dto.setArchived(note.getArchived());
        dto.setUserId(note.getUserId());
        dto.setCreatedAt(note.getCreatedAt());
        dto.setUpdatedAt(note.getUpdatedAt());
        return dto;
    }
}