package com.svistoyanov.mj.jms.endpoints;

import com.svistoyanov.mj.jms.OperationInvoker;

public class AbstractEndpointImpl {

    protected final OperationInvoker operationInvoker;

    public AbstractEndpointImpl(OperationInvoker operationInvoker) {
        this.operationInvoker = operationInvoker;
    }
}
