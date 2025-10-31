package com.svistoyanov.mj.utils;

import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HibernateUtils {
    private static final Logger logger = LoggerFactory.getLogger(HibernateUtils.class);

    private HibernateUtils() {
        // Static only
    }

    public static void eraseAllTables(SessionFactory sessionFactory) {
        logger.debug("Erasing all tables...");

        final EntityManager em = sessionFactory.createEntityManager();
        try {
            em.getTransaction().begin();

            // Disable FKs, so we can erase with no table relationships relation errors
            em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE");

            final List<String> tableNames = em.createNativeQuery(
                            "SELECT DISTINCT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE UPPER(TABLE_SCHEMA) = 'PUBLIC'")
                    .getResultList();
            tableNames.forEach(
                    tableName -> em.createNativeQuery(String.format("DELETE FROM %s", tableName)).executeUpdate());

            // Now enable FKs, so the DB is properly reset
            em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE");

            // Apply the changes to scheme
            em.getTransaction().commit();
        }
        catch (Throwable th) {
            em.getTransaction().rollback();
            throw new RuntimeException(th);
        }
        finally {
            em.close();
        }
    }

    public static void eraseMySqlTables(SessionFactory sessionFactory) {
        logger.debug("Erasing all tables...");

        final EntityManager em = sessionFactory.createEntityManager();
        try {
            em.getTransaction().begin();

            // Disable FKs, so we can erase with no table relationships relation errors
            em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE");

            final List<String> tableNames = em.createNativeQuery(
                            "SELECT DISTINCT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE UPPER(TABLE_SCHEMA) = 'MESSENGER'")
                    .getResultList();
            tableNames.forEach(
                    tableName -> em.createNativeQuery(String.format("DELETE FROM %s", tableName)).executeUpdate());

            // Now enable FKs, so the DB is properly reset
            em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE");

            // Apply the changes to scheme
            em.getTransaction().commit();
        }
        catch (Throwable th) {
            em.getTransaction().rollback();
            throw new RuntimeException(th);
        }
        finally {
            em.close();
        }
    }
}
