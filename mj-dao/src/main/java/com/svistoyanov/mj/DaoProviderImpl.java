package com.svistoyanov.mj;

import org.hibernate.Session;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

public class DaoProviderImpl implements DaoProvider {

    private final Session session;
    private final JpaRepositoryFactory factory;
    private UserDao userDao;
    private MessageDao messageDao;

    public DaoProviderImpl(Session session) {
        this.session = session;
        this.factory = new JpaRepositoryFactory(session);
        session.beginTransaction();
        System.out.println("Transaction begins");
    }

    @Override
    public UserDao getUserDao() {
        if (userDao == null) {
            this.userDao = new UserDaoImpl(factory.getRepository(UserRepository.class));
        }
        return this.userDao;
    }

    @Override
    public MessageDao getMessageDao() {
        if (messageDao == null) {
            this.messageDao = new MessageDaoImpl(factory.getRepository(MessageRepository.class));
        }
        return this.messageDao;
    }

    @Override
    public void commit() {
        session.getTransaction().commit();
    }

    @Override
    public void close() {
        session.close();
    }

}
