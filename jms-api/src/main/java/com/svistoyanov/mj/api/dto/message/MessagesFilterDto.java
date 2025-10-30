package com.svistoyanov.mj.api.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.svistoyanov.mj.api.dto.AbstractDto;

import java.util.UUID;

public class MessagesFilterDto extends AbstractDto {
    @JsonProperty("authorId")
    private UUID authorId;

    @JsonProperty("recipientId")
    private UUID recipientId;

    @JsonProperty("start")
    private Integer start;

    @JsonProperty("offset")
    private Integer offset;

    public MessagesFilterDto() {
    }

    public MessagesFilterDto(UUID authorId, UUID recipientId, Integer start, Integer offset) {
        this.authorId = authorId;
        this.recipientId = recipientId;
        this.start = start;
        this.offset = offset;
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

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
