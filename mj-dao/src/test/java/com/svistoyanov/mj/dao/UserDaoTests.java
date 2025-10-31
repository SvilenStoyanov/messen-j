package com.svistoyanov.mj.dao;

import com.svistoyanov.mj.CrudDao;
import com.svistoyanov.mj.DaoProvider;
import com.svistoyanov.mj.DaoProviderFactory;
import com.svistoyanov.mj.UserDao;
import com.svistoyanov.mj.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDaoTests extends AbstractCrudDaoTests<User> {
    @Test
    void testInsertUserWithNullEmail() {
        User expectedUser = createValidEntity();
        expectedUser.setEmail(null);

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            Assertions.assertThrows(Exception.class, () ->
            {
                UserDao userDao = daoProvider.getUserDao();
                userDao.saveOrUpdate(expectedUser);
                daoProvider.commit();
            });
        }
        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            User actualUser = daoProvider.getUserDao().loadById(expectedUser.getId());
            Assertions.assertNull(actualUser);
        }
    }

    @Test
    void testInsertUserWithNullUsername() {
        User expectedUser = createValidEntity();
        expectedUser.setUsername(null);

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            Assertions.assertThrows(Exception.class, () ->
            {
                UserDao userDao = daoProvider.getUserDao();
                userDao.saveOrUpdate(expectedUser);
                daoProvider.commit();
            });
        }
        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            User actualUser = daoProvider.getUserDao().loadById(expectedUser.getId());

            Assertions.assertNull(actualUser);
        }
    }

    @Test
    void testInsertUserWithUsernameLength() {
        User expectedUser = createValidEntity();
        expectedUser.setUsername("123456789012345678901");

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            Assertions.assertThrows(Exception.class, () ->
            {
                UserDao userDao = daoProvider.getUserDao();
                userDao.saveOrUpdate(expectedUser);
                daoProvider.commit();
            });
        }
        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {

            User actualUser = daoProvider.getUserDao().loadById(expectedUser.getId());

            Assertions.assertNull(actualUser);
        }
    }

    @Test
    void testInsertUserWithNullPassword() {
        User expectedUser = createValidEntity();
        expectedUser.setPassword(null);

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            Assertions.assertThrows(Exception.class, () ->
            {
                UserDao userDao = daoProvider.getUserDao();
                userDao.saveOrUpdate(expectedUser);
                daoProvider.commit();
            });
        }

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            User actualUser = daoProvider.getUserDao().loadById(expectedUser.getId());

            Assertions.assertNull(actualUser);
        }
    }

    @Test
    void testInsertUserWithPasswordLength() {
        User expectedUser = createValidEntity();
        expectedUser.setPassword("123456789012345678901");

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            Assertions.assertThrows(Exception.class, () ->
            {
                UserDao userDao = daoProvider.getUserDao();
                userDao.saveOrUpdate(expectedUser);
                daoProvider.commit();
            });
        }
        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            User actualUser = daoProvider.getUserDao().loadById(expectedUser.getId());

            Assertions.assertNull(actualUser);
        }
    }

    @Test
    void testLoadByNameWhenGivenNameAtTheEnd() {
        User user1 = createUser("mishoM@abv.bg", "Misho", "password");
        User user2 = createUser("gosho@gmail.com", "Gosho", "password");
        User user3 = createUser("Georgi@gmail.com", "Georgi", "password");

        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(user1);
        expectedUsers.add(user2);
        expectedUsers.add(user3);

        persistMultipleUsers(expectedUsers);

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            UserDao userDao = daoProvider.getUserDao();
            List<User> loadedUsers = userDao.loadByName("sho", 0, 10);
            Assertions.assertEquals(expectedUsers.size() - 1, loadedUsers.size());
        }
    }

    @Test
    void testLoadByNameWhenGivenNameAtBeginning() {
        User user1 = createUser("mishoM@abv.bg", "Misho", "password");
        User user2 = createUser("shogo@gmail.com", "Shogo", "password");
        User user3 = createUser("Georgi@gmail.com", "Georgi", "password");

        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(user1);
        expectedUsers.add(user2);
        expectedUsers.add(user3);

        persistMultipleUsers(expectedUsers);

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            UserDao userDao = daoProvider.getUserDao();
            List<User> loadedUsers = userDao.loadByName("sho", 0, 10);
            Assertions.assertEquals(2, loadedUsers.size());
        }
    }

    @Test
    void testLoadByNameWhenGivenNameAtMiddle() {
        User user1 = createUser("misho@gmail.com", "Misho", "password");
        User user2 = createUser("asdf@gmail.com", "Shogo", "password");
        User user3 = createUser("kashon@box.com", "kaSHOn", "password");

        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(user1);
        expectedUsers.add(user2);
        expectedUsers.add(user3);

        persistMultipleUsers(expectedUsers);

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            UserDao userDao = daoProvider.getUserDao();
            List<User> loadedUsers = userDao.loadByName("sho", 0, 10);
            Assertions.assertEquals(expectedUsers.size(), loadedUsers.size());
        }
    }

    @Test
    void testLoadByNameWhenGivenNameEmpty() {
        User user1 = createUser("123@abv.bg", "Misho", "password");
        User user2 = createUser("1234@abv.bg", "Shogo", "password");
        User user3 = createUser("12345@abv.bg", "Georgi", "password");

        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(user1);
        expectedUsers.add(user2);
        expectedUsers.add(user3);

        persistMultipleUsers(expectedUsers);

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            UserDao userDao = daoProvider.getUserDao();
            List<User> loadedUsers = userDao.loadByName("", 0, 10);
            for (User user : loadedUsers) {
                System.out.println(user.getEmail());
            }
            Assertions.assertEquals(expectedUsers.size(), loadedUsers.size());
        }
    }

    @Test
    void testLoadByNameWhenGivenNameWithManySpaces() {
        User user1 = createUser("123@abv.bg", "Misho", "password");
        User user2 = createUser("1234@abv.bg", "Shogo", "password");
        User user3 = createUser("12345@abv.bg", "Georgi", "password");

        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(user1);
        expectedUsers.add(user2);
        expectedUsers.add(user3);

        persistMultipleUsers(expectedUsers);

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            UserDao userDao = daoProvider.getUserDao();
            List<User> loadedUsers = userDao.loadByName("     ", 0, 10);
            Assertions.assertEquals(0, loadedUsers.size());
        }
    }

    @Test
    void testLoadByNameNonExistingUser() {
        User user1 = createUser("123@abv.bg", "Misho", "password");
        User user2 = createUser("1234@abv.bg", "Shogo", "password");
        User user3 = createUser("12345@abv.bg", "Georgi", "password");

        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(user1);
        expectedUsers.add(user2);
        expectedUsers.add(user3);

        persistMultipleUsers(expectedUsers);

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            UserDao userDao = daoProvider.getUserDao();
            List<User> loadedUsers = userDao.loadByName("Ivan", 0, 10);
            Assertions.assertEquals(0, loadedUsers.size());
        }
    }

    @Test
    void testLoadByEmailExistingUser() {
        User user1 = createUser("123@abv.bg", "Misho", "password");
        User user2 = createUser("1234@abv.bg", "Shogo", "password");
        User user3 = createUser("12345@abv.bg", "Georgi", "password");

        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(user1);
        expectedUsers.add(user2);
        expectedUsers.add(user3);

        persistMultipleUsers(expectedUsers);

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            UserDao userDao = daoProvider.getUserDao();
            List<User> loadedUsers = List.of(userDao.loadByEmail(user1.getEmail()));
            Assertions.assertEquals(1, loadedUsers.size());
        }
    }

    @Test
    void testLoadByEmailNonExistingUser() {
        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            User actualUser = daoProvider.getUserDao().loadByEmail("123@abv.bg");

            Assertions.assertNull(actualUser);
        }
    }

    @Override
    public User createValidEntity() {
        return createUser("email@email.com", "username", "password");
    }

    public static User createUser(String email, String username, String password) {
        User user = new User(UUID.randomUUID());
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);

        return user;
    }

    public static User createAndSaveUser(DaoProviderFactory daoProviderFactory, String email, String username,
            String password) {
        User user = createUser(email, username, password);

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            UserDao userDao = daoProvider.getUserDao();

            userDao.saveOrUpdate(user);

            daoProvider.commit();

        }

        return user;
    }

    @Override
    protected CrudDao<User> getDao(DaoProvider daoProvider) {
        return daoProvider.getUserDao();
    }

    @Override
    protected void assertDeepEquals(User expectedUser, User actualUser) {
        Assertions.assertEquals(expectedUser.getId(), actualUser.getId());
        Assertions.assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        Assertions.assertEquals(expectedUser.getUsername(), actualUser.getUsername());
        Assertions.assertEquals(expectedUser.getPassword(), actualUser.getPassword());
    }

    @Override
    protected void modifiedEntity(User expected) {
        expected.setEmail("asd@mail.com");
    }

    public void persistMultipleUsers(List<User> expectedUsers) {
        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            UserDao userDao = daoProvider.getUserDao();

            for (User user : expectedUsers) {
                userDao.saveOrUpdate(user);
            }
            daoProvider.commit();
        }
    }

}
