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
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.GET;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;

public class EditUserControllerTest extends WithApplication {

    private Long id = 1L;

    private Long existsId = 2L;

    private Long notExistsId = 3L;

    private String badEmail = "email@example.com";

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
    public void noSession(){

        Map<String, String> data = new HashMap<>();

        Result result = route(
                app, fakeRequest().bodyForm(data).method(GET).uri(
                        controllers.userAdmin.routes.EditUserController.editUser(existsId).url()
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void testBadSession(){

        Map<String, String> session = new HashMap<>();
        session.put("username", badEmail);

        Map<String, String> data = new HashMap<>();

        Result result = route(
                app, fakeRequest().bodyForm(data).method(GET).uri(
                        controllers.userAdmin.routes.EditUserController.editUser(existsId).url()
                ).session(session)
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void testBadPrivilleges(){

        UserModel userModel = UserModel.FINDER.byId(id);
        userModel.isAdmin = false;
        userModel.update();

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();

        Result result = route(
                app, fakeRequest().bodyForm(data).method(GET).uri(
                        controllers.userAdmin.routes.EditUserController.editUser(existsId).url()
                ).session(session)
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void testGoodUser(){

        Result result = getResult(existsId);

        assertThat(result.status()).isEqualTo(OK);
    }

    @Test
    public void testNotExistsId(){

        Result result = getResult(notExistsId);

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void testCurrentId(){

        Result result = getResult(id);

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    private Result getResult(Long editUserId){

        UserModel userModel = UserModel.FINDER.byId(id);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();

        return route(
                app, fakeRequest().bodyForm(data).method(GET).uri(
                        controllers.userAdmin.routes.EditUserController.editUser(editUserId).url()
                ).session(session)
        );
    }
}
