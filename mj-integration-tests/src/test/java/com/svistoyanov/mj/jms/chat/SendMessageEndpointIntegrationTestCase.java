package com.svistoyanov.mj.jms.chat;

import com.svistoyanov.mj.DaoProvider;
import com.svistoyanov.mj.MessageDao;
import com.svistoyanov.mj.UserDao;
import com.svistoyanov.mj.api.dto.ErrorCodesDto;
import com.svistoyanov.mj.api.dto.ErrorDto;
import com.svistoyanov.mj.api.dto.message.MessageDto;
import com.svistoyanov.mj.api.dto.message.MessageRequestDto;
import com.svistoyanov.mj.entity.Message;
import com.svistoyanov.mj.entity.User;
import com.svistoyanov.mj.jms.AbstractJmsSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

class SendMessageEndpointIntegrationTestCase extends AbstractJmsSupport {
    @Test
    void testSendMessageSuccess() {
        User author = new User(UUID.randomUUID());
        author.setUsername("author");
        author.setEmail("author@gmail.com");
        author.setPassword("112233");

        User recipient = new User(UUID.randomUUID());
        recipient.setUsername("recipient");
        recipient.setPassword("112233");
        recipient.setEmail("recipient@gmail.com");

        MessageRequestDto messageRequestDto = new MessageRequestDto();
        messageRequestDto.setContent("some content");
        messageRequestDto.setAuthorId(author.getId());
        messageRequestDto.setRecipientId(recipient.getId());

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            UserDao userDao = daoProvider.getUserDao();
            userDao.saveOrUpdate(author);
            userDao.saveOrUpdate(recipient);
            daoProvider.commit();
        }

        invokeForSuccess(messageRequestDto, getSendMessageOperation());

        Message dbMessage;
        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            MessageDao messageDao = daoProvider.getMessageDao();
            dbMessage = messageDao.loadByAuthorId(messageRequestDto.getAuthorId());
        }
        Assertions.assertEquals(messageRequestDto.getAuthorId(), dbMessage.getAuthor().getId());
        Assertions.assertEquals(messageRequestDto.getRecipientId(), dbMessage.getRecipient().getId());
        Assertions.assertEquals(messageRequestDto.getContent(), dbMessage.getContent());
        Assertions.assertNotNull(dbMessage);

    }

    @Test
    void testSendMessageNullArg() {
        final ErrorDto error = invokeForError(null, getSendMessageOperation());
        Assertions.assertNotNull(error);
        Assertions.assertEquals(ErrorCodesDto.VALIDATION, error.getErrorCode());
        Assertions.assertEquals(1, error.getMessages().size());
        Assertions.assertEquals("Request can not be null.", error.getMessages().get(0));
    }

    private Function<MessageRequestDto, CompletableFuture<MessageDto>> getSendMessageOperation() {
        return endpointRegistry.getChatEndpoint()::sendMessage;
    }
}
