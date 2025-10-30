package com.svistoyanov.mj.config.jms;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.Session;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

public class JmsConfigUtils {
    private JmsConfigUtils() {
        // Static only
    }

    public static DefaultMessageListenerContainer createListenerContainer(ConnectionFactory connectionFactory) {
        final DefaultMessageListenerContainer listenerContainer = new DefaultMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory);
        listenerContainer.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        return listenerContainer;
    }
}
