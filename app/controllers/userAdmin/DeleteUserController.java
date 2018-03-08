package controllers.userAdmin;

import controllers.core.AccountSecurityController;
import controllers.core.UserAdminSecurityController;
import controllers.userAdmin.routes;
import models.core.user.UserModel;
import play.Logger;
import play.i18n.Messages;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import repositories.core.user.DeleteUserRepository;
import repositories.core.user.FindUserByEmailRepository;
import repositories.core.user.FindUserByIdRepository;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@Security.Authenticated(UserAdminSecurityController.class)
public class DeleteUserController extends Controller {

    private final FindUserByEmailRepository findUserByEmailRepository;
    private final FindUserByIdRepository findUserByIdRepository;
    private final DeleteUserRepository deleteUserRepository;

    private final HttpExecutionContext executionContext;

    @Inject
    public DeleteUserController(
            FindUserByEmailRepository findUserByEmailRepository, FindUserByIdRepository findUserByIdRepository,
            DeleteUserRepository deleteUserRepository, HttpExecutionContext executionContext
    ) {

        this.findUserByEmailRepository = findUserByEmailRepository;
        this.findUserByIdRepository = findUserByIdRepository;
        this.deleteUserRepository = deleteUserRepository;
        this.executionContext = executionContext;
    }

    public CompletionStage<Result> deleteUser(Long id){

        AccountSecurityController.generateSecurityToken(
                Optional.ofNullable(
                        UserModel.FINDER.query().where().eq("email", session().get("username")).findOne()
                )
        );

        Messages messages = Http.Context.current().messages();

        CompletionStage<Optional<UserModel>> stage = findUserByEmailRepository.findUserByEmail(session().get("username"));
        return findUserByIdRepository.findUserById(id).thenCombineAsync(stage, (futureUser, user) -> {

            if(futureUser.isPresent() && futureUser.get() != null){

                return tryToUpdateUser(user.get(), futureUser.get(), messages);
            }

            flash("danger", messages.at("userAdmin.deleteUser.userNotFound"));
            return redirect(controllers.userAdmin.routes.AllUsersController.getAllUsers());
        }, executionContext.current());
    }

    private Result tryToUpdateUser(UserModel user, UserModel futureUser, Messages messages){

        if(user.id.equals(futureUser.id)){

            flash("warning", messages.at("userAdmin.deleteUser.userHasNotBeenDeleted"));
            return redirect(controllers.userAdmin.routes.AllUsersController.getAllUsers());
        }

        try {

            Optional<UserModel> deletedUser = deleteUserRepository.deleteUser(futureUser.id).toCompletableFuture().get();
            if(!deletedUser.isPresent()){

                flash("success", messages.at("userAdmin.deleteUser.userHasBeenDeleted"));
                return redirect(controllers.userAdmin.routes.AllUsersController.getAllUsers());
            }
            flash("warning", messages.at("userAdmin.deleteUser.userHasNotBeenDeleted"));
            return redirect(controllers.userAdmin.routes.AllUsersController.getAllUsers());
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            flash("warning", messages.at("userAdmin.deleteUser.userHasNotBeenDeleted"));
            return redirect(controllers.userAdmin.routes.AllUsersController.getAllUsers());
        }
    }

}
