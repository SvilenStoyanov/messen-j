package com.svistoyanov.mj.api.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DeserializationProcessor {

    public static final DeserializationProcessor instance = new DeserializationProcessor();

    private final ObjectMapper objectMapper = ObjectMapperSingleton.instance.getObjectMapper();

    private final Map<String, Class<?>> javaClassCache = new HashMap<>();

    public <T> T deserialize(String serializedEntity) throws Exception {
        if (serializedEntity == null) {
            return null;
        }
        final ObjectNode classNode = objectMapper.readValue(serializedEntity, ObjectNode.class);
        return jsonTreeDeserialize(classNode);
    }

    private <T> T jsonTreeDeserialize(ObjectNode classNode) throws IOException, ClassNotFoundException {
        final JsonNode classJsonNode = classNode.get("@class");

        if (classJsonNode == null) {
            throw new IllegalArgumentException("Missing type qualifier!");
        }

        final String javaType = classJsonNode.textValue();

        Class<?> javaClazz = javaClassCache.get(javaType);
        if (javaClazz == null) {
            javaClazz = Class.forName(javaType);
            javaClassCache.put(javaType, javaClazz);
        }

        return (T) objectMapper.treeToValue(classNode, javaClazz);
    }
}
