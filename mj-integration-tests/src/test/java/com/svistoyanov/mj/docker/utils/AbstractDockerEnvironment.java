package com.svistoyanov.mj.docker.utils;

import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class AbstractDockerEnvironment {

    public static final    String CONTAINER_VERSION = "junit.extension.cs.integration_test.environment.containers_version";
    protected static final Logger logger            = getLogger(AbstractDockerEnvironment.class);

    protected final String inhouseContainersVersion;

    public AbstractDockerEnvironment(EnvironmentConfig configProvider) {
        inhouseContainersVersion = configProvider.getValue(CONTAINER_VERSION, "latest");
    }

    protected String require(EnvironmentConfig configProvider, String itemName) {
        return configProvider.getValue(itemName).orElseThrow(() -> new RuntimeException(itemName + " is required!"));
    }
}
