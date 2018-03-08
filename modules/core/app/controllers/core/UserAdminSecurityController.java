package controllers.core;

import models.core.user.UserModel;
import play.Logger;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import java.util.Optional;

public class UserAdminSecurityController extends Security.Authenticator {

    @Override
    public String getUsername(Http.Context ctx) {
        String username = new AccountSecurityController().getUsername(ctx);
        try {

            UserModel userModel = UserModel.FINDER.query().where().eq("email", username)
                    .eq("isAdmin", true).findOne();
            ctx.args.put("user", userModel);
            return userModel.email;
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return redirect("/user/account");
    }

}
