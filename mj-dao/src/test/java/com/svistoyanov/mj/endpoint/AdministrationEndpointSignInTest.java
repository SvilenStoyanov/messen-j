package com.svistoyanov.mj.endpoint;

import com.svistoyanov.mj.DaoProvider;
import com.svistoyanov.mj.api.dto.ErrorCodesDto;
import com.svistoyanov.mj.api.dto.user.SignInDto;
import com.svistoyanov.mj.api.dto.user.UserDto;
import com.svistoyanov.mj.dao.UserDaoTests;
import com.svistoyanov.mj.entity.User;
import com.svistoyanov.mj.worker.AdministrationEndpointWorker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.svistoyanov.mj.constants.WorkerConstants.*;

class AdministrationEndpointSignInTest extends AbstractEndpointTest<UserDto, SignInDto, UserDto> {
    protected final AdministrationEndpointWorker admWorker = new AdministrationEndpointWorker(validator, daoProviderFactory);

    private final SignInDto signInDto = new SignInDto();

    @BeforeEach
    protected void setUp() {
        signInDto.setEmail("john@mail.com");
        signInDto.setPassword("passJohn");
    }

    @Test
    void testSignInNullRequest() {
        //Arrange
        SignInDto nullRequestSignInDto = null;

        //Act
        final List<String> messages = waitForBadRequest(admWorker.signIn(nullRequestSignInDto), ErrorCodesDto.VALIDATION);

        //Assert
        Assertions.assertEquals(1, messages.size());
        Assertions.assertEquals(REQUEST_CANNOT_BE_NULL, messages.get(0));

        logger.debug("End of test reached");
    }

    @Test
    void testSignInNullEmail() {
        //Arrange
        signInDto.setEmail(null);

        //Act
        final List<String> messages = waitForBadRequest(admWorker.signIn(signInDto), ErrorCodesDto.VALIDATION);

        //Assert
        Assertions.assertEquals(1, messages.size());
        Assertions.assertEquals(SIGNIN_EMPTY_FIELDS_ERROR_MESSAGE, messages.get(0));
        logger.debug("End of test reached");
    }

    @Test
    void testSignInUserNotFound() {
        //Arrange
        signInDto.setEmail("missing@mail.com");

        //Act
        final List<String> messages = waitForBadRequest(admWorker.signIn(signInDto), ErrorCodesDto.NOT_FOUND);

        //Assert
        Assertions.assertEquals(1, messages.size());
        Assertions.assertEquals(SIGNIN_USER_NOTFOUND_ERROR_MESSAGE, messages.get(0));
        logger.debug("End of test reached");
    }

    @Test
    void testSignInWrongPassword() {
        //Arrange
        User user = new User(UUID.randomUUID());
        user.setEmail(signInDto.getEmail());
        user.setUsername("username");
        user.setPassword("password");

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            daoProvider.getUserDao().saveOrUpdate(user);
            daoProvider.commit();
        }

        //Act
        final List<String> messages = waitForBadRequest(admWorker.signIn(signInDto), ErrorCodesDto.VALIDATION);

        //Assert
        Assertions.assertEquals(1, messages.size());
        Assertions.assertEquals(SIGNIN_PASSWORD_ERROR_MESSAGE, messages.get(0));
        logger.debug("End of test reached");
    }

    @Test
    void testSignInSuccess() {
        //Arrange
        UserDaoTests.createAndSaveUser(
                daoProviderFactory,
                signInDto.getEmail(),
                "username",
                signInDto.getPassword());

        //Act
        CompletableFuture<UserDto> cf = admWorker.signIn(signInDto);
        UserDto userDto = waitForSuccess(cf);

        //Assert
        assertDeepEquals(signInDto, userDto);
        logger.debug("End of test reached");
    }

    @Override
    protected void assertDeepEquals(SignInDto expected, UserDto actual) {
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getPassword(), actual.getPassword());
        logger.debug("End of assert");
    }

}
