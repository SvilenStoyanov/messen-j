package com.svistoyanov.mj.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.svistoyanov.mj.constants.Constants.*;

public class Message extends AbstractEntity {

    @NotNull(message = AUTHOR_NULL_ERROR_MESSAGE)
    private User author;

    @NotNull(message = RECIPIENT_NULL_ERROR_MESSAGE)
    private User recipient;

    @NotBlank(message = CONTENT_BLANK_ERROR_MESSAGE)
    @Length(max = 500, message = OVER_THE_MAX_LENGTH_CONTENT_ERROR_MESSAGE)
    private String content;

    @NotNull
    private LocalDateTime sentAt;

    protected Message() {
    }

    public Message(UUID id) {
        super(id);
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
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
