package com.svistoyanov.mj.docker;

import com.svistoyanov.mj.docker.utils.AbstractMjContainer;
import com.svistoyanov.mj.docker.utils.ContainerUtils;
import jakarta.jms.ConnectionFactory;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;

public class MsgArtemisContainer extends AbstractMjContainer {

    public MsgArtemisContainer(String containerVersion, Network network, String user, String password) {
        super(containerVersion, "msg-broker", network);
        this.container = new GenericContainer<>(
                MsgContainerConstants.Artemis.IMAGE_NAME + ":" + containerVersion
        )
                .withEnv("CLEAN_ON_START", "true")
                .withEnv("JVM_MEMORY", "-Xms1g -Xmx4g")
                .withEnv("JMS_USER_NAME", user)
                .withEnv("JMS_USER_PASS", password)
                .withNetworkAliases(getNetworkAlias())
                .withExposedPorts(MsgContainerConstants.Artemis.JMS_TCP_PORT)
                .withLogConsumer(createLogConsumer("containers.artemis"))
                .withNetwork(network);
    }

    public String getUser() {
        return getEnvValue("JMS_USER_NAME");
    }

    public String getPassword() {
        return getEnvValue("JMS_USER_PASS");
    }

    @Override
    public Integer getMappedPort() {
        return container.getMappedPort(MsgContainerConstants.Artemis.JMS_TCP_PORT);
    }

    public ConnectionFactory createConnectionFactory() {
        return new ActiveMQConnectionFactory(ContainerUtils.createBrokerUrl(getContainerIpAddress(), getMappedPort()), getUser(), getPassword());
    }
}
