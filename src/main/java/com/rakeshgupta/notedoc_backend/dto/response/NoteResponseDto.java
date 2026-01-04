package com.rakeshgupta.notedoc_backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * DTO for note response with all note fields
 */
@Schema(description = "Response object containing complete note information")
public class NoteResponseDto {
    
    @Schema(description = "Unique identifier of the note", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;
    
    @Schema(description = "Note title", example = "Meeting Notes")
    private String title;
    
    @Schema(description = "Note content in markdown format", example = "## Agenda\n- Discuss project timeline\n- Review budget")
    private String content;
    
    @Schema(description = "Set of tags associated with the note", example = "[\"work\", \"meeting\", \"project\"]")
    private Set<String> tags;
    
    @Schema(description = "Whether the note is pinned", example = "false")
    private Boolean pinned;
    
    @Schema(description = "Whether the note is archived", example = "false")
    private Boolean archived;
    
    @Schema(description = "ID of the user who owns this note", example = "11111111-1111-1111-1111-111111111111")
    private UUID userId;
    
    @Schema(description = "Timestamp when the note was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the note was last updated", example = "2024-01-15T14:45:00")
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