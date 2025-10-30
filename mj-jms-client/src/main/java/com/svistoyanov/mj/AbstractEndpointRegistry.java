package com.svistoyanov.mj;

import com.svistoyanov.mj.api.Endpoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractEndpointRegistry implements AutoCloseable {

    private final Map<Class<? extends Endpoint>, Endpoint> endpoints = new HashMap<>();

    public <T extends Endpoint> T getEndpoint(Class<T> endpointType) {
        return (T) endpoints.get(endpointType);
    }

    protected synchronized void registerEndPoint(Class<? extends Endpoint> endpointType, Endpoint client) {
        final Endpoint old = endpoints.put(endpointType, client);
        if (old != null) {
            old.close();
        }
    }

    @Override
    public void close() throws IOException {
        endpoints.values().forEach(Endpoint::close);
    }
}
