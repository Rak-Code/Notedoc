package com.rakeshgupta.notedoc_backend.dto.request;

// Swagger import removed for lightweight build
// import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * DTO for note creation requests with validation constraints
 */
// @Schema(description = "Request object for creating a new note") // Swagger annotation removed
public class NoteCreateRequestDto {
    
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    // @Schema(description = "Note title", example = "Meeting Notes", required = true, maxLength = 255) // Swagger annotation removed
    private String title;
    
    // @Schema(description = "Note content in markdown format", example = "## Agenda\n- Discuss project timeline\n- Review budget") // Swagger annotation removed
    private String content;
    
    // @Schema(description = "Set of tags for categorizing the note", example = "[\"work\", \"meeting\", \"project\"]") // Swagger annotation removed
    private Set<String> tags = new HashSet<>();
    
    // @Schema(description = "Whether the note is pinned", example = "false", defaultValue = "false") // Swagger annotation removed
    private Boolean pinned = false;
    
    // @Schema(description = "Whether the note is archived", example = "false", defaultValue = "false") // Swagger annotation removed
    private Boolean archived = false;

    // Default constructor
    public NoteCreateRequestDto() {}

    // Constructor with all fields
    public NoteCreateRequestDto(String title, String content, Set<String> tags, Boolean pinned, Boolean archived) {
        this.title = title;
        this.content = content;
        this.tags = tags != null ? tags : new HashSet<>();
        this.pinned = pinned != null ? pinned : false;
        this.archived = archived != null ? archived : false;
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
        this.tags = tags != null ? tags : new HashSet<>();
    }

    public Boolean getPinned() {
        return pinned;
    }

    public void setPinned(Boolean pinned) {
        this.pinned = pinned != null ? pinned : false;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived != null ? archived : false;
    }
}