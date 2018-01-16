package models.core.users;

import helpers.BeforeAndAfterTest;
import models.core.user.UserByEmailFindable;
import org.junit.Test;

import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class UserByEmailFindableTest extends BeforeAndAfterTest implements UserByEmailFindable {

    private String nullEmail            = null;
    private String firstExistsEmail     = "john@doe.com";
    private String secondExistsEmail    = "john@smith.com";
    private String notExistsEmail       = "john1@doe.com";

    @Test
    public void checkFirstExistsEmail() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(UserByEmailFindable.super.findUserByEmail(firstExistsEmail));
        });
    }

    @Test
    public void checkSecondExistsEmail() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(UserByEmailFindable.super.findUserByEmail(secondExistsEmail));
        });
    }

    @Test
    public void checkNotExistsEmailTest() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(UserByEmailFindable.super.findUserByEmail(notExistsEmail));
        });
    }

    @Test
    public void checkNullEmailTest() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(UserByEmailFindable.super.findUserByEmail(nullEmail));
        });
    }
}
