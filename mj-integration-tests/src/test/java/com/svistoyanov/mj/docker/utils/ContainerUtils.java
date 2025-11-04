package com.svistoyanov.mj.docker.utils;

import com.github.dockerjava.api.command.CreateVolumeResponse;
import org.slf4j.Logger;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ContainerUtils {

    public ContainerUtils() {
        // static only
    }

    public static void run(MjContainer... containers) {
        run(List.of(containers));
    }

    public static void run(Collection<MjContainer> containers) {
        try {
            final var genericContainers = containers
                    .stream()
                    .map(MjContainer::getGenericContainer)
                    .collect(Collectors.toList());
            Startables.deepStart(genericContainers).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void run(GenericContainer<?>... containers) {
        try {
            Startables.deepStart(containers).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String execInContainer(Logger logger, MjContainer container, String... command) {
        return execInContainer(logger, container.getGenericContainer(), command);
    }

    public static String execInContainer(Logger logger, GenericContainer<?> container, String... command) {
        try {
            final Container.ExecResult result = container.execInContainer(command);
            if (result.getExitCode() != 0) {
                logger.error("Failed to execute command {}", String.join(" ", command));
                logger.error("Error stream: {}", result.getStderr());
                logger.error("Output stream: {}", result.getStdout());
            }

            return result.getStdout();
        } catch (Throwable th) {
            throw new RuntimeException("Failed to execute command " + String.join(" ", command));
        }
    }

    public static String createBrokerUrl(String host, int port) {
        return "tcp://" + host + ":" + port + "?protocols=OPENWIRE";
    }

    public static String createJdbcUrl(String host, int port, String dbName) {
        return String.format("jdbc:mysql://%s:%s/%s", host, port, dbName);
    }

    public static String createMysql8JdbcUrl(String host, int port, String dbName) {
        return String.format("jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true&permitMysqlScheme", host, port, dbName);
    }

    public static String createMariaDb8JdbcUrl(String host, int port, String dbName) {
        return String.format("jdbc:mariadb://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true", host, port, dbName);
    }

    public static String createPostgresJdbcUrl(String host, int port, String dbName) {
        return String.format("jdbc:postgresql://%s:%s/%s", host, port, dbName);
    }

    public static CreateVolumeResponse createCifsVolume(String namePrefix, String device, String deviceOptions) {
        final var driverOptions = new HashMap<String, String>();
        driverOptions.put("type", "cifs");
        driverOptions.put("device", device);
        if (deviceOptions != null) {
            driverOptions.put("o", deviceOptions);
        }

        return DockerClientFactory.instance().client().createVolumeCmd()
                .withName(namePrefix + "_" + DockerClientFactory.SESSION_ID)
                // Set default labels so resource reaper will remove volumes after tests
                .withLabels(DockerClientFactory.DEFAULT_LABELS)
                .withDriver("local")
                .withDriverOpts(driverOptions)
                .exec();
    }

}
