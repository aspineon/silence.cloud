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
import static play.test.Helpers.*;

public class UpdateUsernameControllerTest extends WithApplication {

    private Long id = 1L;

    private Long existsId = 2L;

    private Long notExistsId = 3L;

    private String existsUsername = "John Smith";
    private String newUsername = "John Joseph Smith";

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
    public void sessionNotExistsTest(){

        Map<String, String> data = new HashMap<>();
        data.put("username", newUsername);

        Result result = route(app, addCSRFToken(
                fakeRequest().bodyForm(data).method(POST).uri(
                        controllers.userAdmin.routes.UpdateUsernameController.updateUsername(existsId).url())
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void sessionExistsAndUserIsNotAdmin(){

        UserModel userModel = UserModel.FINDER.byId(id);
        userModel.isAdmin = false;
        userModel.updatedAt = new Date();
        userModel.update();

        Map<String, String> data = new HashMap<>();
        data.put("username", newUsername);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Result result = route(app, addCSRFToken(
                fakeRequest().bodyForm(data).session(session).method(POST).uri(
                        controllers.userAdmin.routes.UpdateUsernameController.updateUsername(existsId).url())
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void sessionExistsAndUserIdAdminAndBadUserId(){

        UserModel userModel = UserModel.FINDER.byId(id);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();
        data.put("username", newUsername);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().method(POST).session(session).bodyForm(data).uri(
                                controllers.userAdmin.routes.UpdateUsernameController.updateUsername(notExistsId).url()
                        )
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void sessionExistsAndUserIdAdminAndUpdateUserIdIsTheSameHowAdminId(){

        UserModel userModel = UserModel.FINDER.byId(id);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();
        data.put("username", newUsername);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().method(POST).session(session).bodyForm(data).uri(
                                controllers.userAdmin.routes.UpdateUsernameController.updateUsername(userModel.id).url()
                        )
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void sessionExistsUserAndUserIsAdminAndUsernameFormIsNull(){

        UserModel userModel = UserModel.FINDER.byId(id);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();

        Result result = route(
                app, addCSRFToken(fakeRequest().bodyForm(data).session(session).method(POST).uri(
                        controllers.userAdmin.routes.UpdateUsernameController.updateUsername(existsId).url())
                )
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(result.flash().containsKey("usernameDanger")).isEqualTo(true);
        assertThat(result.flash().get("usernameDanger"))
                .isEqualTo("There were errors submitting the form with the username. Please try again.");
    }

    @Test
    public void sessionExistsAndUserIdAdminAndOldUsernameIsTheSameHowNewUsername() {

        UserModel userModel = UserModel.FINDER.byId(id);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();
        data.put("username", existsUsername);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().method(POST).session(session).bodyForm(data).uri(
                                controllers.userAdmin.routes.UpdateUsernameController.updateUsername(existsId)
                                        .url()
                        )
                )
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(result.flash().containsKey("usernameDanger")).isEqualTo(true);
        assertThat(result.flash().get("usernameDanger"))
                .isEqualTo("There were errors while updating the username. Please try again.");
        assertThat(result.flash().containsKey("usernameWarning")).isEqualTo(true);
        assertThat(result.flash().get("usernameWarning"))
                .isEqualTo("The old and the new user name are identical.");
    }

    @Test
    public void successToUpdateUsername() {

        UserModel userModel = UserModel.FINDER.byId(id);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();
        data.put("username", newUsername);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().method(POST).session(session).bodyForm(data).uri(
                                controllers.userAdmin.routes.UpdateUsernameController.updateUsername(existsId)
                                        .url()
                        )
                )
        );

        assertThat(result.status()).isEqualTo(OK);
        assertThat(result.flash().containsKey("usernameSuccess")).isEqualTo(true);
        assertThat(result.flash().get("usernameSuccess"))
                .isEqualTo("The username has been updated successfully.");
    }
}
