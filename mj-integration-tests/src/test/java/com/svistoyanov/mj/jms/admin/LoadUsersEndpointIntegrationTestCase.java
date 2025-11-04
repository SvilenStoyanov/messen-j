package com.svistoyanov.mj.jms.admin;

import com.svistoyanov.mj.DaoProvider;
import com.svistoyanov.mj.UserDao;
import com.svistoyanov.mj.api.dto.ErrorCodesDto;
import com.svistoyanov.mj.api.dto.ErrorDto;
import com.svistoyanov.mj.api.dto.user.UsersFilterDto;
import com.svistoyanov.mj.api.dto.user.UsersWrapperDto;
import com.svistoyanov.mj.entity.User;
import com.svistoyanov.mj.jms.AbstractJmsSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

class LoadUsersEndpointIntegrationTestCase extends AbstractJmsSupport {
    @Test
    void testLoadUsersSuccess() {
        User user1 = new User(UUID.randomUUID());
        user1.setUsername("AngelCho");
        user1.setEmail("angel@gmail.com");
        user1.setPassword("angel-pass");

        User user2 = new User(UUID.randomUUID());
        user2.setUsername("TinCho");
        user2.setEmail("tincho@gmail.com");
        user2.setPassword("tincho-pass");

        UsersFilterDto usersFilterDto = new UsersFilterDto();
        usersFilterDto.setUsername("cho");
        usersFilterDto.setStart(0);
        usersFilterDto.setOffset(5);

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            UserDao userDao = daoProvider.getUserDao();
            userDao.saveOrUpdate(user1);
            userDao.saveOrUpdate(user2);
            daoProvider.commit();
        }

        UsersWrapperDto usersWrapperDto = invokeForSuccess(usersFilterDto, getLoadUsersOperation());

        List<User> users;
        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            UserDao userDao = daoProvider.getUserDao();
            users = userDao.loadByName(usersFilterDto.getUsername(), 0, 5);
        }

        Assertions.assertEquals(users.size(), usersWrapperDto.getUsers().size());
        Assertions.assertEquals(users.get(0).getUsername(), usersWrapperDto.getUsers().get(0).getUsername());
        Assertions.assertEquals(2, users.size());
    }

    @Test
    void testLoadUsersNullArg() {
        final ErrorDto error = invokeForError(null, getLoadUsersOperation());
        Assertions.assertNotNull(error);
        Assertions.assertEquals(ErrorCodesDto.VALIDATION, error.getErrorCode());
        Assertions.assertEquals(1, error.getMessages().size());
        Assertions.assertEquals("Request can not be null.", error.getMessages().get(0));
    }

    private Function<UsersFilterDto, CompletableFuture<UsersWrapperDto>> getLoadUsersOperation() {
        return endpointRegistry.getAdministrationEndpoint()::loadUsers;
    }
}
