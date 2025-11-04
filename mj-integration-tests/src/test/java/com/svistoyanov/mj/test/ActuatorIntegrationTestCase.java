package com.svistoyanov.mj.test;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ActuatorIntegrationTestCase extends AbstractIntegrationTestCase {
    protected static final Logger logger = LoggerFactory.getLogger(ActuatorIntegrationTestCase.class);

    @Test
    void healthEnabledTestCase() {
        assertSuccess(actuatorEndpoint("/health"));
    }

    @Test
    void infoEnabledTestCase() {
        assertSuccess(actuatorEndpoint("/info"));
    }

    @Test
    void logfileEnabledTestCase() {
        assertSuccess(actuatorEndpoint("/logfile"));
    }

    @Test
    void metricsEnabledTestCase() {
        assertSuccess(actuatorEndpoint("/prometheus"));
    }
}
