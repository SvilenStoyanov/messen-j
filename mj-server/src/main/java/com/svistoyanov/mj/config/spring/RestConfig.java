package com.svistoyanov.mj.config.spring;

import com.svistoyanov.mj.api.utils.ObjectMapperSingleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;

@Configuration
public class RestConfig {

    @Bean
    public BuildInfoContributor buildInfoContributor() throws IOException {
        return new BuildInfoContributor();
    }

    @Bean
    public HttpMessageConverter<Object> jsonHttpMessageConverter() {
        final var converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(ObjectMapperSingleton.instance.getObjectMapper());
        return converter;
    }
}
