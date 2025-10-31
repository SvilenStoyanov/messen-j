package com.svistoyanov.mj.endpoint;

import com.svistoyanov.mj.DaoProvider;
import com.svistoyanov.mj.MessageDao;
import com.svistoyanov.mj.api.dto.ErrorCodesDto;
import com.svistoyanov.mj.api.dto.message.MessageDto;
import com.svistoyanov.mj.api.dto.message.MessagesFilterDto;
import com.svistoyanov.mj.api.dto.message.MessagesWrapperDto;
import com.svistoyanov.mj.dao.MessageDaoTests;
import com.svistoyanov.mj.dao.UserDaoTests;
import com.svistoyanov.mj.entity.Message;
import com.svistoyanov.mj.entity.User;
import com.svistoyanov.mj.mapper.MessageMapper;
import com.svistoyanov.mj.worker.ChatEndpointWorker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.svistoyanov.mj.constants.WorkerConstants.*;

class ChatEndPointLoadMessageTest extends AbstractEndpointTest<MessagesWrapperDto, MessageDto, MessageDto> {
    protected final ChatEndpointWorker chatEndpointWorker = new ChatEndpointWorker(validator, daoProviderFactory);
    private final   MessagesFilterDto  messagesFilterDto  = new MessagesFilterDto();

    private User author;
    private User recipient;
    private User recipient1;

    @BeforeEach
    protected void setUp() {
        author = UserDaoTests.createAndSaveUser(daoProviderFactory, "author@abv.bg", "authorusername", "authorpassword");
        recipient = UserDaoTests.createAndSaveUser(daoProviderFactory, "recipient@abv.bg", "recipientusername", "recipientpassword");
        recipient1 = UserDaoTests.createAndSaveUser(daoProviderFactory, "zzzz@abv.bg", "zzzzz", "recipientpassword");

        addMessages();

        messagesFilterDto.setAuthorId(author.getId());
        messagesFilterDto.setRecipientId(recipient1.getId());
        messagesFilterDto.setStart(0);
        messagesFilterDto.setOffset(10);
    }

    @Test
    void testLoadMessagesWithNullRequest() {
        //Arrange
        MessagesFilterDto nullRequestFilterDto = null;

        //Act
        final List<String> messages = waitForBadRequest(chatEndpointWorker.loadMessages(nullRequestFilterDto), ErrorCodesDto.VALIDATION);

        //Assert
        Assertions.assertEquals(1, messages.size());
        Assertions.assertEquals(REQUEST_CANNOT_BE_NULL, messages.get(0));

        logger.debug("End of test reached");
    }

    @Test
    void loadMessagesByAuthorIdAndRecipientId() {
        CompletableFuture<MessagesWrapperDto> completableFuture = chatEndpointWorker.loadMessages(messagesFilterDto);

        MessagesWrapperDto messagesWrapperDto = waitForSuccess(completableFuture);

        List<Message> messages;
        List<MessageDto> messageDtos;

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            MessageDao messageDao = daoProvider.getMessageDao();
            messages = messageDao.loadMessagesByAuthorIdAndRecipientId(author.getId(), recipient1.getId(), 0, 5);
            messageDtos = MessageMapper.instance.mapListOfMessagesToDtoMessages(messages);
        }

