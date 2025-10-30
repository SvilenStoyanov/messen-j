package com.svistoyanov.mj.config.spring;

import com.svistoyanov.mj.DaoProviderFactory;
import com.svistoyanov.mj.api.AdministrationEndpoint;
import com.svistoyanov.mj.api.ChatEndpoint;
import com.svistoyanov.mj.worker.AdministrationEndpointWorker;
import com.svistoyanov.mj.worker.ChatEndpointWorker;
import jakarta.validation.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkerConfiguration {

    @Bean
    public AdministrationEndpoint administrationEndpointWorker(Validator validator,
            DaoProviderFactory daoProviderFactory) {
        return new AdministrationEndpointWorker(validator, daoProviderFactory);
    }

    @Bean
    public ChatEndpoint chatEndpointWorker(Validator validator, DaoProviderFactory daoProviderFactory) {
        return new ChatEndpointWorker(validator, daoProviderFactory);
    }
}
