package com.svistoyanov.mj.dao;

import com.svistoyanov.mj.CrudDao;
import com.svistoyanov.mj.DaoProvider;
import com.svistoyanov.mj.DaoProviderFactory;
import com.svistoyanov.mj.MessageDao;
import com.svistoyanov.mj.entity.Message;
import com.svistoyanov.mj.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class MessageDaoTests extends AbstractCrudDaoTests<Message> {
    private User author;
    private User recipient;

    @BeforeEach
    void setUp() {
        author = UserDaoTests.createAndSaveUser(daoProviderFactory, "author@mail.com", "author", "author");
        recipient = UserDaoTests.createAndSaveUser(daoProviderFactory, "recipient@mail.com", "recipient", "recipient");
    }

    @Test
    void testInsertMessageWithNullAuthor() {
        Message expectedMessage = createValidEntity();
        expectedMessage.setAuthor(null);
        testInvalidSave(expectedMessage);
    }

    @Test
    void testInsertMessageWithNullRecipient() {
        Message expectedMessage = createValidEntity();
        expectedMessage.setRecipient(null);
        testInvalidSave(expectedMessage);
    }

    @Test
    void testInsertMessageWithNullContent() {
        Message expectedMessage = createValidEntity();
        expectedMessage.setContent(null);
        testInvalidSave(expectedMessage);
    }

    @Test
    void testInsertMessageWithNullDate() {
        Message expectedMessage = createValidEntity();
        expectedMessage.setSentAt(null);
        testInvalidSave(expectedMessage);
    }

    @Override
    public Message createValidEntity() {
        Message message = new Message(UUID.randomUUID());
        message.setAuthor(author);
        message.setRecipient(recipient);
        message.setContent("Hello!");
        message.setSentAt(LocalDateTime.of(2021, 11, 15, 11, 0));

        return message;
    }

    @Override
    protected CrudDao<Message> getDao(DaoProvider daoProvider) {
        return daoProvider.getMessageDao();
    }

    @Override
    protected void assertDeepEquals(Message expectedMessage, Message actualMessage) {
        Assertions.assertEquals(expectedMessage.getId(), actualMessage.getId());
        Assertions.assertEquals(expectedMessage.getAuthor().getId(), actualMessage.getAuthor().getId());
        Assertions.assertEquals(expectedMessage.getRecipient().getId(), actualMessage.getRecipient().getId());
        Assertions.assertEquals(expectedMessage.getSentAt(), actualMessage.getSentAt());
    }

    @Override
    protected void modifiedEntity(Message expected) {
        expected.setContent("asdas");
    }

    public static void createMessageAndSaveItToDb(DaoProviderFactory daoProviderFactory, User author, User recipient) {
        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            MessageDao messageDao = daoProvider.getMessageDao();

            Message message = new Message(UUID.randomUUID());
            message.setAuthor(author);
            message.setRecipient(recipient);
            message.setContent("asdf");
            message.setSentAt(LocalDateTime.now(ZoneOffset.UTC));

            messageDao.saveOrUpdate(message);
            daoProvider.commit();
        }
    }

}
