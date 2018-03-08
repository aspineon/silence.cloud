package controllers.userAdmin;

import controllers.core.AccountSecurityController;
import controllers.core.UserAdminSecurityController;
import models.core.user.UserModel;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import repositories.core.user.FindUserByEmailRepository;
import repositories.core.user.FindUserByIdRepository;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@Security.Authenticated(UserAdminSecurityController.class)
public class EditUserController extends Controller {

    private final FindUserByEmailRepository findUserByEmailRepository;
    private final FindUserByIdRepository findUserByIdRepository;
    private final HttpExecutionContext executionContext;

    @Inject
    public EditUserController(
            FindUserByEmailRepository findUserByEmailRepository, FindUserByIdRepository findUserByIdRepository,
            HttpExecutionContext executionContext
    ) {
        this.findUserByEmailRepository = findUserByEmailRepository;
        this.findUserByIdRepository = findUserByIdRepository;
        this.executionContext = executionContext;
    }

    public CompletionStage<Result> editUser(Long id){

        CompletionStage<Optional<UserModel>> currentUser = findUserByEmailRepository.findUserByEmail(
                session().get("username")
        );

        return findUserByIdRepository.findUserById(id).thenCombineAsync(currentUser, (futureUser, user) -> {

            if(
                    (user.isPresent() && (user.get() != null)) &&
                            (futureUser.isPresent() && (futureUser.get() != null)) &&
                            (futureUser.get().id != user.get().id)
                    ){
                AccountSecurityController.generateSecurityToken(user);
                return ok(views.html.userAdmin.editUser.render(user.get(), futureUser.get()));
            }

            AccountSecurityController.generateSecurityToken(user);
            return redirect(controllers.userAdmin.routes.AllUsersController.getAllUsers());
        }, executionContext.current());
    }
}
