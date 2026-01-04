package com.rakeshgupta.notedoc_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Note entity representing a user's note with title, content, tags, and metadata.
 * Supports soft deletion and includes audit timestamps.
 */
@Entity
@Table(name = "notes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note {

    /**
     * Unique identifier for the note using UUID as primary key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Title of the note - required field with NOT NULL constraint
     */
    @Column(nullable = false, length = 255)
    private String title;

    /**
     * Content of the note - stored as TEXT to support large content
     */
    @Column(columnDefinition = "TEXT")
    private String content;

    /**
     * Tags associated with the note - stored in separate collection table
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "note_tags",
        joinColumns = @JoinColumn(name = "note_id")
    )
    @Column(name = "tags")
    private Set<String> tags = new HashSet<>();

    /**
     * Whether the note is pinned - defaults to false
     */
    @Column(nullable = false)
    private Boolean pinned = false;

    /**
     * Whether the note is archived - defaults to false
     */
    @Column(nullable = false)
    private Boolean archived = false;

    /**
     * Soft delete flag - defaults to false
     */
    @Column(nullable = false)
    private Boolean deleted = false;

    /**
     * User ID for future authentication integration - currently hardcoded
     */
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    /**
     * Timestamp when the note was created - automatically set on creation
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the note was last updated - automatically updated on modification
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}