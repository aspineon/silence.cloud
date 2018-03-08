package models.core.users;

import helpers.BeforeAndAfterTest;
import models.core.user.UserModel;
import models.core.user.UserUpdatable;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class UserUpdatableTest extends BeforeAndAfterTest implements UserUpdatable {

    private Long firstId = 1L;
    private String username = "John Doe";
    private String firstExistsEmail = "john@doe.com";
    private String secondExistsEmail = "john@smith.com";
    private String firstPhone = "000000000";
    private String secondPhone = "1111111111";

    private String password = "R3v3l@t104";

    private boolean isAdmin = true;

    private Long notExistId = 99L;

    private String newEmail = "jonathan@doe.com";
    private String newPhone = "000000002";

    @Test
    public void updateExistsUserWithNotExistsEmailAndNotExistsPhone(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(UserUpdatable.super.updateUser(firstId, createUserModel(firstId, newEmail, newPhone)));
        });
    }

    @Test
    public void updateUserWithExistsPhone() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(UserUpdatable.super.updateUser(firstId, createUserModel(firstId, newEmail, secondPhone)));
        });
    }

    @Test
    public void updateUserWithExistsEmail() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(UserUpdatable.super.updateUser(firstId, createUserModel(firstId, secondExistsEmail, newPhone)));
        });
    }

    @Test(expected = NullPointerException.class)
    public void updateUserWithNotExistsId(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(UserUpdatable.super.updateUser(notExistId, createUserModel(notExistId, newEmail, newPhone)));
        });
    }

    private UserModel createUserModel(Long id, String email, String phone){

        UserModel userModel = UserModel.FINDER.byId(id);
        userModel.username = username;
        userModel.setEmail(email);
        userModel.phone = phone;
        userModel.updatedAt = new Date();

        return userModel;
    }
}
