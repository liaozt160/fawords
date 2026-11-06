package com.fawords.payload.response;

import java.time.LocalDateTime;

public class QuoteResponse {
    private Long id;
    private String content;
    private boolean isOriginal;
    private String author;
    private String sourceType;
    private String sourceName;
    private String explanation;
    private String category;
    private boolean isPublic;
    private String creatorUsername;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public QuoteResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public boolean isOriginal() { return isOriginal; }
    public void setOriginal(boolean original) { isOriginal = original; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }

    public String getSourceName() { return sourceName; }
    public void setSourceName(String sourceName) { this.sourceName = sourceName; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public boolean isPublic() { return isPublic; }
    public void setPublic(boolean aPublic) { isPublic = aPublic; }

    public String getCreatorUsername() { return creatorUsername; }
    public void setCreatorUsername(String creatorUsername) { this.creatorUsername = creatorUsername; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
