package com.fawords.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class QuoteRequest {
    @NotBlank
    @Size(max = 1000)
    private String content;

    private boolean isOriginal = true;
    private String author;
    private String sourceType;
    private String sourceName;
    private String explanation;
    private String category;
    private boolean isPublic = true;

    public QuoteRequest() {}

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
}
