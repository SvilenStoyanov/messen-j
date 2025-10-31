package com.svistoyanov.mj.endpoint;

import com.svistoyanov.mj.DaoProvider;
import com.svistoyanov.mj.UserDao;
import com.svistoyanov.mj.api.dto.ErrorCodesDto;
import com.svistoyanov.mj.api.dto.user.UserDescriptionDto;
import com.svistoyanov.mj.api.dto.user.UsersFilterDto;
import com.svistoyanov.mj.api.dto.user.UsersWrapperDto;
import com.svistoyanov.mj.dao.UserDaoTests;
import com.svistoyanov.mj.entity.User;
import com.svistoyanov.mj.worker.AdministrationEndpointWorker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.svistoyanov.mj.constants.WorkerConstants.SIGNIN_EMPTY_FIELDS_ERROR_MESSAGE;

public class AdministrationEndpointLoadUsersTest extends AbstractEndpointTest<UsersWrapperDto, UsersFilterDto, User> {
    protected final AdministrationEndpointWorker admWorker      = new AdministrationEndpointWorker(validator, daoProviderFactory);
    private final   UsersFilterDto               usersFilterDto = new UsersFilterDto();

    private User user1;
    private User user2;

    @BeforeEach
    protected void setUp() {
        user1 = UserDaoTests.createAndSaveUser(daoProviderFactory, "martin@mail.com", "Martin", "martin-pass");
        user2 = UserDaoTests.createAndSaveUser(daoProviderFactory, "tino@mail.com", "Tino", "tino-pass");

        usersFilterDto.setUsername("tin");
        usersFilterDto.setStart(0);
        usersFilterDto.setOffset(10);
    }

    @Test
    void loadUsersCountShouldBeCorrect() {
        CompletableFuture<UsersWrapperDto> completableFuture = admWorker.loadUsers(usersFilterDto);
        UsersWrapperDto usersWrapperDto = waitForSuccess(completableFuture);

        List<User> users;
        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            UserDao userDao = daoProvider.getUserDao();
            users = userDao.loadByName(usersFilterDto.getUsername(), 0, 5);
        }

        for (UserDescriptionDto dto :
                usersWrapperDto.getUsers()) {
            logger.debug("{{{{{{{{{{{ Id: {}, Username: {} }}}}}}}}}}}", dto.getId(), dto.getUsername());
        }
        Assertions.assertEquals(users.size(), usersWrapperDto.getUsers().size());
    }

    @Test
    void loadUsersWithNullUsername() {
        usersFilterDto.setUsername(null);

        final List<String> errorMessages = waitForBadRequest(admWorker.loadUsers(usersFilterDto), ErrorCodesDto.VALIDATION);

        Assertions.assertEquals(1, errorMessages.size());
        logger.error("{{{{{{{{{ {} }}}}}}}}}}", errorMessages.get(0));
        Assertions.assertEquals(SIGNIN_EMPTY_FIELDS_ERROR_MESSAGE, errorMessages.get(0));
    }

    @Override
    protected void assertDeepEquals(UsersFilterDto expected, User actual) {

    }
}
