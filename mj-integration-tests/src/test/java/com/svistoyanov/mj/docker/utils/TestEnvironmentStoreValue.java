package com.svistoyanov.mj.docker.utils;

import org.junit.jupiter.api.extension.ExtensionContext;

public record TestEnvironmentStoreValue(IntegrationTestEnvironment environment) implements ExtensionContext.Store.CloseableResource, AutoCloseable {

    @Override
    public void close() {
        environment.shutdown();
    }
}
