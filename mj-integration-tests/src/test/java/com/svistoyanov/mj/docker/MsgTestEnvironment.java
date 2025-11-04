package com.svistoyanov.mj.docker;

import com.svistoyanov.mj.DaoProviderFactory;
import com.svistoyanov.mj.api.EndpointRegistry;
import com.svistoyanov.mj.docker.utils.IntegrationTestEnvironment;

public interface MsgTestEnvironment extends IntegrationTestEnvironment {
    EndpointRegistry getEndpointRegistry();

    DaoProviderFactory getDaoProviderFactory();

}
