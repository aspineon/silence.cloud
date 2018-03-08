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
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.PUT;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;

public class RemindPasswordApiControllerTest extends WithApplication {

    private Map<String, String> data = new HashMap<>();

    private Result result;

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
    public void remindPasswordByEmptyData(){

        result = route(app, fakeRequest().bodyForm(data).method(PUT)
                .uri(routes.RemindPasswordApiController.remindPasswordAction().url()));

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
    }

    @Test
    public void remindPasswordByExistsEmail(){

        data.put("userdata", existsEmail);

        result = route(app, fakeRequest().bodyForm(data).method(PUT)
                .uri(routes.RemindPasswordApiController.remindPasswordAction().url()));

        assertThat(result.status()).isEqualTo(OK);
    }

    @Test
    public void remindPasswordByNotExistsEmail(){

        data.put("userdata", notExistsEmail);

        result = route(app, fakeRequest().bodyForm(data).method(PUT)
                .uri(routes.RemindPasswordApiController.remindPasswordAction().url()));

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
    }

    @Test
    public void remindPasswordByExistsPhone(){

        data.put("userdata", existsPhone);

        result = route(app, fakeRequest().bodyForm(data).method(PUT)
                .uri(routes.RemindPasswordApiController.remindPasswordAction().url()));

        assertThat(result.status()).isEqualTo(OK);
    }

    @Test
    public void remindPasswordByNotExistsPhone(){

        data.put("userdata", notExistsPhone);

        result = route(app, fakeRequest().bodyForm(data).method(PUT)
                .uri(routes.RemindPasswordApiController.remindPasswordAction().url()));

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
    }
}
