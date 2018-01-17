package controllers.auth;

import models.core.user.UserModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Result;
import play.test.WithApplication;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static play.api.test.CSRFTokenHelper.addCSRFToken;
import static play.test.Helpers.*;

public class SignUpControllerTest extends WithApplication {

    private Result result;

    private String username = "John Doe";
    private String password = "R3v3l@t104LoA";

    private String badConfirmPassword = "R3v3l@t104";

    private String existsEmail = "john@doe.com";
    private String existsPhone = "000000000";

    private String notExistsEmail = "john1@doe.com";
    private String notExistsPhone = "000000001";

    @Before
    public void setUp(){
        UserModel userModel = new UserModel();
        userModel.id = System.currentTimeMillis();
        userModel.updateAt = new Date();
        userModel.createdAt = new Date();
        userModel.username = "john doe";
        userModel.setEmail(existsEmail);
        userModel.phone = existsPhone;
        userModel.setPassword(password);
        userModel.isAdmin = true;
        userModel.save();
    }

    @After
    public void tearDown(){
        UserModel.FINDER.query().where().eq("email", existsEmail.toLowerCase()).findOne().delete();
    }

    @Test
    public void createUserWithEmptyData(){

        Map<String, String> data = new HashMap<>();
        result = route(
                app, addCSRFToken(fakeRequest().bodyForm(data).method(POST)
                        .uri(controllers.auth.routes.SignUpController.signUpAction().url()))
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(result.flash().containsKey("danger")).isTrue();
    }

    @Test
    public void createUser(){

        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("email", notExistsEmail);
        data.put("phone", notExistsPhone);
        data.put("password", password);
        data.put("confirmPassword", password);

        Result result = route(
                app, addCSRFToken(fakeRequest().bodyForm(data).method(POST)
                        .uri(controllers.auth.routes.SignUpController.signUpAction().url()))
        );

        assertThat(result.status()).isEqualTo(OK);
        assertThat(result.flash().containsKey("success")).isTrue();
    }

    @Test
    public void createUserWhenPasswordsMismatch() {

        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("email", notExistsEmail);
        data.put("phone", notExistsPhone);
        data.put("password", password);
        data.put("confirmPassword", badConfirmPassword);

        result = route(
                app, addCSRFToken(fakeRequest().bodyForm(data).method(POST)
                        .uri(controllers.auth.routes.SignUpController.signUpAction().url()))
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(result.flash().containsKey("warning")).isTrue();
    }

    @Test
    public void createUserWithExistsEmail() {

        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("email", existsEmail);
        data.put("phone", notExistsPhone);
        data.put("password", password);
        data.put("confirmPassword", password);

        result = route(
                app, addCSRFToken(fakeRequest().bodyForm(data).method(POST)
                        .uri(controllers.auth.routes.SignUpController.signUpAction().url()))
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(result.flash().containsKey("warning")).isTrue();
    }

    @Test
    public void createUserWithExistsPhone() {

        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("email", notExistsEmail);
        data.put("phone", existsPhone);
        data.put("password", password);
        data.put("confirmPassword", password);

        result = route(
                app, addCSRFToken(fakeRequest().bodyForm(data).method(POST)
                        .uri(controllers.auth.routes.SignUpController.signUpAction().url()))
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(result.flash().containsKey("warning")).isTrue();
    }
}
