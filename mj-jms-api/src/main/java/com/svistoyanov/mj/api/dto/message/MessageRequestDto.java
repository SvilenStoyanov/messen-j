package com.svistoyanov.mj.api.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.svistoyanov.mj.api.dto.AbstractDto;

import java.util.UUID;

public class MessageRequestDto extends AbstractDto {
    @JsonProperty("authorId")
    private UUID authorId;

    @JsonProperty("recipientId")
    private UUID recipientId;

    @JsonProperty("content")
    private String content;

    public MessageRequestDto() {
    }

    public MessageRequestDto(UUID authorId, UUID recipientId, String content) {
        this.authorId = authorId;
        this.recipientId = recipientId;
        this.content = content;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }

    public UUID getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(UUID recipientId) {
        this.recipientId = recipientId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
