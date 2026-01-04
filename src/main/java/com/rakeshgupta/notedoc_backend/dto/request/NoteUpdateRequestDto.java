package com.rakeshgupta.notedoc_backend.dto.request;

import jakarta.validation.constraints.Size;
import java.util.Set;

/**
 * DTO for note update requests with optional field validation
 */
public class NoteUpdateRequestDto {
    
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;
    
    private String content;
    
    private Set<String> tags;
    
    private Boolean pinned;
    
    private Boolean archived;

    // Default constructor
    public NoteUpdateRequestDto() {}

    // Constructor with all fields
    public NoteUpdateRequestDto(String title, String content, Set<String> tags, Boolean pinned, Boolean archived) {
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.pinned = pinned;
        this.archived = archived;
    }

    // Getters and setters
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
}