package com.rakeshgupta.notedoc_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * Exception thrown when a requested note is not found or is soft-deleted.
 * Returns HTTP 404 Not Found status when thrown from controllers.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoteNotFoundException extends RuntimeException {

    /**
     * Create a new NoteNotFoundException with a message for the given note ID.
     * 
     * @param id the UUID of the note that was not found
     */
    public NoteNotFoundException(UUID id) {
        super("Note not found with id: " + id);
    }

    /**
     * Create a new NoteNotFoundException with a custom message.
     * 
     * @param message the custom error message
     */
    public NoteNotFoundException(String message) {
        super(message);
    }

    /**
     * Create a new NoteNotFoundException with a message and cause.
     * 
     * @param message the error message
     * @param cause the underlying cause
     */
    public NoteNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}