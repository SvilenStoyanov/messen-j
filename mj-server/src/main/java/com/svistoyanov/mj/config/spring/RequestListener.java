package com.svistoyanov.mj.config.spring;

import com.svistoyanov.mj.api.Endpoint;
import com.svistoyanov.mj.api.constants.MsgConstants;
import com.svistoyanov.mj.api.dto.AbstractDto;
import com.svistoyanov.mj.api.dto.ErrorDto;
import com.svistoyanov.mj.api.exception.MessengerException;
import com.svistoyanov.mj.api.utils.DeserializationProcessor;
import com.svistoyanov.mj.jms.ResponseSender;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

public class RequestListener implements MessageListener {

    private static final Logger         logger = LoggerFactory.getLogger(RequestListener.class);
    private final        ResponseSender responseSender;
    private final        Endpoint       endpointWorker;

    public RequestListener(ResponseSender responseSender, Endpoint endpointWorker) {
        this.responseSender = responseSender;
        this.endpointWorker = endpointWorker;
    }

    @Override
    public void onMessage(Message message) {
        try {
            message.acknowledge();
            logger.info("Received Message {}", message.getJMSCorrelationID());

            if (message instanceof TextMessage textMessage) {
                String operationName = message.getStringProperty(MsgConstants.OPERATION_NAME);

                AbstractDto abstractDto = DeserializationProcessor.instance.deserialize(textMessage.getText());

                sendResponseToWorker(operationName, abstractDto, message);
            }
            else {
                throw new JMSException("Message is not the correct TextMessage format.");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //sendResponseToWorker или navigateResponseToWorker
    private void sendResponseToWorker(String operationName, AbstractDto abstractDto, Message message)
            throws InvocationTargetException, IllegalAccessException {
        Method correctMethod = findCorrectMethod(operationName);
        CompletableFuture<?> completableFuture = (CompletableFuture<?>) correctMethod.invoke(endpointWorker, abstractDto);

        getResultFromWorkerAndCompleteTheFuture(completableFuture, message);
    }

    private void getResultFromWorkerAndCompleteTheFuture(CompletableFuture<?> completableFuture,
            Message message) {
        completableFuture.whenComplete((result, throwable) -> {
            if (throwable == null) {
                try {
                    //gets the result from the worker and send it back to the ResponseDestination queue
                    responseSender.sendResponse((AbstractDto) result, message.getJMSReplyTo(), message.getJMSCorrelationID());
                }
                catch (JMSException e) {
                    e.printStackTrace();
                }
            }
            else {
                ErrorDto errorDto = ((MessengerException) throwable).getErrorDto();
                try {
                    responseSender.sendResponse(errorDto, message.getJMSReplyTo(), message.getJMSCorrelationID());
                }
                catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private Method findCorrectMethod(String operationName) {

        for (Method m : endpointWorker.getClass().getMethods()) {
            if (m.getName().equals(operationName)) {
                return m;
            }
        }
        throw new IllegalArgumentException("No such operation " + operationName);
    }

}
