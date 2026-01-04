package com.rakeshgupta.notedoc_backend.repository;

import com.rakeshgupta.notedoc_backend.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Note entity providing data access operations.
 * Extends JpaRepository for basic CRUD operations and includes custom queries
 * for active notes filtering and search functionality.
 */
@Repository
public interface NoteRepository extends JpaRepository<Note, UUID> {

    /**
     * Find all active (non-deleted) notes for a specific user with pagination.
     * Excludes soft-deleted notes from results.
     * 
     * @param userId the user ID to filter notes by
     * @param pageable pagination and sorting parameters
     * @return paginated list of active notes for the user
     */
    @Query("SELECT n FROM Note n WHERE n.deleted = false AND n.userId = :userId")
    Page<Note> findActiveNotesByUserId(@Param("userId") UUID userId, Pageable pageable);

    /**
     * Find a specific active (non-deleted) note by ID and user ID.
     * Returns empty Optional if note doesn't exist or is soft-deleted.
     * 
     * @param id the note ID to search for
     * @param userId the user ID to filter by
     * @return Optional containing the note if found and active, empty otherwise
     */
    @Query("SELECT n FROM Note n WHERE n.deleted = false AND n.userId = :userId AND n.id = :id")
    Optional<Note> findActiveNoteByIdAndUserId(@Param("id") UUID id, @Param("userId") UUID userId);

    /**
     * Search active (non-deleted) notes by query string in title and content fields.
     * Performs case-insensitive search across both title and content.
     * Excludes soft-deleted notes from search results.
     * 
     * @param query the search query string
     * @param userId the user ID to filter notes by
     * @param pageable pagination and sorting parameters
     * @return paginated list of notes matching the search query
     */
    @Query("SELECT n FROM Note n WHERE n.deleted = false AND n.userId = :userId AND " +
           "(LOWER(n.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(n.content) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Note> searchActiveNotes(@Param("query") String query, @Param("userId") UUID userId, Pageable pageable);

    /**
     * Find all active (non-deleted) notes for a user without pagination.
     * Useful for operations that need to work with all user notes.
     * 
     * @param userId the user ID to filter notes by
     * @return list of all active notes for the user
     */
    @Query("SELECT n FROM Note n WHERE n.deleted = false AND n.userId = :userId")
    java.util.List<Note> findAllActiveNotesByUserId(@Param("userId") UUID userId);

    /**
     * Count active (non-deleted) notes for a specific user.
     * Useful for pagination metadata and statistics.
     * 
     * @param userId the user ID to count notes for
     * @return number of active notes for the user
     */
    @Query("SELECT COUNT(n) FROM Note n WHERE n.deleted = false AND n.userId = :userId")
    long countActiveNotesByUserId(@Param("userId") UUID userId);
}