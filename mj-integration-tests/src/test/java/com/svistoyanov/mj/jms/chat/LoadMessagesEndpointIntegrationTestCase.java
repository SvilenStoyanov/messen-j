package com.svistoyanov.mj.jms.chat;

import com.svistoyanov.mj.DaoProvider;
import com.svistoyanov.mj.MessageDao;
import com.svistoyanov.mj.UserDao;
import com.svistoyanov.mj.api.dto.ErrorCodesDto;
import com.svistoyanov.mj.api.dto.ErrorDto;
import com.svistoyanov.mj.api.dto.message.MessagesFilterDto;
import com.svistoyanov.mj.api.dto.message.MessagesWrapperDto;
import com.svistoyanov.mj.entity.Message;
import com.svistoyanov.mj.entity.User;
import com.svistoyanov.mj.jms.AbstractJmsSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

class LoadMessagesEndpointIntegrationTestCase extends AbstractJmsSupport {

    @Test
    void testLoadMessagesSuccess() {
        User author = new User(UUID.randomUUID());
        author.setUsername("author");
        author.setEmail("author@gmail.com");
        author.setPassword("112233");

        User recipient = new User(UUID.randomUUID());
        recipient.setUsername("recipient");
        recipient.setPassword("112233");
        recipient.setEmail("recipient@gmail.com");

        MessagesFilterDto messagesFilterDto = new MessagesFilterDto();
        messagesFilterDto.setAuthorId(author.getId());
        messagesFilterDto.setRecipientId(recipient.getId());
        messagesFilterDto.setStart(0);
        messagesFilterDto.setOffset(1);

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            UserDao userDao = daoProvider.getUserDao();
            userDao.saveOrUpdate(author);
            userDao.saveOrUpdate(recipient);
            daoProvider.commit();
        }

        Message message = createMessage(author, recipient);
        Message message1 = createMessage(author, recipient);

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            MessageDao messageDao = daoProvider.getMessageDao();
            messageDao.saveOrUpdate(message);
            messageDao.saveOrUpdate(message1);
            daoProvider.commit();
        }

        invokeForSuccess(messagesFilterDto, getLoadMessagesOperation());

        List<Message> dbMessages;
        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            MessageDao messageDao = daoProvider.getMessageDao();
            dbMessages = messageDao.loadMessagesByAuthorId(messagesFilterDto.getAuthorId(), 0, 2);
        }
        Assertions.assertEquals(2, dbMessages.size());

    }

    private Message createMessage(User author, User recipient) {
        Message message = new Message(UUID.randomUUID());
        message.setAuthor(author);
        message.setRecipient(recipient);
        LocalDateTime localDateTime = LocalDateTime.now();
        message.setSentAt(localDateTime);
        message.setContent("someContent");
        return message;
    }

    @Test
    void testLoadMessagesNullArg() {
        final ErrorDto error = invokeForError(null, getLoadMessagesOperation());
        Assertions.assertNotNull(error);
        Assertions.assertEquals(ErrorCodesDto.VALIDATION, error.getErrorCode());
        Assertions.assertEquals(1, error.getMessages().size());
        Assertions.assertEquals("Request can not be null.", error.getMessages().get(0));
    }

    private Function<MessagesFilterDto, CompletableFuture<MessagesWrapperDto>> getLoadMessagesOperation() {
        return endpointRegistry.getChatEndpoint()::loadMessages;
    }
}
