package com.svistoyanov.mj.docker;

public interface MsgContainerConstants {
    interface Messenger {
        String IMAGE_NAME = "com.svistoyanov.mj/messen-j";
        int    HTTP_PORT  = 8080;
        int    DEBUG_PORT = 8787;
    }

    interface Artemis {
        String IMAGE_NAME   = "com.svistoyanov.mj/messen-j-broker";
        int    JMS_TCP_PORT = 61616;
    }
}
