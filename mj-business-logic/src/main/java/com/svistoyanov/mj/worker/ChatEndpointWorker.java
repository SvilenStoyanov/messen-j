package com.svistoyanov.mj.worker;

import com.svistoyanov.mj.DaoProvider;
import com.svistoyanov.mj.DaoProviderFactory;
import com.svistoyanov.mj.MessageDao;
import com.svistoyanov.mj.api.ChatEndpoint;
import com.svistoyanov.mj.api.dto.message.MessageDto;
import com.svistoyanov.mj.api.dto.message.MessageRequestDto;
import com.svistoyanov.mj.api.dto.message.MessagesFilterDto;
import com.svistoyanov.mj.api.dto.message.MessagesWrapperDto;
import com.svistoyanov.mj.entity.Message;
import com.svistoyanov.mj.entity.User;
import com.svistoyanov.mj.mapper.MessageMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static com.svistoyanov.mj.constants.WorkerConstants.*;
import static com.svistoyanov.mj.exception.ExceptionUtils.*;

public class ChatEndpointWorker implements ChatEndpoint
{
    private final DaoProviderFactory daoProviderFactory;
    private final Validator validator;

    protected static final Logger logger = LoggerFactory.getLogger(ChatEndpointWorker.class);

    public ChatEndpointWorker(Validator validator, DaoProviderFactory daoProviderFactory)
    {
        this.validator = validator;
        this.daoProviderFactory = daoProviderFactory;
    }

    @Override
    public CompletableFuture<MessageDto> sendMessage(MessageRequestDto messageRequestDto)
    {
        //null check + unit tests
        if (messageRequestDto == null)
        {
            return CompletableFuture.failedFuture(createValidationError("Request can not be null."));
        }

        //Validate input (MessageRequestDto)
        if (messageRequestDto.getAuthorId() == null)
        {
            return CompletableFuture.failedFuture(createValidationError(SEND_MESSAGE_AUTHOR_ID_CANNOT_BE_NULL_ERROR_MESSAGE));
        }
        if (messageRequestDto.getRecipientId() == null)
        {
            return CompletableFuture.failedFuture(createValidationError(SEND_MESSAGE_RECIPIENT_ID_CANNOT_BE_NULL_ERROR_MESSAGE));
        }

        if (messageRequestDto.getContent() == null)
        {
            return CompletableFuture.failedFuture(createValidationError(SEND_MESSAGE_CONTENT_CANNOT_BE_NULL_ERROR_MESSAGE));
        }
        if (messageRequestDto.getContent().equals(""))
        {
            return CompletableFuture.failedFuture(createValidationError(SEND_MESSAGE_CONTENT_CANNOT_BE_BLANK_ERROR_MESSAGE));
        }

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider())
        {
            //da premestim proverkite sled userite.
            User authorFromMessage = daoProvider.getUserDao().loadById(messageRequestDto.getAuthorId());
            User recipientFromMessage = daoProvider.getUserDao().loadById(messageRequestDto.getRecipientId());

            if (authorFromMessage == null)
            {
                return CompletableFuture.failedFuture(createNotFoundError(SIGNIN_USER_NOTFOUND_ERROR_MESSAGE));
            }
            if (recipientFromMessage == null)
            {
                return CompletableFuture.failedFuture(createNotFoundError(SIGNIN_USER_NOTFOUND_ERROR_MESSAGE));
            }



            Message messageToBeSaved = MessageMapper.instance.mapMessage(messageRequestDto, authorFromMessage, recipientFromMessage);
            //validate the message before saving it
            Set<ConstraintViolation<Message>> violations = validator.validate(messageToBeSaved);
            if (!violations.isEmpty())
            {
                return CompletableFuture.failedFuture(createValidationError(SEND_MESSAGE_MESSAGE_INPUT_IS_INCORRECT_ERROR_MESSAGE));
            }

            daoProvider.getMessageDao().saveOrUpdate(messageToBeSaved);
            daoProvider.commit();

            MessageDto messageDto = MessageMapper.instance.mapMessageToDtoMessage(messageToBeSaved);

            return CompletableFuture.completedFuture(messageDto);

        } catch (IllegalArgumentException | ValidationException e)
        {
            logger.error(INVALID_REQUEST_ERROR_MESSAGE, e);
            return CompletableFuture.failedFuture(createValidationError(e.getMessage()));
        } catch (Throwable th)
        {
            return CompletableFuture.failedFuture(createInternalServerError(UNEXPECTED_ERROR_MESSAGE_TRY_AGAIN));
        }
    }

    @Override
    public CompletableFuture<MessagesWrapperDto> loadMessages(MessagesFilterDto filter)
    {
        //null check + unit tests
        if (filter == null)
        {
            return CompletableFuture.failedFuture(createValidationError("Request can not be null."));
        }

        if (filter.getAuthorId() == null)
        {
            return CompletableFuture.failedFuture(createValidationError(LOAD_MESSAGE_AUTHOR_ID_CANNOT_BE_NULL_ERROR_MESSAGE));
        }
        if (filter.getRecipientId() == null)
        {
            return CompletableFuture.failedFuture(createValidationError(LOAD_MESSAGE_RECIPIENT_ID_CANNOT_BE_NULL_ERROR_MESSAGE));
        }
        if (filter.getStart() < 0)
        {
            return CompletableFuture.failedFuture(createValidationError(LOAD_MESSAGE_START_ERROR_MESSAGE));
        }
        if (filter.getOffset() < 1)
        {
            return CompletableFuture.failedFuture(createValidationError(LOAD_MESSAGE_OFFSET_ERROR_MESSAGE));
        }
        if (filter.getAuthorId() == filter.getRecipientId())
        {
            return CompletableFuture.failedFuture(createValidationError(LOAD_MESSAGE_ERROR_AUTHORID_IS_EQUAL_TO_RECIPIENTID));
        }

        //Check if the author exist, if not then a NotFound exception must be thrown with a proper message.
        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider())
        {
            if (daoProvider.getUserDao().loadById(filter.getAuthorId()) == null)
            {
                return CompletableFuture.failedFuture(createNotFoundError(SIGNIN_USER_NOTFOUND_ERROR_MESSAGE));
            }

            MessageDao messageDao = daoProvider.getMessageDao();
            final List<Message> messages = messageDao.loadMessagesByAuthorIdAndRecipientId(
                    filter.getAuthorId(),
                    filter.getRecipientId(),
                    filter.getStart(),
                    filter.getOffset());

            List<MessageDto> messageDtos = MessageMapper.instance.mapListOfMessagesToDtoMessages(messages);

            MessagesWrapperDto messagesWrapperDto = new MessagesWrapperDto();
            messagesWrapperDto.setMessages(messageDtos);

            return CompletableFuture.completedFuture(messagesWrapperDto);
        } catch (IllegalArgumentException | ValidationException e)
        {
            logger.error(INVALID_REQUEST_ERROR_MESSAGE, e);
            return CompletableFuture.failedFuture(createValidationError(e.getMessage()));
        } catch (Throwable th)
        {
            return CompletableFuture.failedFuture(createInternalServerError(UNEXPECTED_ERROR_MESSAGE_TRY_AGAIN));
        }
    }

    @Override
    public void close()
    {

    }
}
