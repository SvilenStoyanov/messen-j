package com.svistoyanov.mj.config.jms;

import com.svistoyanov.mj.JmsProperties;
import com.svistoyanov.mj.api.AdministrationEndpoint;
import com.svistoyanov.mj.api.ChatEndpoint;
import com.svistoyanov.mj.config.spring.MsgDestination;
import com.svistoyanov.mj.config.spring.RequestListener;
import com.svistoyanov.mj.jms.ResponseSender;
import jakarta.jms.ConnectionFactory;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

@Configuration
public class JmsConfiguration {

    @Bean
    public ConnectionFactory connectionFactory(JmsProperties jmsProperties) {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(jmsProperties.getBrokerUrl(), jmsProperties.getUsername(), jmsProperties.getPassword());
        return connectionFactory;
    }

    @Bean
    public DefaultMessageListenerContainer adminRouterListenerContainer(
            ConnectionFactory connectionFactory,
            ResponseSender responseSender,
            AdministrationEndpoint administrationEndpointWorker
    ) {
        final DefaultMessageListenerContainer listenerContainer = JmsConfigUtils.createListenerContainer(connectionFactory);
        listenerContainer.setDestinationName(MsgDestination.ADMIN_PROCESSING.getDestinationName());
        listenerContainer.setMessageListener(new RequestListener(responseSender, administrationEndpointWorker));
        return listenerContainer;
    }

    @Bean
    public DefaultMessageListenerContainer chatRouterListenerContainer(
            ConnectionFactory connectionFactory,
            ResponseSender responseSender,
            ChatEndpoint chatEndpointWorker
    ) {
        final DefaultMessageListenerContainer listenerContainer = JmsConfigUtils.createListenerContainer(connectionFactory);
        listenerContainer.setDestinationName(MsgDestination.CHAT_PROCESSING.getDestinationName());
        listenerContainer.setMessageListener(new RequestListener(responseSender, chatEndpointWorker));
        return listenerContainer;
    }

    @Bean
    public ResponseSender responseSender(ConnectionFactory connectionFactory) {
        return new ResponseSender(connectionFactory);
    }
}
