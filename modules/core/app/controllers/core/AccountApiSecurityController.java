package controllers.core;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

public class AccountApiSecurityController extends Security.Authenticator {

    @Override
    public String getUsername(Http.Context ctx){

        AccountSecurityController accountSecurityController = new AccountSecurityController();
        return accountSecurityController.getUsername(ctx);
    }

    @Override
    public Result onUnauthorized(Http.Context ctx){
        ObjectNode wrapper = Json.newObject();
        wrapper.put("success", false);
        wrapper.put("message", "Unathorized. Please try again.");
        return unauthorized(wrapper);
    }
}
