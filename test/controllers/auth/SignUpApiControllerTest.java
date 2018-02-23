package controllers.auth;

import models.core.user.UserModel;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import play.mvc.Result;
import play.test.WithApplication;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.POST;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SignUpApiControllerTest extends WithApplication {

    private String username = "John Doe";
    private String password = "R3v3l@t104LoA";

    private String badConfirmPassword = "R3v3l@t104";

    private String existsEmail = "john@doe.com";
    private String existsPhone = "000000000";

    private String notExistsEmail = "john1@doe.com";
    private String notExistsPhone = "000000001";

    @Before
    public void setUp(){
        CreateDefaultUser createDefaultUser = new CreateDefaultUser();
        createDefaultUser.createDefaultUser();
    }

    @After
    public void tearDown(){
        UserModel.FINDER.query().where().eq("email", existsEmail.toLowerCase()).findOne().delete();
    }

    @Test
    public void createUserWithEmptyData(){

        Map<String, String> data = new HashMap<>();
        Result result = route(
                app, fakeRequest().bodyForm(data).method(POST)
                        .uri(controllers.auth.routes.SignUpApiController.signUpAction().url())
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
    }

    @Test
    public void createUser(){

        SignUpControllerTest signUpControllerTest = new SignUpControllerTest();
        Map<String, String> data = signUpControllerTest.createDataFromRequest(notExistsEmail, notExistsPhone);

        Result result = route(
                app, fakeRequest().bodyForm(data).method(POST)
                        .uri(controllers.auth.routes.SignUpApiController.signUpAction().url())
        );

        assertThat(result.status()).isEqualTo(OK);
    }

    @Test
    public void createUserWhenPasswordsMismatch() {

        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("email", notExistsEmail);
        data.put("phone", notExistsPhone);
        data.put("password", password);
        data.put("confirmPassword", badConfirmPassword);

        Result result = route(
                app, fakeRequest().bodyForm(data).method(POST)
                        .uri(controllers.auth.routes.SignUpApiController.signUpAction().url())
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
    }

    @Test
    public void createUserWithExistsEmail() {

        SignUpControllerTest signUpControllerTest = new SignUpControllerTest();
        Map<String, String> data = signUpControllerTest.createDataFromRequest(existsEmail, notExistsPhone);

        Result result = route(
                app, fakeRequest().bodyForm(data).method(POST)
                        .uri(controllers.auth.routes.SignUpApiController.signUpAction().url())
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
    }

    @Test
    public void createUserWithExistsPhone() {

        SignUpControllerTest signUpControllerTest = new SignUpControllerTest();
        Map<String, String> data = signUpControllerTest.createDataFromRequest(notExistsEmail, existsPhone);

        Result result = route(
                app, fakeRequest().bodyForm(data).method(POST)
                        .uri(controllers.auth.routes.SignUpApiController.signUpAction().url())
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
    }
}
