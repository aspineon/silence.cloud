package controllers.account;

import controllers.JsonResponseController;
import controllers.core.AccountApiSecurityController;
import controllers.core.AccountSecurityController;
import play.i18n.Messages;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import repositories.core.user.FindUserByEmailRepository;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@Security.Authenticated(AccountApiSecurityController.class)
public class UserApiController extends Controller {

    private final FindUserByEmailRepository findUserByEmailRepository;
    private final HttpExecutionContext httpExecutionContext;

    @Inject
    public UserApiController(FindUserByEmailRepository findUserByEmailRepository, HttpExecutionContext httpExecutionContext) {
        this.findUserByEmailRepository = findUserByEmailRepository;
        this.httpExecutionContext = httpExecutionContext;
    }

    public CompletionStage<Result> getUser(){

        Messages messages = Http.Context.current().messages();

        return findUserByEmailRepository.findUserByEmail(session().get("username")).thenApplyAsync(user -> {
            if(user.isPresent() && user.get() != null){

                AccountSecurityController.generateSecurityToken(user);
                return ok(Json.toJson(user.get()));
            }

            return unauthorized(
                    JsonResponseController.buildJsonResponse(
                            "error", messages.at("user.notFound")
                    )
            );
        }, httpExecutionContext.current());
    }
}
