package models.core.users;

import helpers.BeforeAndAfterTest;
import models.core.user.AllUsersFindable;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class FindAllUsersInterfaceTest extends BeforeAndAfterTest implements AllUsersFindable {

    private int expectedSize = 2;

    @Test
    public void findAllUsersSizeTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertEquals(expectedSize, AllUsersFindable.super.findAllUsers().size());
        });
    }

}
