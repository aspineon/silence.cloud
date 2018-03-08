package controllers;

import controllers.auth.SignInController;
import controllers.core.AccountSecurityController;
import play.filters.csrf.AddCSRFToken;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import repositories.core.user.FindUserByIdRepository;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@Security.Authenticated(AccountSecurityController.class)
public class DashboardController extends Controller {

    private final FindUserByIdRepository findUserByIdRepository;
    private final HttpExecutionContext httpExecutionContext;

    @Inject
    public DashboardController(FindUserByIdRepository findUserByIdRepository, HttpExecutionContext httpExecutionContext) {
        this.findUserByIdRepository = findUserByIdRepository;
        this.httpExecutionContext = httpExecutionContext;
    }

    @AddCSRFToken
    public CompletionStage<Result> getDashboard(Long id){

        return findUserByIdRepository.findUserById(id).thenApplyAsync(user -> {
            if(user.isPresent() && user.get() != null){

                AccountSecurityController.generateSecurityToken(user);
                return ok(views.html.dashboard.render(user.get()));
            }
            return redirect(controllers.auth.routes.SignInController.signInAction());
        }, httpExecutionContext.current());
    }
}
