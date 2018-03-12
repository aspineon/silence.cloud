package controllers.core;

import models.core.user.UserByEmailFindable;
import models.core.user.UserModel;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import java.util.Optional;
import java.util.UUID;

import static play.mvc.Controller.ctx;
import static play.mvc.Controller.response;
import static play.mvc.Controller.session;

public class AccountSecurityController extends Security.Authenticator implements UserByEmailFindable {

    public final String AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";

    public static final String AUTH_TOKEN = "authToken";

    @Override
    public String getUsername(Http.Context ctx) {
        String[] authTokenHeaderValues = ctx.request().headers().get(this.AUTH_TOKEN_HEADER);
        Optional<UserModel> user = Optional.empty();

        if ((authTokenHeaderValues != null) && (authTokenHeaderValues.length == 1) && (authTokenHeaderValues[0] != null)) {

            user = checkByToken(authTokenHeaderValues[0]);
        } else if(session().get("username") != null) {

            user = checkBySession();
        } else {

            user = Optional.empty();
        }

        if(user.isPresent() && user.get() != null){

            ctx.args.put("user", user.get());

            return user.get().email;
        } else {

            return null;
        }
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return redirect("/auth/sign-in");
    }

    public static void generateSecurityToken(Optional<UserModel> user){
        session().clear();
        response().setCookie(Http.Cookie.builder(AUTH_TOKEN, user.get().token)
                .withSecure(ctx().request().secure()).withHttpOnly(true).build());
        session().put("username", user.get().email);
    }

    private Optional<UserModel> checkByToken(String token){

        UserModel user = UserModel.FINDER.query().where().eq("token", token)
                .eq("isActive", true).findOne();
        if (user != null) {

            user.token = UUID.randomUUID().toString();
            user.update();
            return Optional.ofNullable(user);
        }
        return null;
    }

    private Optional<UserModel> checkBySession(){

        UserModel user = UserByEmailFindable.super.findUserByEmail(session().get("username"));
        if((user != null) && user.isActive) {

            return Optional.ofNullable(user);
        }
        return Optional.empty();
    }

    private Optional<UserModel> checkByCookie(){

        UserModel user = UserModel.FINDER.query().where()
                .eq("token", response().cookie("authToken").get().value()).findOne();
        if(user != null && user.isActive){

            return Optional.ofNullable(user);
        }

        return Optional.empty();
    }
}
