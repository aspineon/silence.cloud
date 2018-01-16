package models.core.users;

import helpers.BeforeAndAfterTest;
import models.core.user.UserDeletable;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class UserDeletableTest extends BeforeAndAfterTest implements UserDeletable {

    private Long existsId = 1L;
    private Long notExistsId = 99L;

    @Test
    public void deleteUserWithNotExistsId() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(UserDeletable.super.deleteUser(notExistsId));
        });
    }

    @Test
    public void deleteUserWithExistsId() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(UserDeletable.super.deleteUser(existsId));
        });
    }
}
