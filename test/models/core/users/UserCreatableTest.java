package models.core.users;

import helpers.BeforeAndAfterTest;
import models.core.user.UserCreatable;
import models.core.user.UserModel;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class UserCreatableTest extends BeforeAndAfterTest implements UserCreatable {

    private String username         = "Susan Moore";
    private String email            = "susan@moore.com";
    private String phone            = "000000001";
    private boolean isAdmin         = true;
    private String password         = "R3v3l@t104LoA";

    private String existsEmail      = "john@doe.com";
    private String existsPhone      = "000000000";

    private String newEmail         = "email@example.com";
    private String newPhone         = "000000002";

    @Test
    public void createNewUserTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(createUser(email, phone));
        });
    }

    @Test
    public void createUserWithExistsEmailTest() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(createUser(existsEmail, newPhone));
        });
    }

    @Test
    public void createUserWithExistsPhoneTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(createUser(newEmail, existsPhone));
        });
    }

    private UserModel createUser(String userEmail, String userPhone){

        UserModel userModel = new UserModel();
        userModel.username = username;
        userModel.setEmail(userEmail);
        userModel.phone = userPhone;
        userModel.setPassword(password);
        userModel.isAdmin = isAdmin;
        userModel.isActive = true;
        userModel.setUuid();
        userModel.setToken();

        return UserCreatable.super.createNewUser(userModel);
    }
}
