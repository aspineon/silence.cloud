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

public class UpdatePhoneControllerTest extends WithApplication {

    private Long adminId = 1L;
    private Long existsId = 2L;
    private Long notExistsId = 3L;

    private String badEmail = "email@example.com";

    private String newPhone = "22222222222";

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
    public void testEmptySession(){

        Map<String, String> session = new HashMap<>();

        Map<String, String> data = new HashMap<>();
        data.put("phone", newPhone);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().session(session).bodyForm(data).uri(
                                controllers.userAdmin.routes.UpdatePhoneController.updatePhone(existsId).url()
                        ).method(POST)
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void testBadSession(){

        Map<String, String> session = new HashMap<>();
        session.put("username", badEmail);

        Map<String, String> data = new HashMap<>();
        data.put("phone", newPhone);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().session(session).bodyForm(data).uri(
                                controllers.userAdmin.routes.UpdatePhoneController.updatePhone(existsId).url()
                        ).method(POST)
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void testUserIsNotAdmin(){

        UserModel userModel = UserModel.FINDER.byId(adminId);
        userModel.isAdmin = false;
        userModel.updatedAt = new Date();
        userModel.update();

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();
        data.put("phone", newPhone);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().session(session).bodyForm(data).uri(
                                controllers.userAdmin.routes.UpdatePhoneController.updatePhone(existsId).url()
                        ).method(POST)
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void updatePhoneForNotExistsAccount(){

        UserModel userModel = UserModel.FINDER.byId(adminId);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();
        data.put("phone", newPhone);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().session(session).bodyForm(data).uri(
                                controllers.userAdmin.routes.UpdatePhoneController.updatePhone(notExistsId).url()
                        ).method(POST)
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void currentUserAccountAndUpdateUserAccountAreTheSame(){

        UserModel userModel = UserModel.FINDER.byId(adminId);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();
        data.put("phone", newPhone);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().session(session).bodyForm(data).uri(
                                controllers.userAdmin.routes.UpdatePhoneController.updatePhone(userModel.id).url()
                        ).method(POST)
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void testUpdatePhoneWithEmptyData(){

        UserModel userModel = UserModel.FINDER.byId(adminId);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().session(session).bodyForm(data).uri(
                                controllers.userAdmin.routes.UpdatePhoneController.updatePhone(existsId).url()
                        ).method(POST)
                )
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(result.flash().containsKey("phoneDanger")).isEqualTo(true);
        assertThat(result.flash().get("phoneDanger"))
                .isEqualTo("There were errors submitting the form with the phone. Please try again.");
    }

    @Test
    public void testUpdatePhoneWithOldDataData(){

        UserModel userModel = UserModel.FINDER.byId(adminId);

        UserModel currentUser = UserModel.FINDER.byId(existsId);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();
        data.put("phone", currentUser.phone);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().session(session).bodyForm(data).uri(
                                controllers.userAdmin.routes.UpdatePhoneController.updatePhone(existsId).url()
                        ).method(POST)
                )
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(result.flash().containsKey("phoneDanger")).isEqualTo(true);
        assertThat(result.flash().get("phoneDanger"))
                .isEqualTo("There were errors while updating the email. Please try again.");
        assertThat(result.flash().containsKey("phoneWarning")).isEqualTo(true);
        assertThat(result.flash().get("phoneWarning"))
                .isEqualTo("Old and new phone are the same.");

    }

    @Test
    public void testUpdatePhoneWithExistsData(){

        UserModel userModel = UserModel.FINDER.byId(adminId);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();
        data.put("phone", userModel.phone);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().session(session).bodyForm(data).uri(
                                controllers.userAdmin.routes.UpdatePhoneController.updatePhone(existsId).url()
                        ).method(POST)
                )
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(result.flash().containsKey("phoneDanger")).isEqualTo(true);
        assertThat(result.flash().get("phoneDanger"))
                .isEqualTo("There were errors while updating the email. Please try again.");
        assertThat(result.flash().containsKey("phoneWarning")).isEqualTo(true);
        assertThat(result.flash().get("phoneWarning"))
                .isEqualTo("Phone exists. Please choose other phone address.");

    }

    @Test
    public void testSuccessToUpdatePhone(){

        UserModel userModel = UserModel.FINDER.byId(adminId);

        Map<String, String> session = new HashMap<>();
        session.put("username", userModel.email);

        Map<String, String> data = new HashMap<>();
        data.put("phone", newPhone);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().session(session).bodyForm(data).uri(
                                controllers.userAdmin.routes.UpdatePhoneController.updatePhone(existsId).url()
                        ).method(POST)
                )
        );

        assertThat(result.status()).isEqualTo(OK);
        assertThat(result.flash().containsKey("phoneSuccess")).isEqualTo(true);
        assertThat(result.flash().get("phoneSuccess"))
                .isEqualTo("Phone has been updated.");
    }

}
