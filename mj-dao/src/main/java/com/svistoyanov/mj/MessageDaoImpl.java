package com.svistoyanov.mj;

import com.svistoyanov.mj.entity.Message;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

public class MessageDaoImpl extends CrudDaoImpl<Message, MessageRepository> implements MessageDao {

    public MessageDaoImpl(MessageRepository repository) {
        super(repository);
    }

    @Override
    public Message loadByAuthorId(UUID authorId)
    {
        return this.repository.findMessageByAuthorId(authorId);
    }

    @Override
    public List<Message> loadMessagesByAuthorId(UUID authorId, int page, int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return this.repository.loadAllByAuthorId(authorId,pageable);
    }

    @Override
    public List<Message> loadMessagesByAuthorIdAndRecipientId(UUID authorId, UUID recipientId, Integer page, Integer size)
    {
        Pageable pageable = PageRequest.of(page, size,Sort.by("sentAt").descending());
        return this.repository.findAllByAuthorIdAndRecipientId(authorId,recipientId,pageable);
    }

}