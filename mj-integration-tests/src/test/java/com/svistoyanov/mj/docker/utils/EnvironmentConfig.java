package com.svistoyanov.mj.docker.utils;

import java.util.Optional;

public interface EnvironmentConfig {

    Optional<String> getValue(String configurationKey);

    default String getValue(String configurationKey, String defaultValue) {
        return getValue(configurationKey).orElse(defaultValue);
    }
}
