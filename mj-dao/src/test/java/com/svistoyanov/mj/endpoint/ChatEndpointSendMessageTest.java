package com.svistoyanov.mj.endpoint;

import com.svistoyanov.mj.DaoProvider;
import com.svistoyanov.mj.MessageDao;
import com.svistoyanov.mj.api.dto.ErrorCodesDto;
import com.svistoyanov.mj.api.dto.message.MessageDto;
import com.svistoyanov.mj.api.dto.message.MessageRequestDto;
import com.svistoyanov.mj.dao.UserDaoTests;
import com.svistoyanov.mj.entity.Message;
import com.svistoyanov.mj.entity.User;
import com.svistoyanov.mj.worker.ChatEndpointWorker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.svistoyanov.mj.constants.WorkerConstants.*;

class ChatEndpointSendMessageTest extends AbstractEndpointTest<MessageDto, Message, MessageDto> {
    protected final ChatEndpointWorker chatEndpointWorker = new ChatEndpointWorker(validator, daoProviderFactory);
    private final   MessageRequestDto  messageRequestDto  = new MessageRequestDto();

    @BeforeEach
    void setUp() {
        User author = UserDaoTests.createAndSaveUser(daoProviderFactory, "author@abv.bg", "authorusername", "authorpassword");
        User recipient = UserDaoTests.createAndSaveUser(daoProviderFactory, "recipient@abv.bg", "recipientusername", "recipientpassword");

        //set author , recipient and content to the requestDto
        messageRequestDto.setAuthorId(author.getId());
        messageRequestDto.setRecipientId(recipient.getId());
        messageRequestDto.setContent("some content lorem ipsum");
    }

    @Test
    void testSendMessagesWithNullRequest() {
        //Arrange
        MessageRequestDto messageRequestDto = null;

        //Act
        final List<String> messages = waitForBadRequest(chatEndpointWorker.sendMessage(messageRequestDto), ErrorCodesDto.VALIDATION);

        //Assert
        Assertions.assertEquals(1, messages.size());
        Assertions.assertEquals(REQUEST_CANNOT_BE_NULL, messages.get(0));

        logger.debug("End of test reached");
    }

    @Test
    void sendMessageSuccessTest() {
        CompletableFuture<MessageDto> completableFuture = chatEndpointWorker.sendMessage(messageRequestDto);

        MessageDto messageDto = waitForSuccess(completableFuture);
        Message dbMessage;

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            MessageDao messageDao = daoProvider.getMessageDao();
            dbMessage = messageDao.loadById(messageDto.getId());
        }

        assertDeepEquals(dbMessage, messageDto);
    }

    @Test
    void sendMessageWithNullAuthorIdTest() {
        //Arrange
        messageRequestDto.setAuthorId(null);

        //Act
        final List<String> messages = waitForBadRequest(chatEndpointWorker.sendMessage(messageRequestDto), ErrorCodesDto.VALIDATION);

        //Assert
        Assertions.assertEquals(1, messages.size());
        Assertions.assertEquals(SEND_MESSAGE_AUTHOR_ID_CANNOT_BE_NULL_ERROR_MESSAGE, messages.get(0));
        logger.debug("End of test reached");
    }

    @Test
    void sendMessageWithNullRecipientIdTest() {
        //Arrange
        messageRequestDto.setRecipientId(null);

        //Act
        final List<String> messages = waitForBadRequest(chatEndpointWorker.sendMessage(messageRequestDto), ErrorCodesDto.VALIDATION);

        Assertions.assertEquals(1, messages.size());
        Assertions.assertEquals(SEND_MESSAGE_RECIPIENT_ID_CANNOT_BE_NULL_ERROR_MESSAGE, messages.get(0));
        logger.debug("End of test reached");
    }

    @Test
    void sendMessageWithNullContentTest() {
        //Arrange
        messageRequestDto.setContent(null);

        //Act
        final List<String> messages = waitForBadRequest(chatEndpointWorker.sendMessage(messageRequestDto), ErrorCodesDto.VALIDATION);

        Assertions.assertEquals(1, messages.size());
        Assertions.assertEquals(SEND_MESSAGE_CONTENT_CANNOT_BE_NULL_ERROR_MESSAGE, messages.get(0));
        logger.debug("End of test reached");
    }

    @Test
    void sendMessageWithEmptyContentTest() {
        //Arrange
        messageRequestDto.setContent("");

        //Act
        final List<String> messages = waitForBadRequest(chatEndpointWorker.sendMessage(messageRequestDto), ErrorCodesDto.VALIDATION);

        Assertions.assertEquals(1, messages.size());

        Assertions.assertEquals(SEND_MESSAGE_CONTENT_CANNOT_BE_BLANK_ERROR_MESSAGE, messages.get(0));
        logger.debug("End of test reached");
    }

    @Override
    protected void assertDeepEquals(Message expected, MessageDto actual) {
        Assertions.assertNotNull(expected);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getContent(), actual.getContent());
        Assertions.assertEquals(expected.getAuthor().getId(), actual.getAuthor().getId());
        Assertions.assertEquals(expected.getRecipient().getId(), actual.getRecipient().getId());
        //        Assertions.assertEquals(expected.getSentAt().withNano(0), actual.getSentAt().withNano(0));
        System.out.println("        Expected:" + expected.getSentAt());
        System.out.println("        Actual  :" + actual.getSentAt());
        Assertions.assertTrue(expected.getSentAt().isEqual(actual.getSentAt()));

    }
}
