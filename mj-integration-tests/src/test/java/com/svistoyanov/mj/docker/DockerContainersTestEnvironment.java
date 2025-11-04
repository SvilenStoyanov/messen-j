package com.svistoyanov.mj.docker;

import com.svistoyanov.mj.DaoProviderFactory;
import com.svistoyanov.mj.EndpointRegistryImpl;
import com.svistoyanov.mj.api.EndpointRegistry;
import com.svistoyanov.mj.docker.utils.AbstractDockerEnvironment;
import com.svistoyanov.mj.docker.utils.ContainerUtils;
import com.svistoyanov.mj.docker.utils.EnvironmentConfig;
import com.svistoyanov.mj.docker.utils.Mysql8Container;
import com.svistoyanov.mj.repo.impl.DaoProviderFactoryImpl;
import com.svistoyanov.mj.utils.HibernateUtils;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.MySQL8Dialect;
import org.testcontainers.containers.Network;

import java.util.Properties;

public class DockerContainersTestEnvironment extends AbstractDockerEnvironment implements MsgTestEnvironment {
    private static final String MSG_VERSION     = "junit.extension.msg.image.version";
    private static final String DATABASE_NAME   = "messenger";
    private static final String DATABASE_USER   = "db_user";
    private static final String DATABASE_PASS   = "db_pass";
    private static final String BROKER_USERNAME = "guest";
    private static final String BROKER_PASSWORD = "guest";

    private Mysql8Container     mysqlContainer;
    private MsgContainer        msgContainer;
    private MsgArtemisContainer msgArtemisContainer;

    private final String           msgImageVersion;
    private       EndpointRegistry endpointRegistry;

    private static SessionFactory sessionFactory;

    public DockerContainersTestEnvironment(EnvironmentConfig configProvider) {
        super(configProvider);
        this.msgImageVersion = configProvider.getValue(MSG_VERSION, null);
    }

    @Override
    public void bootstrap() {
        final Network.NetworkImpl network = Network.builder().build();

        msgArtemisContainer = new MsgArtemisContainer(msgImageVersion, network, BROKER_USERNAME, BROKER_PASSWORD);
        mysqlContainer = new Mysql8Container("8.0.33", DATABASE_NAME, DATABASE_USER, DATABASE_PASS, network);
        msgContainer = new MsgContainer(msgImageVersion, network, mysqlContainer, msgArtemisContainer);

        ContainerUtils.run(msgArtemisContainer);

        ContainerUtils.run(mysqlContainer);

        ContainerUtils.run(msgContainer);

        logContainersInfo();
    }

    @Override
    public void resetDatabaseData() {
        getDaoProviderFactory();
        HibernateUtils.eraseMySqlTables(sessionFactory);
    }

    private void logContainersInfo() {
        logger.info("====== Containers ======");
        String offset = "\t";
        msgContainer.logInfo("Messenger", offset);
        mysqlContainer.logInfo("MySQL", offset);
        msgArtemisContainer.logInfo("Artemis", offset);
    }

    @Override
    public void shutdown() {

    }

    @Override
    public String getBaseRestUri() {
        String baseUri = String.format(
                "http://%s:%s", msgContainer.getContainerIpAddress(), msgContainer.getMappedPort() + "/api");

        logger.info("<<<<<<<<<<>>>>>>>>>>>>");
        logger.info(baseUri);
        logger.info("<<<<<<<<<<>>>>>>>>>>>>");

        return baseUri;
    }

    @Override
    public EndpointRegistry getEndpointRegistry() {
        if (endpointRegistry == null) {
            endpointRegistry = new EndpointRegistryImpl(
                    msgArtemisContainer.createConnectionFactory(), "IntegrationTestsId");
        }
        return endpointRegistry;
    }

    @Override
    public DaoProviderFactory getDaoProviderFactory() {
        if (sessionFactory == null) {
            sessionFactory = buildSessionFactory();
        }
        return new DaoProviderFactoryImpl(sessionFactory);

    }

    private SessionFactory buildSessionFactory() {
        final String msgJdbcUrl = ContainerUtils.createJdbcUrl(
                mysqlContainer.getContainerIpAddress(),
                mysqlContainer.getMappedPort(),
                mysqlContainer.getDbName()
        );

        final Properties hibernateProperties = new Properties();

        Configuration configuration = new Configuration();

        hibernateProperties.put(AvailableSettings.DRIVER, org.mariadb.jdbc.Driver.class.getName());
        hibernateProperties.put(AvailableSettings.USER, mysqlContainer.getUser());
        hibernateProperties.put(AvailableSettings.PASS, mysqlContainer.getUserPassword());
        hibernateProperties.put(AvailableSettings.URL, msgJdbcUrl);
        hibernateProperties.put(AvailableSettings.DIALECT, MySQL8Dialect.class.getName());

        final StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .applySettings(hibernateProperties)
                .build();

        return configuration.buildSessionFactory(serviceRegistry);
    }

}
