package models.core.users;

import helpers.BeforeAndAfterTest;
import models.core.user.UserByIdFindable;
import models.core.user.UserModel;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class UserByIdFindableTest extends BeforeAndAfterTest implements UserByIdFindable {

    private Long firstUserId        = 1L;
    private Long secondUserId       = 2L;
    private Long notExistsUserId    = 99L;

    @Test
    public void findUserByFirstUserId() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            checkUser(UserByIdFindable.super.findUserById(firstUserId));
        });
    }

    @Test
    public void findUserBySecondId() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            checkUser(UserByIdFindable.super.findUserById(secondUserId));
        });
    }

    @Test(expected = NoSuchElementException.class)
    public void findUserByNotExistsId(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            Optional<UserModel> optionalUser = UserByIdFindable.super.findUserById(notExistsUserId);

            assertFalse(optionalUser.isPresent());
            assertNull(optionalUser.get());
        });
    }

    private void checkUser(Optional<UserModel> userById) {
        Optional<UserModel> optionalUser = userById;

        assertTrue(optionalUser.isPresent());
        assertNotNull(optionalUser.get());
        assertNotNull(optionalUser.map(user -> user.id));
        assertNotNull(optionalUser.map(user -> user.username));
        assertNotNull(optionalUser.map(user -> user.email));
        assertNotNull(optionalUser.map(user -> user.phone));
        assertNotNull(optionalUser.map(user -> user.createdAt));
        assertNotNull(optionalUser.map(user -> user.updatedAt));
    }


}
