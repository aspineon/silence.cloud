package controllers.userAdmin;

import controllers.auth.routes;
import controllers.core.UserAdminSecurityController;
import play.filters.csrf.AddCSRFToken;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import repositories.core.user.FindUserByEmailRepository;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@Security.Authenticated(UserAdminSecurityController.class)
public class AddUserController extends Controller {

    private final FindUserByEmailRepository findUserByEmailRepository;
    private final HttpExecutionContext executionContext;

    @Inject
    public AddUserController(
            FindUserByEmailRepository findUserByEmailRepository, HttpExecutionContext executionContext
    ) {
        this.findUserByEmailRepository = findUserByEmailRepository;
        this.executionContext = executionContext;
    }

    @AddCSRFToken
    public CompletionStage<Result> addUserForm(){

        return findUserByEmailRepository.findUserByEmail(session().get("username")).thenApplyAsync(user -> {
            if(user.isPresent() && (user.get() != null)){

                return ok(views.html.userAdmin.addUser.render(user.get()));
            }
            return redirect(controllers.auth.routes.SignInController.signInForm());
        }, executionContext.current());
    }
}
