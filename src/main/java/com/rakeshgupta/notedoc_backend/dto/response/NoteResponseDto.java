package com.rakeshgupta.notedoc_backend.dto.response;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * DTO for note response with all note fields
 */
public class NoteResponseDto {
    
    private UUID id;
    private String title;
    private String content;
    private Set<String> tags;
    private Boolean pinned;
    private Boolean archived;
    private UUID userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default constructor
    public NoteResponseDto() {}

    // Constructor with all fields
    public NoteResponseDto(UUID id, String title, String content, Set<String> tags, 
                          Boolean pinned, Boolean archived, UUID userId, 
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.pinned = pinned;
        this.archived = archived;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Boolean getPinned() {
        return pinned;
    }

    public void setPinned(Boolean pinned) {
        this.pinned = pinned;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}