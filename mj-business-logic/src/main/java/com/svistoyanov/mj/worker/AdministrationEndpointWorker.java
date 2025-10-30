package com.svistoyanov.mj.worker;

import com.svistoyanov.mj.DaoProvider;
import com.svistoyanov.mj.DaoProviderFactory;
import com.svistoyanov.mj.UserDao;
import com.svistoyanov.mj.api.AdministrationEndpoint;
import com.svistoyanov.mj.api.dto.user.*;
import com.svistoyanov.mj.entity.User;
import com.svistoyanov.mj.mapper.UserMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static com.svistoyanov.mj.constants.WorkerConstants.*;
import static com.svistoyanov.mj.exception.ExceptionUtils.*;

public class AdministrationEndpointWorker implements AdministrationEndpoint {

    protected static final Logger logger = LoggerFactory.getLogger(AdministrationEndpointWorker.class);

    private final Validator          validator;
    private final DaoProviderFactory daoProviderFactory;

    public AdministrationEndpointWorker(Validator validator, DaoProviderFactory daoProviderFactory) {
        this.validator = validator;
        this.daoProviderFactory = daoProviderFactory;
    }

    @Override
    public CompletableFuture<Void> signUp(SignUpDto signUpDto) {
        //null check + unit tests
        if (signUpDto == null) {
            return CompletableFuture.failedFuture(createValidationError(REQUEST_CANNOT_BE_NULL));
        }

        if (signUpDto.getEmail() == null || signUpDto.getUsername() == null || signUpDto.getPassword() == null) {
            return CompletableFuture.failedFuture(createValidationError(SIGNUP_EMPTY_FIELDS_ERROR_MESSAGE));
        }

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            if (daoProvider.getUserDao().loadByEmail(signUpDto.getEmail()) != null) {
                return CompletableFuture.failedFuture(createConflictError(SIGNUP_USER_EXISTS_ERROR_MESSAGE));
            }

            User user = UserMapper.instance.signUpDtoToUser(signUpDto);
            Set<ConstraintViolation<User>> violations = validator.validate(user);
            if (!violations.isEmpty()) {
                return CompletableFuture.failedFuture(createValidationError(SIGNUP_INVALID_FIELDS_ERROR_MESSAGE));
            }

            daoProvider.getUserDao().saveOrUpdate(user);
            daoProvider.commit();

            return CompletableFuture.completedFuture(null);

        }
        catch (IllegalArgumentException | ValidationException e) {
            logger.error("Invalid request.", e);
            return CompletableFuture.failedFuture(createValidationError(e.getMessage()));
        }
        catch (Throwable th) {
            return CompletableFuture.failedFuture(createInternalServerError(UNEXPECTED_ERROR_MESSAGE_TRY_AGAIN));
        }
    }

    @Override
    public CompletableFuture<UserDto> signIn(SignInDto signInDto) {
        //null check + unit tests
        if (signInDto == null) {
            return CompletableFuture.failedFuture(createValidationError(REQUEST_CANNOT_BE_NULL));
        }

        if (signInDto.getEmail() == null || signInDto.getPassword() == null) {
            return CompletableFuture.failedFuture(createValidationError(SIGNIN_EMPTY_FIELDS_ERROR_MESSAGE));
        }

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            User user = daoProvider.getUserDao().loadByEmail(signInDto.getEmail());

            if (user == null) {
                return CompletableFuture.failedFuture(createNotFoundError(SIGNIN_USER_NOTFOUND_ERROR_MESSAGE));
            }

            if (!signInDto.getPassword().equals(user.getPassword())) {
                return CompletableFuture.failedFuture(createValidationError(SIGNIN_PASSWORD_ERROR_MESSAGE));
            }

            UserDto userDto = UserMapper.instance.signInDtoToUserDto(signInDto);
            userDto.setId(user.getId());

            return CompletableFuture.completedFuture(userDto);

        }
        catch (IllegalArgumentException e) {
            logger.error("Invalid request.", e);
            return CompletableFuture.failedFuture(createValidationError(e.getMessage()));
        }
    }

    @Override
    public CompletableFuture<UsersWrapperDto> loadUsers(UsersFilterDto filter) {
        //null check + unit tests
        if (filter == null) {
            return CompletableFuture.failedFuture(createValidationError(REQUEST_CANNOT_BE_NULL));
        }
        if (filter.getUsername() == null) {
            return CompletableFuture.failedFuture(createValidationError(SIGNIN_EMPTY_FIELDS_ERROR_MESSAGE));
        }
        if (filter.getStart() < 0) {
            return CompletableFuture.failedFuture(createValidationError(LOAD_MESSAGE_START_ERROR_MESSAGE));
        }
        if (filter.getOffset() < 1) {
            return CompletableFuture.failedFuture(createValidationError(LOAD_MESSAGE_OFFSET_ERROR_MESSAGE));
        }

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            UserDao userDao = daoProvider.getUserDao();
            final List<User> users = userDao.loadByName(
                    filter.getUsername(),
                    filter.getStart(),
                    filter.getOffset());

            List<UserDescriptionDto> userDtoList = UserMapper.instance.usersToUserDtoList(users);

            UsersWrapperDto usersWrapperDto = new UsersWrapperDto();
            usersWrapperDto.setUsers(userDtoList);

            return CompletableFuture.completedFuture(usersWrapperDto);
        }
        catch (IllegalArgumentException | ValidationException e) {
            logger.error(INVALID_REQUEST_ERROR_MESSAGE, e);
            return CompletableFuture.failedFuture(createValidationError(e.getMessage()));
        }
        catch (Throwable th) {
            return CompletableFuture.failedFuture(createInternalServerError(UNEXPECTED_ERROR_MESSAGE_TRY_AGAIN));
        }
    }

    @Override
    public void close() {

    }
}
