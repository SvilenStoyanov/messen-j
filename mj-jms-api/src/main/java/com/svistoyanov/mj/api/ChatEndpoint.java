package com.svistoyanov.mj.api;

import com.svistoyanov.mj.api.dto.message.MessageDto;
import com.svistoyanov.mj.api.dto.message.MessageRequestDto;
import com.svistoyanov.mj.api.dto.message.MessagesFilterDto;
import com.svistoyanov.mj.api.dto.message.MessagesWrapperDto;

import java.util.concurrent.CompletableFuture;

public interface ChatEndpoint extends Endpoint {

    CompletableFuture<MessageDto> sendMessage(MessageRequestDto message);

    CompletableFuture<MessagesWrapperDto> loadMessages(MessagesFilterDto filter);

}
