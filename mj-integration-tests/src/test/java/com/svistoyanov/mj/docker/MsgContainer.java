package com.svistoyanov.mj.docker;

import com.svistoyanov.mj.docker.utils.AbstractMjContainer;
import com.svistoyanov.mj.docker.utils.ContainerUtils;
import com.svistoyanov.mj.docker.utils.ContainerConstants;
import com.svistoyanov.mj.docker.utils.Mysql8Container;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;

public class MsgContainer extends AbstractMjContainer {
    public MsgContainer(String containerVersion, Network network, Mysql8Container msgMysqlContainer,
            MsgArtemisContainer msgArtemisContainer) {
        super(containerVersion, "messenger", network);
        container = new GenericContainer<>(
                MsgContainerConstants.Messenger.IMAGE_NAME + ":" + containerVersion
        )
                .withEnv("DEBUG", "true")
                .withEnv("JAVA_OPTS", "")
                .withEnv("PROFILE", "dev")
                .withEnv("DB_URL", ContainerUtils.createJdbcUrl(msgMysqlContainer.getNetworkAlias(), ContainerConstants.Mysql8.DB_PORT, msgMysqlContainer.getDbName()))
                .withEnv("DB_USER", msgMysqlContainer.getUser())
                .withEnv("DB_PASSWORD", msgMysqlContainer.getUserPassword())
                .withEnv("BROKER_URL", ContainerUtils.createBrokerUrl(msgArtemisContainer.getNetworkAlias(), MsgContainerConstants.Artemis.JMS_TCP_PORT))
                .withEnv("BROKER_USERNAME", msgArtemisContainer.getUser())
                .withEnv("BROKER_PASSWORD", msgArtemisContainer.getPassword())
                .withExposedPorts(
                        MsgContainerConstants.Messenger.DEBUG_PORT,
                        MsgContainerConstants.Messenger.HTTP_PORT)
                .withLogConsumer(createLogConsumer("containers.msg"))
                .withNetwork(network);
        //        container.setPortBindings(Arrays.asList("8787:8787"));
    }

    @Override
    public Integer getMappedPort() {
        return container.getMappedPort(MsgContainerConstants.Messenger.HTTP_PORT);
    }

}
