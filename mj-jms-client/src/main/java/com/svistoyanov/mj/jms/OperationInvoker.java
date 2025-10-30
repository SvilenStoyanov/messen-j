package com.svistoyanov.mj.jms;

import com.svistoyanov.mj.api.dto.AbstractDto;

import java.util.concurrent.CompletableFuture;

public class OperationInvoker {
    private OperationSender  operationSender;
    private ResponseReceiver responseReceiver;

    public OperationInvoker(OperationSender operationSender,
            ResponseReceiver responseReceiver) {
        this.operationSender = operationSender;
        this.responseReceiver = responseReceiver;
    }

    public <T> CompletableFuture<T> invoke(AbstractDto abstractDto, String operationName) {
        final CompletableFuture<T> cf = new CompletableFuture<>();
        String jmsCorrelationID = operationSender.send(abstractDto, operationName);
        responseReceiver.attachCompletableFuture(jmsCorrelationID, cf);
        return cf;
    }
}
