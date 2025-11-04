package com.svistoyanov.mj.docker.utils;

import org.testcontainers.containers.Network;

public class Mysql8Container extends AbstractMjContainer {

    private static final String rootPassword = "q";

    public Mysql8Container(String dbName, String user, String userPassword, Network network) {
        this(ContainerConstants.Mysql8.VERSION, dbName, user, userPassword, "mysql8", network);
    }

    public Mysql8Container(String containerVersion, String dbName, String user, String userPassword, Network network) {
        this(containerVersion, dbName, user, userPassword, "mysql8", network);
    }

    public Mysql8Container(String containerVersion, String dbName, String user, String userPassword, String networkAlias, Network network) {
        super(containerVersion, networkAlias, network);
        this.container = createBasicContainer(ContainerConstants.Mysql8.IMAGE_NAME, containerVersion, network, false)
                .withEnv("DB_ROOT_PASSWORD", rootPassword)
                .withEnv("MYSQL_DATABASE", dbName)
                .withEnv("MYSQL_USER", user)
                .withEnv("MYSQL_PASSWORD", userPassword)
                .withExposedPorts(ContainerConstants.Mysql8.DB_PORT);
    }

    @Override
    public Integer getMappedPort() {
        return container.getMappedPort(ContainerConstants.Mysql8.DB_PORT);
    }

    public String getDbName() {
        return getEnvValue("MYSQL_DATABASE");
    }

    public String getUser() {
        return getEnvValue("MYSQL_USER");
    }

    public String getUserPassword() {
        return getEnvValue("MYSQL_PASSWORD");
    }

    public String getMariaNetworkConnectionUrl() {
        return ContainerUtils.createMariaDb8JdbcUrl(getNetworkAlias(), ContainerConstants.Mysql8.DB_PORT, getDbName());
    }

    public String getMysqlNetworkConnectionUrl() {
        return ContainerUtils.createMysql8JdbcUrl(getNetworkAlias(), ContainerConstants.Mysql8.DB_PORT, getDbName());
    }
}
