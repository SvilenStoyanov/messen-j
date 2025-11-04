package com.svistoyanov.mj.docker.utils;

import org.testcontainers.containers.GenericContainer;

public interface MjContainer {

    GenericContainer<?> getGenericContainer();

    String getNetworkAlias();

    String getContainerIpAddress();

    Integer getMappedPort();

    void logInfo();

    void logInfo(String name, String offset);

    String execInContainer(String... command);

    default void init() {

    }
}
