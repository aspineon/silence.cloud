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
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.GET;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;

public class DeleteUserControllerTest extends WithApplication {

    private Long id = 1L;
    private Long existsId = 2L;
    private Long notExistsId = 3L;

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

        Result result = route(
                app, fakeRequest().session(session).method(GET).uri(
                        controllers.userAdmin.routes.DeleteUserController.deleteUser(existsId).url()
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void userIsNotAdmin(){

        UserModel userModel = UserModel.FINDER.byId(id);
        userModel.isAdmin = false;
        userModel.update();

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Result result = route(
                app, fakeRequest().session(session).method(GET).uri(
                        controllers.userAdmin.routes.DeleteUserController.deleteUser(existsId).url()
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void userToDeleteNotFound(){

        UserModel userModel = UserModel.FINDER.byId(id);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Result result = route(
                app, fakeRequest().session(session).method(GET).uri(
                        controllers.userAdmin.routes.DeleteUserController.deleteUser(notExistsId).url()
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
        assertThat(result.flash().containsKey("danger")).isEqualTo(true);
        assertThat(result.flash().get("danger")).isEqualTo("User not found. Please try again.");

    }

    @Test
    public void deleteUserByCurrentUserAdminId(){

        UserModel userModel = UserModel.FINDER.byId(id);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Result result = route(
                app, fakeRequest().session(session).method(GET).uri(
                        controllers.userAdmin.routes.DeleteUserController.deleteUser(id).url()
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
        assertThat(result.flash().containsKey("warning")).isEqualTo(true);
        assertThat(result.flash().get("warning")).isEqualTo("User has not been deleted. Please try again.");
    }

    @Test
    public void successToDeleteUser(){

        UserModel userModel = UserModel.FINDER.byId(id);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Result result = route(
                app, fakeRequest().session(session).method(GET).uri(
                        controllers.userAdmin.routes.DeleteUserController.deleteUser(existsId).url()
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
        assertThat(result.flash().containsKey("success")).isEqualTo(true);
        assertThat(result.flash().get("success")).isEqualTo("User has been deleted.");
    }

    @Test
    public void userIsNotActive(){

        UserModel userModel = UserModel.FINDER.byId(id);
        userModel.isActive = false;
        userModel.updatedAt = new Date();
        userModel.save();

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Result result = route(
                app, fakeRequest().session(session).method(GET).uri(
                        controllers.userAdmin.routes.DeleteUserController.deleteUser(existsId).url()
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }
}
