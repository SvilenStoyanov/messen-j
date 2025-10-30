package com.svistoyanov.mj;

public interface DaoProvider extends AutoCloseable {

    UserDao getUserDao();

    MessageDao getMessageDao();

    void commit();

    @Override
    void close();
}
