package com.svistoyanov.mj.api;

import com.brandmaker.core.app.api.v1.dtos.message.MessageDto;
import com.brandmaker.core.app.api.v1.dtos.message.MessageRequestDto;
import com.brandmaker.core.app.api.v1.dtos.message.MessagesFilterDto;
import com.brandmaker.core.app.api.v1.dtos.message.MessagesWrapperDto;

import java.util.concurrent.CompletableFuture;

public interface ChatEndpoint extends Endpoint
{
    CompletableFuture<MessageDto> sendMessage(MessageRequestDto message);
    CompletableFuture<MessagesWrapperDto> loadMessages(MessagesFilterDto filter);

}
