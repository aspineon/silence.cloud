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
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.POST;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;

public class SignInControllerTest extends WithApplication {

    private Map<String, String> data = new HashMap<>();

    private Result result;

    private String existsEmail = "john@doe.com";
    private String existsPhone = "000000000";
    private String existsPassword = "R3v3l@t104LoA";

    private String notExistsEmail = "john1@doe.com";
    private String notExistsPhone = "000000001";
    private String notExistsPassword = "R3vel@t104LoA";

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
    public void signInWithEmptyData() {

        result = route(
                app, addCSRFToken(
                        fakeRequest().bodyForm(data).method(POST).uri(
                                controllers.auth.routes.SignInController.signInAction().url()
                        )
                )
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(result.flash().containsKey("danger")).isTrue();
    }

    @Test
    public void signInWithExistsEmailAndExistsPassword() {

        data.put("userdata", existsEmail);
        data.put("password", existsPassword);

        result = route(
                app, addCSRFToken(
                        fakeRequest().bodyForm(data).method(POST).uri(
                                controllers.auth.routes.SignInController.signInAction().url()
                        )
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void signInWithNotExistsEmailAndExistsPassword() {

        data.put("userdata", notExistsEmail);
        data.put("password", existsPassword);

        result = route(
                app, addCSRFToken(
                        fakeRequest().bodyForm(data).method(POST).uri(
                                controllers.auth.routes.SignInController.signInAction().url()
                        )
                )
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(result.flash().containsKey("danger")).isTrue();
    }

    @Test
    public void signInWithExistsEmailAndNotExistsPassword() {

        data.put("userdata", existsEmail);
        data.put("password", notExistsPassword);

        badAction();
    }

    @Test
    public void signInWithExistsPhoneAndExistsPassword() {
        data.put("userdata", existsPhone);
        data.put("password", existsPassword);

        result = route(
                app, addCSRFToken(
                        fakeRequest().bodyForm(data).method(POST).uri(
                                controllers.auth.routes.SignInController.signInAction().url()
                        )
                )
        );

        assertThat(result.status()).isEqualTo(SEE_OTHER);
    }

    @Test
    public void signInWithNotExistsPhoneAndExistsPassword() {

        data.put("userdata", notExistsPhone);
        data.put("password", existsPassword);

        badAction();
    }

    @Test
    public void signInWithExistsPhoneAndNotExistsPassword() {

        data.put("userdata", existsPhone);
        data.put("password", notExistsPassword);

        badAction();
    }

    private void badAction(){

        result = route(
                app, addCSRFToken(
                        fakeRequest().bodyForm(data).method(POST).uri(
                                controllers.auth.routes.SignInController.signInAction().url()
                        )
                )
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(result.flash().containsKey("danger")).isTrue();
    }
}
