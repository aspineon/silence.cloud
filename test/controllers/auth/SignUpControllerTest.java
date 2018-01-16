package controllers.auth;

import org.junit.Test;
import play.mvc.Result;
import play.test.WithApplication;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static play.api.test.CSRFTokenHelper.addCSRFToken;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.*;

public class SignUpControllerTest extends WithApplication {

    private String username = "John Doe";
    private String password = "R3v3l@t104LoA";

    private String badConfirmPassword = "R3v3l@t104";

    private String existsEmail = "john@doe.com";
    private String existsPhone = "000000000";

    private String notExistsEmail = "john1@doe.com";
    private String notExistsPhone = "000000001";


    @Test
    public void createUserWithEmptyData(){

        Map<String, String> data = new HashMap<>();
        Result result = route(
                app, addCSRFToken(fakeRequest().bodyForm(data).method(POST)
                        .uri(controllers.auth.routes.SignUpController.signUpAction().url()))
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
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

        assertThat(result.status()).isEqualTo(SEE_OTHER);
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
                app, addCSRFToken(fakeRequest().bodyForm(data).method(POST)
                        .uri(controllers.auth.routes.SignUpController.signUpAction().url()))
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void createUserWithExistsEmail() {

        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("email", existsEmail);
        data.put("phone", notExistsPhone);
        data.put("password", password);
        data.put("confirmPassword", password);

        Result result;

        result = route(
                app, addCSRFToken(fakeRequest().bodyForm(data).method(POST)
                        .uri(controllers.auth.routes.SignUpController.signUpAction().url()))
        );

        assertThat(result.flash().containsKey("success")).isTrue();

        result = route(
                app, addCSRFToken(fakeRequest().bodyForm(data).method(POST)
                        .uri(controllers.auth.routes.SignUpController.signUpAction().url()))
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void createUserWithExistsPhone() {

        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("email", notExistsEmail);
        data.put("phone", existsPhone);
        data.put("password", password);
        data.put("confirmPassword", password);

        Result result;

        result = route(
                app, addCSRFToken(fakeRequest().bodyForm(data).method(POST)
                        .uri(controllers.auth.routes.SignUpController.signUpAction().url()))
        );

        assertThat(result.flash().containsKey("success")).isTrue();

        result = route(
                app, addCSRFToken(fakeRequest().bodyForm(data).method(POST)
                        .uri(controllers.auth.routes.SignUpController.signUpAction().url()))
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }
}
