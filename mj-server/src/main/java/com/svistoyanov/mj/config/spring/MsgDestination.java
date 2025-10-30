package com.svistoyanov.mj.config.spring;

public enum MsgDestination {
    ADMIN_PROCESSING("administration-input-queue"),
    CHAT_PROCESSING("chat-input-queue");

    private String destinationName;

    MsgDestination(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getDestinationName() {
        return destinationName;
    }
}
