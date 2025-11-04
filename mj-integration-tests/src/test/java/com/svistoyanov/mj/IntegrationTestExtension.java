package com.svistoyanov.mj;

import com.svistoyanov.mj.api.EndpointRegistry;
import com.svistoyanov.mj.docker.DockerContainersTestEnvironment;
import com.svistoyanov.mj.docker.MsgTestEnvironment;
import com.svistoyanov.mj.docker.utils.AbstractIntegrationTestExtension;
import com.svistoyanov.mj.docker.utils.EnvironmentConfig;
import com.svistoyanov.mj.docker.utils.IntegrationTestEnvironment;
import com.svistoyanov.mj.docker.utils.IntegrationTestValueDescriptor;
import com.svistoyanov.mj.injector.BaseMsgUri;
import com.svistoyanov.mj.injector.MsgDaoProviderFactory;
import com.svistoyanov.mj.injector.MsgEndpointRegistry;

import java.util.List;
import java.util.function.Function;

public class IntegrationTestExtension extends AbstractIntegrationTestExtension {

    private static final Function<EnvironmentConfig, IntegrationTestEnvironment> ENVIRONMENT_SUPPLIER = DockerContainersTestEnvironment::new;

    private static final List<IntegrationTestValueDescriptor<?, ?>> TEST_VALUE_DESCRIPTORS = List.of(
            new IntegrationTestValueDescriptor<>(BaseMsgUri.class, String.class, (env, ann) -> env.getBaseRestUri()),
            new IntegrationTestValueDescriptor<>(MsgEndpointRegistry.class, EndpointRegistry.class, (env, ann) -> ((MsgTestEnvironment) env).getEndpointRegistry()),
            new IntegrationTestValueDescriptor<>(MsgDaoProviderFactory.class, DaoProviderFactory.class, (env, ann) -> ((MsgTestEnvironment) env).getDaoProviderFactory())
    );

    protected IntegrationTestExtension() {
        super(ENVIRONMENT_SUPPLIER);
    }

    @Override
    public List<IntegrationTestValueDescriptor<?, ?>> getTestValueDescriptors() {
        return TEST_VALUE_DESCRIPTORS;
    }
}
