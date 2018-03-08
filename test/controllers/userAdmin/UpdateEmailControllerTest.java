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

public class UpdateEmailControllerTest extends WithApplication {

    private Long id = 1L;

    private Long existsId = 2L;

    private Long notExistsId = 3L;

    private String existsEmail = "john@smith.com";
    private String newEmail = "john1@smith.com";

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


        Map<String, String> data = new HashMap<>();
        data.put("email", newEmail);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().method(POST).bodyForm(data).uri(
                                controllers.userAdmin.routes.UpdateEmailController.updateEmail(existsId).url()
                        )
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void testInvalidSession(){

        Map<String, String> session = new HashMap<>();
        session.put("username", newEmail);

        Map<String, String> data = new HashMap<>();
        data.put("email", newEmail);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().method(POST).bodyForm(data).uri(
                                controllers.userAdmin.routes.UpdateEmailController.updateEmail(existsId).url()
                        ).session(session)
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void notAdminSession(){

        UserModel userModel = UserModel.FINDER.byId(id);
        userModel.isAdmin = false;
        userModel.updatedAt = new Date();
        userModel.update();

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();
        data.put("email", newEmail);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().method(POST).bodyForm(data).uri(
                                controllers.userAdmin.routes.UpdateEmailController.updateEmail(existsId).url()
                        ).session(session)
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void testWithUserIsAdminAndUserIdNotExists(){

        UserModel userModel = UserModel.FINDER.byId(id);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();
        data.put("email", newEmail);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().method(POST).bodyForm(data).uri(
                                controllers.userAdmin.routes.UpdateEmailController.updateEmail(notExistsId).url()
                        ).session(session)
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void testWithEmptyEmailForm(){

        UserModel userModel = UserModel.FINDER.byId(id);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().method(POST).bodyForm(data).uri(
                                controllers.userAdmin.routes.UpdateEmailController.updateEmail(existsId).url()
                        ).session(session)
                )
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(result.flash().containsKey("emailDanger"));
        assertThat(result.flash().get("emailDanger"))
                .isEqualTo("There were errors submitting the form with the email. Please try again.");
    }

    @Test
    public void testWithEmailsAreTheSame(){

        UserModel userModel = UserModel.FINDER.byId(id);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();
        data.put("email", existsEmail);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().method(POST).bodyForm(data).uri(
                                controllers.userAdmin.routes.UpdateEmailController.updateEmail(existsId).url()
                        ).session(session)
                )
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(result.flash().containsKey("emailDanger"));
        assertThat(result.flash().get("emailDanger"))
                .isEqualTo("There were errors while updating the email. Please try again.");
        assertThat(result.flash().containsKey("emailDanger"));
        assertThat(result.flash().get("emailWarning"))
                .isEqualTo("Old and new e-mail are the same.");
    }

    @Test
    public void testWithExistsEmail(){

        UserModel userModel = UserModel.FINDER.byId(id);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();
        data.put("email", userModel.email);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().method(POST).bodyForm(data).uri(
                                controllers.userAdmin.routes.UpdateEmailController.updateEmail(existsId).url()
                        ).session(session)
                )
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(result.flash().containsKey("emailDanger"));
        assertThat(result.flash().get("emailDanger"))
                .isEqualTo("There were errors while updating the email. Please try again.");
        assertThat(result.flash().containsKey("emailDanger"));
        assertThat(result.flash().get("emailWarning"))
                .isEqualTo("E-mail exists. Please choose other e-mail address.");
    }

    @Test
    public void testSuccessUpdateEmail(){

        UserModel userModel = UserModel.FINDER.byId(id);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();
        data.put("email", newEmail);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().method(POST).bodyForm(data).uri(
                                controllers.userAdmin.routes.UpdateEmailController.updateEmail(existsId).url()
                        ).session(session)
                )
        );

        assertThat(result.status()).isEqualTo(OK);
        assertThat(result.flash().containsKey("emailSuccess"));
        assertThat(result.flash().get("emailSuccess"))
                .isEqualTo("E-mail has been updated.");
    }
}
