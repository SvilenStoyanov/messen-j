package com.svistoyanov.mj;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "msg.jms")
public class JmsProperties {

    @NotNull
    @NotBlank
    private String brokerUrl;

    @NotNull
    @NotBlank
    private String username;

    @NotNull
    @NotBlank
    private String password;

    @NotNull
    @Positive
    private Integer minConsumers = 1;

    @NotNull
    @Positive
    private Integer maxConsumers = 1;

    public String getBrokerUrl() {
        return brokerUrl;
    }

    public void setBrokerUrl(String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getMinConsumers() {
        return minConsumers;
    }

    public void setMinConsumers(Integer minConsumers) {
        this.minConsumers = minConsumers;
    }

    public Integer getMaxConsumers() {
        return maxConsumers;
    }

    public void setMaxConsumers(Integer maxConsumers) {
        this.maxConsumers = maxConsumers;
    }
}

