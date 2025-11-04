package com.svistoyanov.mj;

import com.svistoyanov.mj.api.EndpointRegistry;
import com.svistoyanov.mj.api.dto.message.MessageRequestDto;
import com.svistoyanov.mj.api.dto.message.MessagesFilterDto;
import com.svistoyanov.mj.api.dto.user.SignInDto;
import com.svistoyanov.mj.api.dto.user.SignUpDto;
import jakarta.jms.Connection;
import jakarta.jms.JMSException;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import java.util.UUID;

public class Main {
    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("tcp://0.0.0.0:61616", "guest", "guest");

        EndpointRegistry endpointRegistry = new EndpointRegistryImpl(connectionFactory, "martin");
        //1.instancirane na takova registry i injectvane v testovete

        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("chat-input-queue");
        Queue queue1 = session.createQueue("message-input-queue");

        MessageRequestDto messageRequestDto = new MessageRequestDto();
        messageRequestDto.setContent("SOMECONTENT");
        messageRequestDto.setAuthorId(UUID.randomUUID());
        messageRequestDto.setRecipientId(UUID.randomUUID());

        endpointRegistry.getChatEndpoint().sendMessage(messageRequestDto);
        endpointRegistry.getChatEndpoint().loadMessages(new MessagesFilterDto());
        endpointRegistry.getAdministrationEndpoint().signIn(new SignInDto());
        endpointRegistry.getAdministrationEndpoint().signUp(new SignUpDto());

        //        String clientId = UUID.randomUUID().toString();
        //        String operationName = "sendMessage";
        //
        //
        //
        //        OperationSender operationSender = new OperationSender(connectionFactory,queue, clientId,queue1);
        //        operationSender.send(messageRequestDto,operationName);

    }

}
