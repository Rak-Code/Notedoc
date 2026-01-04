package com.rakeshgupta.notedoc_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when note data validation fails or invalid data is provided.
 * Returns HTTP 400 Bad Request status when thrown from controllers.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidNoteDataException extends RuntimeException {

    /**
     * Create a new InvalidNoteDataException with the given message.
     * 
     * @param message the error message describing the validation failure
     */
    public InvalidNoteDataException(String message) {
        super(message);
    }

    /**
     * Create a new InvalidNoteDataException with a message and cause.
     * 
     * @param message the error message
     * @param cause the underlying cause
     */
    public InvalidNoteDataException(String message, Throwable cause) {
        super(message, cause);
    }
}