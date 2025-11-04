package com.svistoyanov.mj.docker.utils;

public interface IntegrationTestEnvironment {

    void bootstrap();

    void resetDatabaseData();

    void shutdown();

    String getBaseRestUri();
}
