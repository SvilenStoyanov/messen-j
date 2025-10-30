package com.svistoyanov.mj.api.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.svistoyanov.mj.api.dto.AbstractDto;

import java.util.ArrayList;
import java.util.List;

public class MessagesWrapperDto extends AbstractDto {
    @JsonProperty("messages")
    private List<MessageDto> messages = new ArrayList<>();

    public MessagesWrapperDto() {
    }

    public MessagesWrapperDto(List<MessageDto> messages) {
        this.messages = messages;
    }

    public List<MessageDto> getMessages() {
        return new ArrayList<>(messages);
    }

    public void setMessages(List<MessageDto> messages) {
        this.messages = messages;
    }

}
