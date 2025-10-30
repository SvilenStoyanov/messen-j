package com.svistoyanov.mj.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static com.svistoyanov.mj.constants.Constants.*;

public class UserValidationTests extends AbstractEntityValidationTests<User>
{
    @BeforeEach
    public void setUp()
    {
        entity = new User(UUID.randomUUID());
        entity.setEmail("email@email.com");
        entity.setUsername("Username");
        entity.setPassword("password");
    }

    @Test
    void testNullEmail()
    {
        entity.setEmail(null);
        testInvalidEntity(entity, EMAIL_NULL_ERROR_MESSAGE);
    }

    @Test
    void testEmptyEmail()
    {
        entity.setEmail("");
        testInvalidEntity(entity, EMAIL_PATTERN_ERROR_MESSAGE);
    }

    @Test
    void testInvalidEmail()
    {
        entity.setEmail("email");
        testInvalidEntity(entity, EMAIL_PATTERN_ERROR_MESSAGE);
    }

    @ParameterizedTest
    @ValueSource(strings = {"email.email.com", "email", "email@email..com"})
    void testIncorrectEmailPattern(String email)
    {
        entity.setEmail(email);
        testInvalidEntity(entity, EMAIL_PATTERN_ERROR_MESSAGE);
    }

    @Test
    void testNullUsername()
    {
        entity.setUsername(null);
        testInvalidEntity(entity, USERNAME_NULL_ERROR_MESSAGE);
    }

    @Test
    void testEmptyUsername()
    {
        entity.setUsername("   ");
        testInvalidEntity(entity, USERNAME_PATTERN_ERROR_MESSAGE);
    }

    @Test
    void testWhenUsernameIsMoreThanOneWord()
    {
        entity.setUsername("Two Words");
        testInvalidEntity(entity, USERNAME_PATTERN_ERROR_MESSAGE);
    }

    @Test
    void testUsernameMinLength()
    {
        entity.setUsername("12");
        testInvalidEntity(entity, USERNAME_LENGTH_ERROR_MESSAGE);
    }

    @Test
    void testUsernameMaxLength()
    {
        entity.setUsername("123456789012345678901");
        testInvalidEntity(entity, USERNAME_LENGTH_ERROR_MESSAGE);
    }

    @Test
    void TestNullPassword()
    {
        entity.setPassword(null);
        testInvalidEntity(entity, PASSWORD_EMPTY_ERROR_MESSAGE);
    }

    @Test
    void testEmptyPassword()
    {
        entity.setPassword("   ");
        testInvalidEntity(entity, PASSWORD_EMPTY_ERROR_MESSAGE);
    }

    @Test
    void testPasswordMinLength()
    {
        entity.setPassword("12");
        testInvalidEntity(entity, PASSWORD_LENGTH_ERROR_MESSAGE);
    }

    @Test
    void testPasswordMaxLength()
    {
        entity.setPassword("123456789012345678901");
        testInvalidEntity(entity, PASSWORD_LENGTH_ERROR_MESSAGE);
    }

}