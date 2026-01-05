package com.rakeshgupta.notedoc_backend.dto.request;

// Swagger import removed for lightweight build
// import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import java.util.Set;

/**
 * DTO for note update requests with optional field validation
 */
// @Schema(description = "Request object for updating an existing note. All fields are optional.") // Swagger annotation removed
public class NoteUpdateRequestDto {
    
    @Size(max = 255, message = "Title must not exceed 255 characters")
    // @Schema(description = "Updated note title", example = "Updated Meeting Notes", maxLength = 255) // Swagger annotation removed
    private String title;
    
    // @Schema(description = "Updated note content in markdown format", example = "## Updated Agenda\n- Finalize project timeline\n- Approve budget") // Swagger annotation removed
    private String content;
    
    // @Schema(description = "Updated set of tags for categorizing the note", example = "[\"work\", \"meeting\", \"project\", \"urgent\"]") // Swagger annotation removed
    private Set<String> tags;
    
    // @Schema(description = "Updated pinned status", example = "true") // Swagger annotation removed
    private Boolean pinned;
    
    // @Schema(description = "Updated archived status", example = "false") // Swagger annotation removed
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