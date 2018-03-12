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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static play.api.test.CSRFTokenHelper.addCSRFToken;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.POST;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;

public class UpdatePasswordControllerTest extends WithApplication {

    private Long id = 1L;
    private Long existsId = 2L;
    private Long notExistsId = 3L;

    private String notExistsEmail = "email@example.com";

    private String existsPassword = "R3v3l@t104LoA";
    private String newPassword = "Alph@Bet@123";
    private String confirmPassword = "Alph@Bet@12";

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
    public void testNoSession(){

        Map<String, String> session = new HashMap<>();

        Map<String, String> data = new HashMap<>();
        data.put("password", newPassword);
        data.put("confirmPassword", confirmPassword);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().method(POST).bodyForm(data).session(session).uri(
                                controllers.userAdmin.routes.UpdatePasswordController.updatePassword(existsId).url()
                        )
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void testBadSession(){

        Map<String, String> session = new HashMap<>();
        session.put("username", notExistsEmail);

        Map<String, String> data = new HashMap<>();
        data.put("password", newPassword);
        data.put("confirmPassword", confirmPassword);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().method(POST).bodyForm(data).session(session).uri(
                                controllers.userAdmin.routes.UpdatePasswordController.updatePassword(existsId).url()
                        )
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void testBadAdminSession(){

        UserModel userModel = UserModel.FINDER.byId(id);
        userModel.isAdmin = false;
        userModel.updatedAt = new Date();
        userModel.update();

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();
        data.put("password", newPassword);
        data.put("confirmPassword", confirmPassword);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().method(POST).bodyForm(data).session(session).uri(
                                controllers.userAdmin.routes.UpdatePasswordController.updatePassword(existsId).url()
                        )
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void updateNotExistsUser(){

        UserModel userModel = UserModel.FINDER.byId(id);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();
        data.put("password", newPassword);
        data.put("confirmPassword", confirmPassword);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().method(POST).bodyForm(data).session(session).uri(
                                controllers.userAdmin.routes.UpdatePasswordController.updatePassword(notExistsId).url()
                        )
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void updateCurrentAdminUser(){

        UserModel userModel = UserModel.FINDER.byId(id);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();
        data.put("password", newPassword);
        data.put("confirmPassword", confirmPassword);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().method(POST).bodyForm(data).session(session).uri(
                                controllers.userAdmin.routes.UpdatePasswordController.updatePassword(userModel.id).url()
                        )
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void updatePasswordWithEmptyData(){

        UserModel userModel = UserModel.FINDER.byId(id);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().method(POST).bodyForm(data).session(session).uri(
                                controllers.userAdmin.routes.UpdatePasswordController.updatePassword(existsId).url()
                        )
                )
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(result.flash().containsKey("passwordDanger")).isEqualTo(true);
        assertThat(result.flash().get("passwordDanger"))
                .isEqualTo("There were errors submitting the form with the password. Please try again.");
    }

    @Test
    public void updatePasswordWhenOldAndNewPasswordAreTheSame(){

        UserModel userModel = UserModel.FINDER.byId(id);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();
        data.put("password", existsPassword);
        data.put("confirmPassword", existsPassword);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().method(POST).bodyForm(data).session(session).uri(
                                controllers.userAdmin.routes.UpdatePasswordController.updatePassword(existsId).url()
                        )
                )
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(result.flash().containsKey("passwordDanger")).isEqualTo(true);
        assertThat(result.flash().get("passwordDanger"))
                .isEqualTo("There were errors while updating the password. Please try again.");
        assertThat(result.flash().containsKey("passwordWarning")).isEqualTo(true);
        assertThat(result.flash().get("passwordWarning")).isEqualTo("Old and new password are the same.");
    }

    @Test
    public void newPasswordAndOldPasswordNotMatching(){

        UserModel userModel = UserModel.FINDER.byId(id);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();
        data.put("password", newPassword);
        data.put("confirmPassword", confirmPassword);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().method(POST).bodyForm(data).session(session).uri(
                                controllers.userAdmin.routes.UpdatePasswordController.updatePassword(existsId).url()
                        )
                )
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(result.flash().containsKey("passwordDanger")).isEqualTo(true);
        assertThat(result.flash().get("passwordDanger"))
                .isEqualTo("There were errors while updating the password. Please try again.");
        assertThat(result.flash().containsKey("passwordWarning")).isEqualTo(true);
        assertThat(result.flash().get("passwordWarning")).isEqualTo("New and confirm passwords mismatch.");
    }

    @Test
    public void passwordHasBeenUpdated(){

        UserModel userModel = UserModel.FINDER.byId(id);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();
        data.put("password", newPassword);
        data.put("confirmPassword", newPassword);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().method(POST).bodyForm(data).session(session).uri(
                                controllers.userAdmin.routes.UpdatePasswordController.updatePassword(existsId).url()
                        )
                )
        );

        assertThat(result.status()).isEqualTo(OK);
        assertThat(result.flash().containsKey("passwordSuccess")).isEqualTo(true);
        assertThat(result.flash().get("passwordSuccess"))
                .isEqualTo("Password has been updated.");
    }

    @Test
    public void userIsNotActive(){

        UserModel userModel = UserModel.FINDER.byId(id);
        userModel.isActive = false;
        userModel.update();

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();
        data.put("password", newPassword);
        data.put("confirmPassword", newPassword);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().method(POST).bodyForm(data).session(session).uri(
                                controllers.userAdmin.routes.UpdatePasswordController.updatePassword(existsId).url()
                        )
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }
}
