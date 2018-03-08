package controllers.account;

import com.google.common.collect.ImmutableMap;
import helpers.BeforeAndAfterTest;
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
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.GET;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;

public class UserControllerTest extends WithApplication implements UserByIdFindable {

    private Long userId = 1L;

    private String notExistsUsername = "email@example.com";

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
    public void authUserTest(){

        Map<String, String> session = new HashMap<>();
        session.put("username", UserByIdFindable.super.findUserById(userId).get().email);

        Result result = route(
                app, fakeRequest().method(GET).uri(routes.UserController.getUser().url()).session(session)
        );

        assertThat(result.status()).isEqualTo(OK);
    }

    @Test
    public void notExistsSessionTest(){

        Map<String, String> session = new HashMap<>();
        session.put("username", notExistsUsername);

        Result result = route(
                app, fakeRequest().method(GET).uri(routes.UserController.getUser().url()).session(session)
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }
}
