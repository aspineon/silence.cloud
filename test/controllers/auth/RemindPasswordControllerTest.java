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

import static play.api.test.CSRFTokenHelper.addCSRFToken;
import static org.assertj.core.api.Assertions.assertThat;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class RemindPasswordControllerTest extends WithApplication {

    private Map<String, String> data = new HashMap<>();

    private Result result;

    private String existsEmail = "john@doe.com";
    private String existsPhone = "000000000";
    private String existsPassword = "R3v3l@t104LoA";

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
        userModel.setPassword(existsPassword);
        userModel.isAdmin = true;
        userModel.save();
    }

    @After
    public void tearDown(){
        UserModel.FINDER.query().where().eq("email", existsEmail.toLowerCase()).findOne().delete();
    }

    @Test
    public void remindPasswordByEmptyData(){

        result = route(
                app, addCSRFToken(
                        fakeRequest().bodyForm(data).method(POST).uri(
                                routes.RemindPasswordController.remindPasswordAction().url()
                        )
                )
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(result.flash().containsKey("danger")).isTrue();
    }

    @Test
    public void remindPasswordByExistsEmail(){

        data.put("userdata", existsEmail);

        result = route(
                app, addCSRFToken(
                        fakeRequest().bodyForm(data).method(POST).uri(
                                routes.RemindPasswordController.remindPasswordAction().url()
                        )
                )
        );

        assertThat(result.status()).isEqualTo(OK);
        assertThat(result.flash().containsKey("success"));
    }

    @Test
    public void remindPasswordByNotExistsEmail(){

        data.put("userdata", notExistsEmail);

        result = route(
                app, addCSRFToken(
                        fakeRequest().bodyForm(data).method(POST).uri(
                                routes.RemindPasswordController.remindPasswordAction().url()
                        )
                )
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(result.flash().containsKey("warning")).isTrue();
    }

    @Test
    public void remindPasswordByExistsPhone(){

        data.put("userdata", existsPhone);

        result = route(
                app, addCSRFToken(
                        fakeRequest().bodyForm(data).method(POST).uri(
                                routes.RemindPasswordController.remindPasswordAction().url()
                        )
                )
        );

        assertThat(result.status()).isEqualTo(OK);
        assertThat(result.flash().containsKey("success"));
    }

    @Test
    public void remindPasswordByNotExistsPhone(){

        data.put("userdata", notExistsPhone);

        result = route(
                app, addCSRFToken(
                        fakeRequest().bodyForm(data).method(POST).uri(
                                routes.RemindPasswordController.remindPasswordAction().url()
                        )
                )
        );

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(result.flash().containsKey("warning")).isTrue();
    }
}
