package models.core.users;

import helpers.BeforeAndAfterTest;
import models.core.user.UserByPhoneAndPasswordFindable;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class UserByPhoneAndPasswordFindableTest extends BeforeAndAfterTest implements UserByPhoneAndPasswordFindable {

    private String firstPhone           = "000000000";
    private String secondPhone          = "1111111111";
    private String notExistsPhone       = "000000001";
    private String nullPhone            = null;

    private String password             = "R3v3l@t104LoA";
    private String notExistsPassword    = "R3v3l@t104LoA1";
    private String nullPassword         = "R3v3l@t104LoA1";

    @Test
    public void findUserByFirstPhoneAndPassword(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(UserByPhoneAndPasswordFindable.super.findUserByPhoneAndPassword(firstPhone, password));
        });
    }

    @Test
    public void findUserBySecondPhoneAndPassword(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(UserByPhoneAndPasswordFindable.super.findUserByPhoneAndPassword(secondPhone, password));
        });
    }

    @Test
    public void findUserByNullPhoneAndPassword(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(UserByPhoneAndPasswordFindable.super.findUserByPhoneAndPassword(nullPhone, password));
        });
    }

    @Test
    public void findUserByNotExistsPhoneAndPassword(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(UserByPhoneAndPasswordFindable.super.findUserByPhoneAndPassword(notExistsPhone, password));
        });
    }

    @Test
    public void findUserByFirstPhoneAndNullPassword(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(UserByPhoneAndPasswordFindable.super.findUserByPhoneAndPassword(firstPhone, nullPassword));
        });
    }

    @Test
    public void findUserByFirstPhoneAndNotExistsPassword() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(UserByPhoneAndPasswordFindable.super.findUserByPhoneAndPassword(firstPhone, notExistsPassword));
        });
    }
}
