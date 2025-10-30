package com.svistoyanov.mj.api.dto.message;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.svistoyanov.mj.api.dto.AbstractEntityDto;
import com.svistoyanov.mj.api.dto.user.UserDescriptionDto;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY,
        setterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY
)
public class MessageDto extends AbstractEntityDto {
    @JsonProperty("author")
    private UserDescriptionDto author;

    @JsonProperty("recipient")
    private UserDescriptionDto recipient;

    @JsonProperty("content")
    private String content;

    @JsonProperty("sentAt")
    private LocalDateTime sentAt;

    public MessageDto() {
    }

    public MessageDto(UUID id, UserDescriptionDto author, UserDescriptionDto recipient, String content,
            LocalDateTime sentAt) {
        super(id);
        this.author = author;
        this.recipient = recipient;
        this.content = content;
        this.sentAt = sentAt;
    }

    public UserDescriptionDto getAuthor() {
        return author;
    }

    public void setAuthor(UserDescriptionDto author) {
        this.author = author;
    }

    public UserDescriptionDto getRecipient() {
        return recipient;
    }

    public void setRecipient(UserDescriptionDto recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
}