        for (int i = 0; i < messageDtos.size(); i++) {
            MessageDto messageDto = messageDtos.get(i);
            MessageDto messageFromWrapperDto = messagesWrapperDto.getMessages().get(i);

            assertDeepEquals(messageDto, messageFromWrapperDto);
        }
    }

    @Test
    void loadMessagesWithSameAuthorIdAndRecipientId() {
        messagesFilterDto.setRecipientId(author.getId());

        final List<String> messagess = waitForBadRequest(chatEndpointWorker.loadMessages(messagesFilterDto), ErrorCodesDto.VALIDATION);

        Assertions.assertEquals(1, messagess.size());
        Assertions.assertEquals(LOAD_MESSAGE_ERROR_AUTHORID_IS_EQUAL_TO_RECIPIENTID, messagess.get(0));
    }

    @Test
    void loadMessagesCountShouldBeCorrect() {
        CompletableFuture<MessagesWrapperDto> completableFuture = chatEndpointWorker.loadMessages(messagesFilterDto);

        MessagesWrapperDto messagesWrapperDto = waitForSuccess(completableFuture);

        List<Message> messages;

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            MessageDao messageDao = daoProvider.getMessageDao();
            messages = messageDao.loadMessagesByAuthorIdAndRecipientId(author.getId(), recipient1.getId(), 0, 5);
        }

        Assertions.assertEquals(messages.size(), messagesWrapperDto.getMessages().size());
    }

    @Test
    void loadMessagesWithNullAuthorId() {
        messagesFilterDto.setAuthorId(null);

        final List<String> messagess = waitForBadRequest(chatEndpointWorker.loadMessages(messagesFilterDto), ErrorCodesDto.VALIDATION);

        Assertions.assertEquals(1, messagess.size());
        Assertions.assertEquals(LOAD_MESSAGE_AUTHOR_ID_CANNOT_BE_NULL_ERROR_MESSAGE, messagess.get(0));
    }

    @Test
    void loadMessagesWithNullRecipientId() {
        messagesFilterDto.setRecipientId(null);

        final List<String> messagess = waitForBadRequest(chatEndpointWorker.loadMessages(messagesFilterDto), ErrorCodesDto.VALIDATION);

        Assertions.assertEquals(1, messagess.size());
        Assertions.assertEquals(LOAD_MESSAGE_RECIPIENT_ID_CANNOT_BE_NULL_ERROR_MESSAGE, messagess.get(0));
    }

    @Test
    void loadMessagesWithNegativeStart() {
        messagesFilterDto.setStart(-1);

        final List<String> messagess = waitForBadRequest(chatEndpointWorker.loadMessages(messagesFilterDto), ErrorCodesDto.VALIDATION);

        Assertions.assertEquals(1, messagess.size());
        Assertions.assertEquals(LOAD_MESSAGE_START_ERROR_MESSAGE, messagess.get(0));
    }

    @Test
    void loadMessagesWithNegativeOffset() {
        messagesFilterDto.setOffset(-1);

        final List<String> messagess = waitForBadRequest(chatEndpointWorker.loadMessages(messagesFilterDto), ErrorCodesDto.VALIDATION);

        Assertions.assertEquals(1, messagess.size());
        Assertions.assertEquals(LOAD_MESSAGE_OFFSET_ERROR_MESSAGE, messagess.get(0));
    }

    @Test
    void loadMessagesWithZeroOffset() {
        messagesFilterDto.setOffset(0);

        final List<String> messagess = waitForBadRequest(chatEndpointWorker.loadMessages(messagesFilterDto), ErrorCodesDto.VALIDATION);

        Assertions.assertEquals(1, messagess.size());
        Assertions.assertEquals(LOAD_MESSAGE_OFFSET_ERROR_MESSAGE, messagess.get(0));
    }

    @Test
    void loadMessagesMultipleTimesTest() {
        loadMessagesByAuthorIdAndRecipientId();

        messagesFilterDto.setRecipientId(recipient.getId());
        CompletableFuture<MessagesWrapperDto> completableFuture = chatEndpointWorker.loadMessages(messagesFilterDto);

        MessagesWrapperDto messagesWrapperDto = waitForSuccess(completableFuture);

        List<Message> messages;
        List<MessageDto> messageDtos;

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            MessageDao messageDao = daoProvider.getMessageDao();
            messages = messageDao.loadMessagesByAuthorIdAndRecipientId(author.getId(), recipient.getId(), 0, 5);
            messageDtos = MessageMapper.instance.mapListOfMessagesToDtoMessages(messages);
        }

        for (int i = 0; i < messageDtos.size(); i++) {
            MessageDto messageDto = messageDtos.get(i);
            MessageDto messageFromWrapperDto = messagesWrapperDto.getMessages().get(i);

            assertDeepEquals(messageDto, messageFromWrapperDto);
        }
    }

    private void addMessages() {
        for (int i = 0; i < 5; i++) {
            if (i > 2) {
                MessageDaoTests.createMessageAndSaveItToDb(daoProviderFactory, author, recipient1);
            }
            else {
                MessageDaoTests.createMessageAndSaveItToDb(daoProviderFactory, author, recipient);
            }
        }
    }

    @Override
    protected void assertDeepEquals(MessageDto expected, MessageDto actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getAuthor().getId(), actual.getAuthor().getId());
        Assertions.assertEquals(expected.getRecipient().getId(), actual.getRecipient().getId());
        Assertions.assertEquals(expected.getContent(), actual.getContent());
        Assertions.assertEquals(expected.getSentAt(), actual.getSentAt());
    }
}
