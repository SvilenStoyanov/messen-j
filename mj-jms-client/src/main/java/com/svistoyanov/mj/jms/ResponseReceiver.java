package com.svistoyanov.mj.jms;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.Destination;
import jakarta.jms.JMSConsumer;
import jakarta.jms.JMSContext;

import java.util.concurrent.CompletableFuture;

public class ResponseReceiver
{
        private final ResponseListener responseListener = new ResponseListener();
        private final JMSContext context;

    public ResponseReceiver(Destination destination, ConnectionFactory connectionFactory)
    {
        this.context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(destination);
        consumer.setMessageListener(responseListener);
    }

    public void attachCompletableFuture(String correlationId, CompletableFuture future)
    {
        responseListener.attachCompletableFuture(correlationId, future);
    }

    public void close (){
        context.close();
    }

}
