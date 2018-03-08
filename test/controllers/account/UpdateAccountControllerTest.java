package controllers.account;

import com.google.common.collect.ImmutableMap;

import helpers.DefaultUsers;
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
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.POST;
import static play.test.Helpers.fakeRequest;
import static play.api.test.CSRFTokenHelper.addCSRFToken;
import static play.test.Helpers.route;

public class UpdateAccountControllerTest extends WithApplication {

    private String  firstUsername       = "John Doe";
    private String  firstEmail          = "john@doe.com";
    private String  firstPhone          = "000000000";

    private String  secondEmail         = "john@smith.com";
    private String  secondPhone         = "1111111111";

    private String notExistsUserName    = "r3v";
    private String notExistsEmail       = "jonathan@doe.com";
    private String notExistsPhone       = "000000001";

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
    public void unauthorizedTest(){

        Map<String, String> session = new HashMap<>();
        session.put("username", notExistsEmail);

        Result result = route(
                app, addCSRFToken(fakeRequest().method(POST).uri(routes.UpdateAccountController.updateAccount().url()))
                        .session(session)
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void emptyDataTest(){

        Map<String, String> session = new HashMap<>();
        session.put("username", firstEmail);

        Map<String, String> data = new HashMap<>();

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().bodyForm(data).method(POST).uri(
                                routes.UpdateAccountController.updateAccount().url()
                        ).session(session)
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
        assertThat(result.flash().containsKey("danger"));
        assertThat(result.flash().get("danger")).isEqualTo("Account has not been updated.");
    }

    @Test
    public void updateWithCurrentUsernameTest(){

        Map<String, String> session = new HashMap<>();
        session.put("username", firstEmail);

        Map<String, String> data = new HashMap<>();
        data.put("username", firstUsername);
        data.put("email", notExistsEmail);
        data.put("phone", notExistsPhone);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().bodyForm(data).method(POST).uri(
                                routes.UpdateAccountController.updateAccount().url()
                        ).session(session)
                )
        );

        assertThat(result.status()).isEqualTo(OK);
        assertThat(result.flash().containsKey("success")).isEqualTo(true);
        assertThat(result.flash().get("success")).isEqualTo("Account has been updated.");

        assertThat(result.flash().containsKey("usernameWarning")).isEqualTo(true);
        assertThat(result.flash().get("usernameWarning")).isEqualTo("Old and new usernames are the same.");
    }

    @Test
    public void updateWithCurrentEmailTest(){

        Map<String, String> session = new HashMap<>();
        session.put("username", firstEmail);

        Map<String, String> data = new HashMap<>();
        data.put("username", notExistsUserName);
        data.put("email", firstEmail);
        data.put("phone", notExistsPhone);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().bodyForm(data).method(POST).uri(
                                routes.UpdateAccountController.updateAccount().url()
                        ).session(session)
                )
        );

        assertThat(result.status()).isEqualTo(OK);
        assertThat(result.flash().containsKey("success")).isEqualTo(true);
        assertThat(result.flash().get("success")).isEqualTo("Account has been updated.");

        assertThat(result.flash().containsKey("emailWarning")).isEqualTo(true);
        assertThat(result.flash().get("emailWarning")).isEqualTo("Old and new email are the same.");
    }

    @Test
    public void updateWithCurrentPhoneTest(){

        Map<String, String> session = new HashMap<>();
        session.put("username", firstEmail);

        Map<String, String> data = new HashMap<>();
        data.put("username", notExistsUserName);
        data.put("email", notExistsEmail);
        data.put("phone", firstPhone);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().bodyForm(data).method(POST).uri(
                                routes.UpdateAccountController.updateAccount().url()
                        ).session(session)
                )
        );

        assertThat(result.status()).isEqualTo(OK);
        assertThat(result.flash().containsKey("success")).isEqualTo(true);
        assertThat(result.flash().get("success")).isEqualTo("Account has been updated.");

        assertThat(result.flash().containsKey("phoneWarning")).isEqualTo(true);
        assertThat(result.flash().get("phoneWarning")).isEqualTo("Old and new phones are the same.");
    }

    @Test
    public void updateWithExistsEmailTest(){

        Map<String, String> session = new HashMap<>();
        session.put("username", firstEmail);

        Map<String, String> data = new HashMap<>();
        data.put("username", notExistsUserName);
        data.put("email", secondEmail);
        data.put("phone", notExistsPhone);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().bodyForm(data).method(POST).uri(
                                routes.UpdateAccountController.updateAccount().url()
                        ).session(session)
                )
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(result.flash().containsKey("danger")).isEqualTo(true);
        assertThat(result.flash().get("danger")).isEqualTo("Account has not been updated.");

        assertThat(result.flash().containsKey("emailDanger")).isEqualTo(true);
        assertThat(result.flash().get("emailDanger")).isEqualTo("Sorry. Email " +secondEmail+ " exists. Please try again.");
    }

    @Test
    public void updateWithExistsPhoneTest(){

        Map<String, String> session = new HashMap<>();
        session.put("username", firstEmail);

        Map<String, String> data = new HashMap<>();
        data.put("username", notExistsUserName);
        data.put("email", notExistsEmail);
        data.put("phone", secondPhone);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().bodyForm(data).method(POST).uri(
                                routes.UpdateAccountController.updateAccount().url()
                        ).session(session)
                )
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(result.flash().containsKey("danger")).isEqualTo(true);
        assertThat(result.flash().get("danger")).isEqualTo("Account has not been updated.");

        assertThat(result.flash().containsKey("phoneDanger")).isEqualTo(true);
        assertThat(result.flash().get("phoneDanger")).isEqualTo("Sorry. Phone " +secondPhone+ " exists. Please try again.");
    }

    @Test
    public void successOfUpdateAccount(){

        Map<String, String> session = new HashMap<>();
        session.put("username", firstEmail);

        Map<String, String> data = new HashMap<>();
        data.put("username", notExistsUserName);
        data.put("email", notExistsEmail);
        data.put("phone", notExistsPhone);

        Result result = route(
                app, addCSRFToken(
                        fakeRequest().bodyForm(data).method(POST).uri(
                                routes.UpdateAccountController.updateAccount().url()
                        ).session(session)
                )
        );

        assertThat(result.status()).isEqualTo(OK);
        assertThat(result.flash().containsKey("success")).isEqualTo(true);
        assertThat(result.flash().get("success")).isEqualTo("Account has been updated.");
    }
}
