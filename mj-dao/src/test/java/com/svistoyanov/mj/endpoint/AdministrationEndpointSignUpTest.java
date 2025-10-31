package com.svistoyanov.mj.endpoint;

import com.svistoyanov.mj.DaoProvider;
import com.svistoyanov.mj.api.dto.ErrorCodesDto;
import com.svistoyanov.mj.api.dto.user.SignUpDto;
import com.svistoyanov.mj.entity.User;
import com.svistoyanov.mj.worker.AdministrationEndpointWorker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.svistoyanov.mj.constants.WorkerConstants.*;

class AdministrationEndpointSignUpTest extends AbstractEndpointTest<Void, SignUpDto, User> {
    protected final AdministrationEndpointWorker admWorker = new AdministrationEndpointWorker(validator, daoProviderFactory);

    private final SignUpDto signUpDto = new SignUpDto();

    @BeforeEach
    protected void setUp() {
        signUpDto.setEmail("john@mail.com");
        signUpDto.setUsername("john");
        signUpDto.setPassword("passJohn");
    }

    //1. izvikvam endpoint s nqkakvi argumenti
    //2. izchakvane na otgovor s daden timeout, otg moje da e exception ili dto(ako e uspeshno)
    //3. da se sravni rezultata. //whencomplete - da izchaka otgovor i da uvedomi latch-a
    // da iznesem cqloto izchakvane na drugo mqsto - neshto koeto da priema complFuture i da izchakva otgovor

    @Test
    void testSignUpNullRequest() {
        //Arrange
        SignUpDto nullRequestSignUpDto = null;

        //Act
        final List<String> messages = waitForBadRequest(admWorker.signUp(nullRequestSignUpDto), ErrorCodesDto.VALIDATION);

        //Assert
        Assertions.assertEquals(1, messages.size());
        Assertions.assertEquals(REQUEST_CANNOT_BE_NULL, messages.get(0));

        logger.debug("End of test reached");
    }

    @Test
    void testSignUpNullEmail() {
        //Arrange
        signUpDto.setEmail(null);

        //Act
        final List<String> messages = waitForBadRequest(admWorker.signUp(signUpDto), ErrorCodesDto.VALIDATION);

        //Assert
        Assertions.assertEquals(1, messages.size());
        Assertions.assertEquals(SIGNUP_EMPTY_FIELDS_ERROR_MESSAGE, messages.get(0));
        logger.debug("End of test reached");
    }

    @Test
    void testSignUpDuplicateExists() {
        //Arrange, Act
        admWorker.signUp(signUpDto);
        final List<String> messages = waitForBadRequest(admWorker.signUp(signUpDto), ErrorCodesDto.CONFLICT);

        //Assert
        Assertions.assertEquals(1, messages.size());
        Assertions.assertEquals(SIGNUP_USER_EXISTS_ERROR_MESSAGE, messages.get(0));
        logger.debug("End of test reached");
    }

    @Test
    void testSignUpInvalidEmail() {
        //Arrange
        signUpDto.setEmail("invalid email");

        //Act
        final List<String> messages = waitForBadRequest(admWorker.signUp(signUpDto), ErrorCodesDto.VALIDATION);

        //Assert
        Assertions.assertEquals(1, messages.size());
        Assertions.assertEquals(SIGNUP_INVALID_FIELDS_ERROR_MESSAGE, messages.get(0));
        logger.debug("End of test reached");
    }

    @Test
    void testSignUpUnexpectedError() {
        //Arrange
        signUpDto.setEmail("12345678901@gmail.com"); //21 chars

        //Act
        final List<String> messages = waitForBadRequest(admWorker.signUp(signUpDto), ErrorCodesDto.INTERNAL_SERVER_ERROR);

        //Assert
        Assertions.assertEquals(1, messages.size());
        Assertions.assertEquals(UNEXPECTED_ERROR_MESSAGE_TRY_AGAIN, messages.get(0));
        logger.debug("End of test reached");
    }

    @Test
    void testSignUpSuccess() {
        //Arrange, Act
        CompletableFuture<Void> cf = admWorker.signUp(signUpDto);
        waitForSuccess(cf);
        User user;
        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            user = daoProvider.getUserDao().loadByEmail(signUpDto.getEmail());
        }

        //Assert
        assertDeepEquals(signUpDto, user);
        logger.debug("End of test reached");
    }

    @Override
    protected void assertDeepEquals(SignUpDto expected, User actual) {
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getUsername(), actual.getUsername());
        Assertions.assertEquals(expected.getPassword(), actual.getPassword());
    }

}
