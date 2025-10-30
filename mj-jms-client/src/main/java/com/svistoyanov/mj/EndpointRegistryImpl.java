package com.svistoyanov.mj;

import com.svistoyanov.mj.api.AdministrationEndpoint;
import com.svistoyanov.mj.api.ChatEndpoint;
import com.svistoyanov.mj.api.EndpointRegistry;
import com.svistoyanov.mj.api.constants.DestinationsConstants;
import com.svistoyanov.mj.jms.OperationInvoker;
import com.svistoyanov.mj.jms.OperationSender;
import com.svistoyanov.mj.jms.ResponseReceiver;
import com.svistoyanov.mj.jms.endpoints.AdministrationEndpointImpl;
import com.svistoyanov.mj.jms.endpoints.ChatEndpointImpl;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Destination;
import jakarta.jms.JMSContext;
import org.slf4j.Logger;

import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class EndpointRegistryImpl extends AbstractEndpointRegistry implements EndpointRegistry {

    private static final Logger logger = getLogger(EndpointRegistryImpl.class);

    protected final ConnectionFactory connectionFactory;
    private final ResponseReceiver responseReceiver;

    public EndpointRegistryImpl(ConnectionFactory jmsContextFactory, String clientId) {
        this.connectionFactory = jmsContextFactory;

        try (JMSContext context = jmsContextFactory.createContext()) {
            Destination chatDestination = context.createQueue(DestinationsConstants.CHAT_DESTINATION);
            Destination administrationDestination = context.createQueue(DestinationsConstants.ADMINISTRATION_DESTINATION);

            Destination responseDestination = context.createQueue("response-" + clientId);

            responseReceiver = new ResponseReceiver(responseDestination, connectionFactory);
            //da dokopva toq map

            OperationSender operationChatSender = new OperationSender(connectionFactory, chatDestination, clientId,
                    responseDestination);
            OperationSender operationAdminSender = new OperationSender(connectionFactory, administrationDestination, clientId,
                    responseDestination);

            OperationInvoker operationChatInvoker = new OperationInvoker(operationChatSender, responseReceiver);
            OperationInvoker operationAdminInvoker = new OperationInvoker(operationAdminSender, responseReceiver);

            registerEndPoint(ChatEndpoint.class, new ChatEndpointImpl(operationChatInvoker));
            registerEndPoint(AdministrationEndpoint.class, new AdministrationEndpointImpl(operationAdminInvoker));
        }
    }

    @Override
    public AdministrationEndpoint getAdministrationEndpoint() {
        return getEndpoint(AdministrationEndpoint.class);
    }

    @Override
    public ChatEndpoint getChatEndpoint() {
        return getEndpoint(ChatEndpoint.class);
    }

    @Override
    public void close() throws IOException {
        super.close();
        responseReceiver.close();
    }
}
