package controllers.account;

import controllers.core.AccountSecurityController;
import play.filters.csrf.AddCSRFToken;
import play.i18n.Messages;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import repositories.core.user.FindUserByEmailRepository;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@Security.Authenticated(AccountSecurityController.class)
public class UserController extends Controller {

    private final FindUserByEmailRepository findUserByEmailRepository;
    private final HttpExecutionContext httpExecutionContext;

    @Inject
    public UserController(FindUserByEmailRepository findUserByEmailRepository, HttpExecutionContext httpExecutionContext) {
        this.findUserByEmailRepository = findUserByEmailRepository;
        this.httpExecutionContext = httpExecutionContext;
    }

    @AddCSRFToken
    public CompletionStage<Result> getUser(){

        Messages messages = Http.Context.current().messages();
        return findUserByEmailRepository.findUserByEmail(session().get("username")).thenApplyAsync(user -> {

            if(user.isPresent() && user.get() != null){

                AccountSecurityController.generateSecurityToken(user);
                return ok(views.html.account.user.render(user.get()));
            }

            return redirect(controllers.auth.routes.SignInController.signInForm());
        }, httpExecutionContext.current());
    }
}
