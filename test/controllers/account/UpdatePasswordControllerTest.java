package controllers.account;

import com.google.common.collect.ImmutableMap;
import helpers.DefaultUsers;
import models.core.user.UserByIdFindable;
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
import static play.mvc.Http.HttpVerbs.POST;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;

public class UpdatePasswordControllerTest extends WithApplication implements UserByIdFindable {

    private Long id = 1L;

    private String notExistsEmail = "mail@example.com";

    private String oldPassword = "R3v3l@t104LoA";
    private String badOldPassword = "Rev3l@t104LoA";
    private String newPassword = "R3v3l@t104Loa";
    private String badConfirmPassword = "R3v3l@t104Lo1";

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
    public void testNotAuthentiacated(){

        Map<String, String> session = new HashMap<>();
        Map<String, String> data = new HashMap<>();
        data.put("username", notExistsEmail);

        Result result = route(app, addCSRFToken(fakeRequest().method(POST).uri(
                controllers.account.routes.UpdatePasswordController.updatePassword().url()
        )).session(session));

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void emptyData(){

        UserModel userModel = UserByIdFindable.super.findUserById(id).get();
        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);
        Map<String, String> data = new HashMap<>();

        Result result = route(app, addCSRFToken(fakeRequest().method(POST).uri(
                controllers.account.routes.UpdatePasswordController.updatePassword().url()
        )).session(session).bodyForm(data));

        assertThat(result.status()).isEqualTo(SEE_OTHER);
        assertThat(result.flash().get("danger")).isEqualTo("Password has not been updated.");
    }

    @Test
    public void testBadOldPassword(){

        UserModel userModel = UserByIdFindable.super.findUserById(id).get();
        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);
        Map<String, String> data = new HashMap<>();
        data.put("oldPassword", badOldPassword);
        data.put("newPassword", newPassword);
        data.put("confirmPassword", newPassword);

        Result result = route(app, addCSRFToken(fakeRequest().method(POST).uri(
                controllers.account.routes.UpdatePasswordController.updatePassword().url()
        )).session(session).bodyForm(data));

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(result.flash().get("danger")).isEqualTo("Password has not been updated.");
        assertThat(result.flash().get("oldPasswordWarning")).isEqualTo("Bad old password. Please try again.");
    }

    @Test
    public void passwordsMismatch(){

        UserModel userModel = UserByIdFindable.super.findUserById(id).get();
        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);
        Map<String, String> data = new HashMap<>();
        data.put("oldPassword", oldPassword);
        data.put("newPassword", newPassword);
        data.put("confirmPassword", badConfirmPassword);

        Result result = route(app, addCSRFToken(fakeRequest().method(POST).uri(
                controllers.account.routes.UpdatePasswordController.updatePassword().url()
        )).session(session).bodyForm(data));

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(result.flash().get("danger")).isEqualTo("Password has not been updated.");
        assertThat(result.flash().get("passwordsMismatchWarning")).isEqualTo("Passwords mismatch. Please try again.");
    }

    @Test
    public void testUpdatePassword(){

        UserModel userModel = UserByIdFindable.super.findUserById(id).get();
        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);
        Map<String, String> data = new HashMap<>();
        data.put("oldPassword", oldPassword);
        data.put("newPassword", newPassword);
        data.put("confirmPassword", newPassword);

        Result result = route(app, addCSRFToken(fakeRequest().method(POST).uri(
                controllers.account.routes.UpdatePasswordController.updatePassword().url()
        )).session(session).bodyForm(data));

        assertThat(result.status()).isEqualTo(OK);
        assertThat(result.flash().get("success")).isEqualTo("Password has been updated.");
    }

    @Test
    public void userIsInactiveTest(){

        UserModel userModel = UserByIdFindable.super.findUserById(id).get();
        userModel.isActive = false;
        userModel.update();

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);
        Map<String, String> data = new HashMap<>();
        data.put("oldPassword", oldPassword);
        data.put("newPassword", newPassword);
        data.put("confirmPassword", newPassword);

        Result result = route(app, addCSRFToken(fakeRequest().method(POST).uri(
                controllers.account.routes.UpdatePasswordController.updatePassword().url()
        )).session(session).bodyForm(data));

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }
}
