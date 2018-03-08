package controllers.userAdmin;

import controllers.core.AccountSecurityController;
import controllers.core.UserAdminSecurityController;
import forms.core.Username;
import models.core.user.UserModel;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.Messages;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import repositories.core.user.FindUserByEmailRepository;
import repositories.core.user.FindUserByIdRepository;
import repositories.core.user.UpdateUserRepository;

import javax.inject.Inject;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

@Security.Authenticated(UserAdminSecurityController.class)
public class UpdateUsernameController extends Controller {

    private final FindUserByEmailRepository findUserByEmailRepository;
    private final FindUserByIdRepository findUserByIdRepository;
    private final UpdateUserRepository updateUserRepository;
    private final FormFactory formFactory;
    private final HttpExecutionContext executionContext;

    @Inject
    public UpdateUsernameController(
            FindUserByEmailRepository findUserByEmailRepository, FindUserByIdRepository findUserByIdRepository,
            UpdateUserRepository updateUserRepository, FormFactory formFactory, HttpExecutionContext executionContext
    ) {
        this.findUserByEmailRepository = findUserByEmailRepository;
        this.findUserByIdRepository = findUserByIdRepository;
        this.updateUserRepository = updateUserRepository;
        this.formFactory = formFactory;
        this.executionContext = executionContext;
    }

    @RequireCSRFCheck
    public CompletionStage<Result> updateUsername(Long id){

        CompletionStage<Optional<UserModel>> futureUser = findUserByIdRepository.findUserById(id);

        try {
            if (!futureUser.toCompletableFuture().get().isPresent()) {

                return CompletableFuture.completedFuture(
                        redirect(controllers.userAdmin.routes.AllUsersController.getAllUsers())
                );
            }
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return CompletableFuture.completedFuture(
                    redirect(controllers.userAdmin.routes.AllUsersController.getAllUsers())
            );
        }

        return updateCurrentUser(futureUser);
    }

    private CompletionStage<Result> updateCurrentUser(CompletionStage<Optional<UserModel>> futureUser) {
        return findUserByEmailRepository.findUserByEmail(session().get("username"))
                .thenCombineAsync(futureUser, (user, currentUser) -> {

                    if(
                            user.isPresent() && (user.get() != null) && currentUser.isPresent() &&
                                    (currentUser.get() != null) && !user.get().id.equals(currentUser.get().id)
                            ){

                        AccountSecurityController.generateSecurityToken(user);
                        return tryToUpdateUsername(user.get(), currentUser.get());
                    }

                    return redirect(controllers.userAdmin.routes.AllUsersController.getAllUsers());
                }, executionContext.current());
    }

    @AddCSRFToken
    private Result tryToUpdateUsername(UserModel sessionUser, UserModel userModel){

        Messages messages = Http.Context.current().messages();

        Form<Username> usernameForm = formFactory.form(Username.class).bindFromRequest();
        if(usernameForm.hasErrors()){

            return usernameFormError(sessionUser, userModel, messages);
        }

        Username username = usernameForm.get();
        return ifUsernameHasBenUpdated(sessionUser, userModel, messages, username);

    }

    private Result ifUsernameHasBenUpdated(UserModel sessionUser, UserModel userModel, Messages messages, Username username) {
        if(userModel.username.equals(username.username)){

            return oldAndNewUsernameAreTheSame(sessionUser, userModel, messages);
        }

        userModel.username = username.username;
        userModel.updatedAt = new Date();

        try {

            return userHasBeenUpdated(sessionUser, userModel, messages);
        }catch (Exception e){

            return usernameHasNotBeenUpdate(sessionUser, userModel, messages, e);
        }
    }

    private Result usernameFormError(UserModel sessionUser, UserModel userModel, Messages messages) {
        flash("usernameDanger",messages.at("userAdmin.updateUsername.formErrors"));
        return badRequest(views.html.userAdmin.editUser.render(sessionUser, userModel));
    }

    private Result usernameHasNotBeenUpdate(UserModel sessionUser, UserModel userModel, Messages messages, Exception e) {
        Logger.error(e.getMessage(), e);
        flash("usernameDanger", messages.at("userAdmin.updateUsername.usernameHasNotBeenUpdated"));
        return badRequest(views.html.userAdmin.editUser.render(sessionUser, userModel));
    }

    private Result oldAndNewUsernameAreTheSame(UserModel sessionUser, UserModel userModel, Messages messages) {
        flash("usernameDanger", messages.at("userAdmin.updateUsername.updateUsernameErrors"));
        flash("usernameWarning", messages.at("userAdmin.updateUsername.usernamesAreTheSame"));
        return badRequest(views.html.userAdmin.editUser.render(sessionUser, userModel));
    }

    private Result userHasBeenUpdated(UserModel sessionUser, UserModel userModel, Messages messages)
            throws InterruptedException, ExecutionException {
        Optional<UserModel> stage = updateUserRepository.updateUser(userModel).toCompletableFuture().get();
        if(stage.isPresent() && (stage.get() != null)){

            flash("usernameSuccess", messages.at("userAdmin.updateUsername.usernameHasBeenUpdates"));
            return ok(views.html.userAdmin.editUser.render(sessionUser, userModel));
        }

        flash("usernameDanger", messages.at("userAdmin.updateUsername.usernameHasNotBeenUpdated"));
        return badRequest(views.html.userAdmin.editUser.render(sessionUser, userModel));
    }
}
