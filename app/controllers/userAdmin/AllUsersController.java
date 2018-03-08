package controllers.userAdmin;

import controllers.core.AccountSecurityController;
import controllers.core.UserAdminSecurityController;
import models.core.user.UserModel;
import play.filters.csrf.AddCSRFToken;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import repositories.core.user.FindAllUsersRepository;
import repositories.core.user.FindUserByEmailRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Security.Authenticated(UserAdminSecurityController.class)
public class AllUsersController extends Controller {

    private final FindAllUsersRepository findAllUsersRepository;
    private final FindUserByEmailRepository findUserByEmailRepository;
    private final HttpExecutionContext executionContext;

    @Inject
    public AllUsersController(
            FindAllUsersRepository findAllUsersRepository, FindUserByEmailRepository findUserByEmailRepository,
            HttpExecutionContext executionContext
    ) {
        this.findAllUsersRepository = findAllUsersRepository;
        this.findUserByEmailRepository = findUserByEmailRepository;
        this.executionContext = executionContext;
    }

    @AddCSRFToken
    public CompletionStage<Result> getAllUsers(){

        CompletionStage<List<UserModel>> allUsersStage = findAllUsersRepository.findAllUsers();

        return findUserByEmailRepository.findUserByEmail(session().get("username"))
                .thenCombineAsync(allUsersStage,(user, allUsers) -> {

                    if(user.isPresent() && user.get() != null){

                        AccountSecurityController.generateSecurityToken(user);
                        return ok(views.html.userAdmin.listOfUsers.render(user.get(), allUsers));
                    }
                    return redirect(controllers.auth.routes.SignInController.signInForm());
        }, executionContext.current());
    }
}
