package com.svistoyanov.mj;

import com.svistoyanov.mj.utils.HibernateUtils;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class PersistenceTestHelper {

    private static SessionFactory sessionFactory;

    public static DaoProviderFactory getDaoProviderFactory()
    {
        if (sessionFactory == null) {
            sessionFactory = buildSessionFactory();
        }
        return new DaoProviderFactoryImpl(sessionFactory);
    }

    private static SessionFactory buildSessionFactory()
    {
        Configuration configuration = new Configuration();

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .configure("/hibernate.cfg.xml")
                .applySetting(AvailableSettings.DRIVER,org.h2.Driver.class.getName())
                .applySetting(AvailableSettings.USER,"root")
                .applySetting(AvailableSettings.PASS,"root")
                .applySetting(AvailableSettings.URL,"jdbc:h2:mem:testDb")
                .applySetting(AvailableSettings.DIALECT,org.hibernate.dialect.H2Dialect.class.getName())
                .build();

        SchemaProvider.provideSchemaInDb(serviceRegistry);
        SchemaProvider.provideSchemaInFile(serviceRegistry);

        return configuration.buildSessionFactory(serviceRegistry);
    }

    public static void resetAllTables()
    {
        HibernateUtils.eraseAllTables(sessionFactory);
    }

}
