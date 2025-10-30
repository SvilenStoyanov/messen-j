package com.svistoyanov.mj;

import org.hibernate.SessionFactory;

public class DaoProviderFactoryImpl implements DaoProviderFactory {

    private final SessionFactory sessionFactory;

    public DaoProviderFactoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public DaoProvider createDaoProvider() {
        return new DaoProviderImpl(sessionFactory.openSession());
    }
}
