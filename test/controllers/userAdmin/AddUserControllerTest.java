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
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;

public class AddUserControllerTest extends WithApplication {

    private Long firstId = 1L;

    private String notExistsEmail = "email@example.com";

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
    public void testBadUserSession(){

        Map<String, String> session = new HashMap<>();
        session.put("username", notExistsEmail);

        Result result = route(
                app, fakeRequest().session(session).uri(
                        controllers.userAdmin.routes.AddUserController.addUserForm().url()
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void testNotAdminSession(){

        UserModel userModel = UserModel.FINDER.byId(firstId);
        userModel.isAdmin = false;
        userModel.update();

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Result result = route(
                app, fakeRequest().session(session).uri(
                        controllers.userAdmin.routes.AddUserController.addUserForm().url()
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void testAdminSession(){

        UserModel userModel = UserModel.FINDER.byId(firstId);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Result result = route(
                app, fakeRequest().session(session).uri(
                        controllers.userAdmin.routes.AddUserController.addUserForm().url()
                )
        );

        assertThat(result.status()).isEqualTo(OK);
    }

    @Test
    public void userIsNotActive(){

        UserModel userModel = UserModel.FINDER.byId(firstId);
        userModel.isActive = false;
        userModel.updatedAt = new Date();
        userModel.save();

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Result result = route(
                app, fakeRequest().session(session).uri(
                        controllers.userAdmin.routes.AddUserController.addUserForm().url()
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }
}
