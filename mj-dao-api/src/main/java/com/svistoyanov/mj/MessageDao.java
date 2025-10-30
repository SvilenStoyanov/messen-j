package com.svistoyanov.mj;

import com.svistoyanov.mj.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageDao extends CrudDao<Message> {

    Message loadByAuthorId(UUID authorId);

    List<Message> loadMessagesByAuthorId(UUID authorId, int page, int size);

    List<Message> loadMessagesByAuthorIdAndRecipientId(UUID authorId, UUID recipientId, Integer page, Integer size);
}
