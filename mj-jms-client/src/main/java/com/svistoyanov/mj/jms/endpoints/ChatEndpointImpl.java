package com.svistoyanov.mj.jms.endpoints;

import com.svistoyanov.mj.api.ChatEndpoint;
import com.svistoyanov.mj.api.constants.OperationsNames;
import com.svistoyanov.mj.api.dto.message.MessageDto;
import com.svistoyanov.mj.api.dto.message.MessageRequestDto;
import com.svistoyanov.mj.api.dto.message.MessagesFilterDto;
import com.svistoyanov.mj.api.dto.message.MessagesWrapperDto;
import com.svistoyanov.mj.jms.OperationInvoker;

import java.util.concurrent.CompletableFuture;

public class ChatEndpointImpl extends AbstractEndpointImpl implements ChatEndpoint {

    public ChatEndpointImpl(OperationInvoker operationInvoker) {
        super(operationInvoker);
    }

    @Override
    public CompletableFuture<MessageDto> sendMessage(MessageRequestDto message) {
        return operationInvoker.invoke(message, OperationsNames.SEND_MESSAGE);
    }

    @Override
    public CompletableFuture<MessagesWrapperDto> loadMessages(MessagesFilterDto filter) {
        return operationInvoker.invoke(filter, OperationsNames.LOAD_MESSAGES);
    }

    @Override
    public void close() {

    }
}
