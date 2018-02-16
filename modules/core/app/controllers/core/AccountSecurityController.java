package controllers.core;

import models.core.user.UserModel;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

public class AccountSecurityController extends Security.Authenticator {

    public final String AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";

    @Override
    public String getUsername(Http.Context ctx) {
        String[] authTokenHeaderValues = ctx.request().headers().get(this.AUTH_TOKEN_HEADER);
        if ((authTokenHeaderValues != null) && (authTokenHeaderValues.length == 1) && (authTokenHeaderValues[0] != null)) {
            UserModel user = UserModel.FINDER.query().where().eq("token", authTokenHeaderValues[0]).findOne();
            if (user != null) {
                ctx.args.put("user", user);
                return user.username;
            }
        }

        return null;
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return unauthorized();
    }
}
