package models.core.users;

import helpers.BeforeAndAfterTest;
import models.core.user.UserByPhoneFindable;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class FindUserByPhoneTest extends BeforeAndAfterTest implements UserByPhoneFindable {

    private String firstPhone           = "000000000";
    private String secondPhone          = "1111111111";
    private String notExistsPhone       = "000000001";
    private String nullPhone            = null;

    @Test
    public void firstExistsPhoneTest() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(UserByPhoneFindable.super.findUserByPhone(firstPhone));
        });
    }

    @Test
    public void secondExistsPhoneTest() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(UserByPhoneFindable.super.findUserByPhone(secondPhone));
        });
    }

    @Test
    public void notExistsPhoneTest() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(UserByPhoneFindable.super.findUserByPhone(notExistsPhone));
        });
    }

    @Test
    public void nullPhoneTest() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(UserByPhoneFindable.super.findUserByPhone(nullPhone));
        });
    }
}
