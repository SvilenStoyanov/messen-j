package com.svistoyanov.mj.api.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class ObjectMapperSingleton {

    public static final ObjectMapperSingleton instance = new ObjectMapperSingleton();
    private final       ObjectMapper          objectMapper;

    private ObjectMapperSingleton() {
        this.objectMapper = new ObjectMapper();
        configureMapper(this.objectMapper);
    }

    private void configureMapper(ObjectMapper objectMapper) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        objectMapper
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .setDateFormat(dateFormat)
                .registerModule(new JavaTimeModule());
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

}