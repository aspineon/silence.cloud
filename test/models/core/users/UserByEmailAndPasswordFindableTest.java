package models.core.users;

import helpers.BeforeAndAfterTest;
import models.core.user.UserByEmailAndPasswordFindable;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class UserByEmailAndPasswordFindableTest extends BeforeAndAfterTest implements UserByEmailAndPasswordFindable {

    private String firstExistsEmail     = "john@doe.com";
    private String secondExistsEmail    = "john@smith.com";
    private String notExistsEmail       = "john1@smith.com";
    private String nullEmail            = null;

    private String existsPassword       = "R3v3l@t104LoA";
    private String notExistsPassword    = "R3v3l@t104LoA1";
    private String nullPassword         = null;

    @Test
    public void firstExistsEmailAndPasswordTest() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(
                    UserByEmailAndPasswordFindable.super.findUserByEmailAndPassword(firstExistsEmail, existsPassword)
            );
        });
    }

    @Test
    public void secondExistsEmailAndExistsPasswordTest() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(
                    UserByEmailAndPasswordFindable.super.findUserByEmailAndPassword(secondExistsEmail, existsPassword)
            );
        });
    }

    @Test
    public void notExistsEmailAndExistsPasswordTest() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(
                    UserByEmailAndPasswordFindable.super.findUserByEmailAndPassword(notExistsEmail, existsPassword)
            );
        });
    }

    @Test
    public void nullEmailAndExistsPasswordTest() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(
                    UserByEmailAndPasswordFindable.super.findUserByEmailAndPassword(nullEmail, existsPassword)
            );
        });
    }

    @Test
    public void existsEmailAndNotExistsPasswordTest() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(
                    UserByEmailAndPasswordFindable.super.findUserByEmailAndPassword(firstExistsEmail, notExistsPassword)
            );
        });
    }

    @Test
    public void existsEmailAndNullPasswordTest() {

        assertNull(
                UserByEmailAndPasswordFindable.super.findUserByEmailAndPassword(firstExistsEmail, nullPassword)
        );
    }
}
