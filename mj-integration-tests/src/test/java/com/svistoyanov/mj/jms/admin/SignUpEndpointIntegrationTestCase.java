package com.svistoyanov.mj.jms.admin;

import com.svistoyanov.mj.DaoProvider;
import com.svistoyanov.mj.UserDao;
import com.svistoyanov.mj.api.dto.ErrorCodesDto;
import com.svistoyanov.mj.api.dto.ErrorDto;
import com.svistoyanov.mj.api.dto.user.SignUpDto;
import com.svistoyanov.mj.entity.User;
import com.svistoyanov.mj.jms.AbstractJmsSupport;
import com.svistoyanov.mj.jms.UserConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

class SignUpEndpointIntegrationTestCase extends AbstractJmsSupport {
    @Test
    void testSignUpSuccess() {
        SignUpDto request = new SignUpDto(UserConstants.EMAIL, UserConstants.USERNAME, UserConstants.PASSWORD);
        invokeForSuccess(request, getSignUpOperation());

        User dbUser;
        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            UserDao userDao = daoProvider.getUserDao();
            dbUser = userDao.loadByEmail(request.getEmail());
        }
        Assertions.assertNotNull(dbUser);

    }

    @Test
    void testSignUpNullArg() {
        final ErrorDto error = invokeForError(null, getSignUpOperation());
        Assertions.assertNotNull(error);
        Assertions.assertEquals(ErrorCodesDto.VALIDATION, error.getErrorCode());
        Assertions.assertEquals(1, error.getMessages().size());
        Assertions.assertEquals("Request can not be null.", error.getMessages().get(0));
    }

    private Function<SignUpDto, CompletableFuture<Void>> getSignUpOperation() {
        return endpointRegistry.getAdministrationEndpoint()::signUp;
    }
}
