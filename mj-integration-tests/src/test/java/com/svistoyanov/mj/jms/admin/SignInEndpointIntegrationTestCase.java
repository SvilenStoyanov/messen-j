package com.svistoyanov.mj.jms.admin;

import com.svistoyanov.mj.DaoProvider;
import com.svistoyanov.mj.UserDao;
import com.svistoyanov.mj.api.dto.ErrorCodesDto;
import com.svistoyanov.mj.api.dto.ErrorDto;
import com.svistoyanov.mj.api.dto.user.SignInDto;
import com.svistoyanov.mj.api.dto.user.UserDto;
import com.svistoyanov.mj.entity.User;
import com.svistoyanov.mj.jms.AbstractJmsSupport;
import com.svistoyanov.mj.jms.UserConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

class SignInEndpointIntegrationTestCase extends AbstractJmsSupport {
    @Test
    void testSignInSuccess() {
        SignInDto request = new SignInDto(UserConstants.EMAIL, UserConstants.PASSWORD);

        User user = new User(UUID.randomUUID());
        user.setUsername("user1");
        user.setEmail(UserConstants.EMAIL);
        user.setPassword(UserConstants.PASSWORD);

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            UserDao userDao = daoProvider.getUserDao();
            userDao.saveOrUpdate(user);
            daoProvider.commit();
        }

        invokeForSuccess(request, getSignInOperation());

        User dbUser;
        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            UserDao userDao = daoProvider.getUserDao();
            dbUser = userDao.loadByEmail(request.getEmail());
        }
        Assertions.assertNotNull(dbUser);
        Assertions.assertEquals(dbUser.getEmail(), dbUser.getEmail());

    }

    @Test
    void testSignInNullArg() {
        final ErrorDto error = invokeForError(null, getSignInOperation());
        Assertions.assertNotNull(error);
        Assertions.assertEquals(ErrorCodesDto.VALIDATION, error.getErrorCode());
        Assertions.assertEquals(1, error.getMessages().size());
        Assertions.assertEquals("Request can not be null.", error.getMessages().get(0));
    }

    private Function<SignInDto, CompletableFuture<UserDto>> getSignInOperation() {
        return endpointRegistry.getAdministrationEndpoint()::signIn;
    }
}
