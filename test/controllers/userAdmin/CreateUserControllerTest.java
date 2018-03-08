package controllers.userAdmin;

import com.google.common.collect.ImmutableMap;
import helpers.DefaultUsers;
import models.core.user.UserModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.Evolutions;
import play.mvc.Result;
import play.test.WithApplication;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static play.api.test.CSRFTokenHelper.addCSRFToken;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.POST;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;

public class CreateUserControllerTest extends WithApplication {

    private Long id = 1L;

    private String newUsername = "new username";
    private String newEmail = "email@example.com";
    private String newPhone = "2222222222";
    private String newPassword = "R3v3l@t104LoA";
    private String badPassword = "R3v3l@t104Loa";

    Database database;

    @Before
    public void setUp(){

        database = Databases.inMemory(
                "mydatabase",
                ImmutableMap.of(
                        "MODE", "MYSQL"
                ),
                ImmutableMap.of(
                        "logStatements", true
                )
        );
        Evolutions.applyEvolutions(database);
        DefaultUsers defaultUsers = new DefaultUsers();
        defaultUsers.createUsers();
    }

    @After
    public void tearDown(){

        DefaultUsers defaultUsers = new DefaultUsers();
        defaultUsers.deleteUsers();
        Evolutions.cleanupEvolutions(database);
        database.shutdown();
    }

    @Test
    public void testBadSession(){

        Map<String, String> session = new HashMap<>();
        session.put("username", newEmail);

        Map<String, String> data = new HashMap<>();

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().bodyForm(data).method(POST).uri(
                                controllers.userAdmin.routes.CreateUserController.createUser().url()
                        )
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void testNotAdminSession(){

        UserModel userModel = UserModel.FINDER.byId(id);
        userModel.isAdmin = false;
        userModel.update();

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().bodyForm(data).method(POST).uri(
                                controllers.userAdmin.routes.CreateUserController.createUser().url()
                        ).session(session)
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void testEmptyData(){

        UserModel userModel = UserModel.FINDER.byId(id);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().bodyForm(data).method(POST).uri(
                                controllers.userAdmin.routes.CreateUserController.createUser().url()
                        ).session(session)
                )
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(result.flash().containsKey("danger")).isEqualTo(true);
        assertThat(result.flash().get("danger")).isEqualTo("User has not been created. Please try again.");
    }

    @Test
    public void createUserTest(){

        UserModel userModel = UserModel.FINDER.byId(id);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();
        data.put("username", newUsername);
        data.put("email", newEmail);
        data.put("phone", newPhone);
        data.put("password", newPassword);
        data.put("confirmPassword", newPassword);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().bodyForm(data).method(POST).uri(
                                controllers.userAdmin.routes.CreateUserController.createUser().url()
                        ).session(session)
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
        assertThat(result.flash().containsKey("success")).isEqualTo(true);
        assertThat(result.flash().get("success")).isEqualTo("User has been created.");
    }

    @Test
    public void createUserWithExistsEmail(){

        UserModel userModel = UserModel.FINDER.byId(id);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();
        data.put("username", newUsername);
        data.put("email", userModel.email);
        data.put("phone", newPhone);
        data.put("password", newPassword);
        data.put("confirmPassword", newPassword);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().bodyForm(data).method(POST).uri(
                                controllers.userAdmin.routes.CreateUserController.createUser().url()
                        )
                ).session(session)
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(result.flash().containsKey("danger")).isEqualTo(true);
        assertThat(result.flash().get("danger")).isEqualTo("User has not been created. Please try again.");
        assertThat(result.flash().containsKey("emailWarning")).isEqualTo(true);
        assertThat(result.flash().get("emailWarning")).isEqualTo(
                "Sorry. Email " +userModel.email+ " exists. Please try again."
        );
    }

    @Test
    public void createUserWithExistsPhone(){

        UserModel userModel = UserModel.FINDER.byId(id);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();
        data.put("username", newUsername);
        data.put("email", newEmail);
        data.put("phone", userModel.phone);
        data.put("password", newPassword);
        data.put("confirmPassword", newPassword);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().bodyForm(data).method(POST).uri(
                                controllers.userAdmin.routes.CreateUserController.createUser().url()
                        ).session(session)
                )
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(result.flash().containsKey("danger")).isEqualTo(true);
        assertThat(result.flash().get("danger")).isEqualTo("User has not been created. Please try again.");
        assertThat(result.flash().containsKey("phoneWarning")).isEqualTo(true);
        assertThat(result.flash().get("phoneWarning")).isEqualTo(
                "Sorry. Phone " +userModel.phone+ " exists. Please try again."
        );
    }

    @Test
    public void createUserWithBadPasswordConfirmation(){

        UserModel userModel = UserModel.FINDER.byId(id);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();
        data.put("username", newUsername);
        data.put("email", newEmail);
        data.put("phone", newPhone);
        data.put("password", newPassword);
        data.put("confirmPassword", badPassword);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().bodyForm(data).method(POST).uri(
                                controllers.userAdmin.routes.CreateUserController.createUser().url()
                        ).session(session)
                )
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(result.flash().containsKey("danger")).isEqualTo(true);
        assertThat(result.flash().get("danger")).isEqualTo("User has not been created. Please try again.");
        assertThat(result.flash().containsKey("confirmPasswordWarning")).isEqualTo(true);
        assertThat(result.flash().get("confirmPasswordWarning")).isEqualTo("Passwords mismatch. Please try again.");
    }
}
