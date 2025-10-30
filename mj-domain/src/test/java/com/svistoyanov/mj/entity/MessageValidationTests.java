package com.svistoyanov.mj.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.svistoyanov.mj.constants.Constants.*;

public class MessageValidationTests extends AbstractEntityValidationTests<Message> {

    @BeforeEach
    public void setUp() {
        entity = new Message(UUID.randomUUID());
        entity.setAuthor(createValidUser());
        entity.setRecipient(createValidUser());
        entity.setContent("Lorem ipsum dolor sit amet, consectetur adipiscing");
        entity.setSentAt(LocalDateTime.now());
    }

    public User createValidUser() {
        return createUser("email@email.com", "username", "password");
    }

    public static User createUser(String email, String username, String password) {
        User user = new User(UUID.randomUUID());
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);

        return user;
    }

    @Test
    void testMessageWithNullAuthor() {
        entity.setAuthor(null);
        testInvalidEntity(entity, AUTHOR_NULL_ERROR_MESSAGE);
    }

    @Test
    void testMessageWithNullRecipient() {
        entity.setRecipient(null);
        testInvalidEntity(entity, RECIPIENT_NULL_ERROR_MESSAGE);
    }

    @Test
    void testMessageWithNullContent() {
        entity.setContent(null);
        testInvalidEntity(entity, CONTENT_BLANK_ERROR_MESSAGE);
    }

    @Test
    void testMessageWithEmptySpaceContent() {
        entity.setContent(" ");
        testInvalidEntity(entity, CONTENT_BLANK_ERROR_MESSAGE);
    }

    @Test
    void testMessageWithBlankContent() {
        entity.setContent("");
        testInvalidEntity(entity, CONTENT_BLANK_ERROR_MESSAGE);
    }

    @Test
    void testMessageWithOverTheMaxLengthContent() {
        entity.setContent(OVER_THE_MAX_LENGTH_CONTENT);
        testInvalidEntity(entity, OVER_THE_MAX_LENGTH_CONTENT_ERROR_MESSAGE);
    }

    @Test
    void testMessageWithNullSentAt() {
        entity.setSentAt(null);
        testInvalidEntity(entity, SENT_AT_ERROR_MESSAGE);
    }
}
