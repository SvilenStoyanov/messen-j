package com.svistoyanov.mj.docker.utils;

import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Optional;

public class EnvConfigProviderImpl implements EnvironmentConfig {

    private final ExtensionContext ctx;

    public EnvConfigProviderImpl(ExtensionContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public Optional<String> getValue(String configurationKey) {
        return ctx.getConfigurationParameter(configurationKey);
    }
}
