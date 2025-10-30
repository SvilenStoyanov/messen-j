package com.svistoyanov.mj.config.spring;

import org.springframework.boot.actuate.info.MapInfoContributor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public class BuildInfoContributor extends MapInfoContributor {
    private static final String BUILD_PROPERTIES_FILE = "/build.properties";

    private static Map<String, Object> readBuildProperties() throws IOException {
        try (InputStream is = BuildInfoContributor.class.getResourceAsStream(BUILD_PROPERTIES_FILE)) {
            final Properties productInfoProperties = new Properties();
            productInfoProperties.load(is);

            final TreeMap<String, Object> buildProperties = new TreeMap<>();
            for (Map.Entry<Object, Object> buildProperty : productInfoProperties.entrySet()) {
                buildProperties.put(buildProperty.getKey().toString(), buildProperty.getValue());
            }
            return buildProperties;
        }
    }

    public BuildInfoContributor() throws IOException {
        super(readBuildProperties());
    }
}
