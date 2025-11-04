package com.svistoyanov.mj.docker.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.images.AbstractImagePullPolicy;
import org.testcontainers.images.ImageData;
import org.testcontainers.utility.DockerImageName;

public class MjImagePullPolicy extends AbstractImagePullPolicy {

    private static final Logger logger = LoggerFactory.getLogger(MjImagePullPolicy.class);

    private final String  networkAlias;
    private final boolean pullImage;

    public MjImagePullPolicy(String networkAlias, boolean pullImage) {
        this.networkAlias = networkAlias;
        this.pullImage = pullImage;
    }

    @Override
    protected boolean shouldPullCached(DockerImageName dockerImageName, ImageData imageData) {
        logger.info("Pulling image with network alias {}: {}", networkAlias, pullImage ? "Yes" : "No");
        return pullImage;
    }
}
