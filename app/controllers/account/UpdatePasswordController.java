package controllers.account;

import controllers.account.routes;
import controllers.core.AccountSecurityController;
import forms.core.Password;
import models.core.user.UserByEmailFindable;
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
import repositories.core.user.FindUserByEmailAndPasswordRepository;
import repositories.core.user.FindUserByEmailRepository;
import repositories.core.user.UpdateUserRepository;

import javax.inject.Inject;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Security.Authenticated(AccountSecurityController.class)
public class UpdatePasswordController extends Controller implements UserByEmailFindable {

    private final FindUserByEmailRepository findUserByEmailRepository;
    private final FindUserByEmailAndPasswordRepository findUserByEmailAndPasswordRepository;
    private final UpdateUserRepository updateUserRepository;
    private final HttpExecutionContext executionContext;
    private final FormFactory formFactory;

    @Inject
    public UpdatePasswordController(
            FindUserByEmailRepository findUserByEmailRepository,
            FindUserByEmailAndPasswordRepository findUserByEmailAndPasswordRepository,
            UpdateUserRepository updateUserRepository, HttpExecutionContext executionContext, FormFactory formFactory
    ) {
        this.findUserByEmailRepository = findUserByEmailRepository;
        this.findUserByEmailAndPasswordRepository = findUserByEmailAndPasswordRepository;
        this.updateUserRepository = updateUserRepository;
        this.executionContext = executionContext;
        this.formFactory = formFactory;
    }

    @RequireCSRFCheck
    public CompletionStage<Result> updatePassword(){

        Messages messages = Http.Context.current().messages();

        Form<Password> passwordForm = formFactory.form(Password.class).bindFromRequest();
        if(passwordForm.hasErrors()){
            flash("danger", messages.at("account.password.passwordHasNotBeenUpdated"));
            AccountSecurityController.generateSecurityToken(
                    Optional.ofNullable(UserByEmailFindable.super.findUserByEmail(session().get("username")))
            );
            return CompletableFuture.completedFuture(redirect(controllers.account.routes.UserController.getUser()));
        }

        Password password = passwordForm.get();

        if(!password.newPassword.equals(password.confirmPassword)){

            return passwordsMismatch();
        }

        CompletionStage<Optional<UserModel>> stage = findUserByEmailAndPasswordRepository.findUserByEmailAndPassword(
                session().get("username"), password.oldPassword
        );
        try {
            return updateUserPassword(password, stage);
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return CompletableFuture.completedFuture(redirect(controllers.account.routes.UserController.getUser()));
        }
    }

    @AddCSRFToken
    private CompletionStage<Result> updateUserPassword(Password password, CompletionStage<Optional<UserModel>> stage)
            throws InterruptedException, java.util.concurrent.ExecutionException {

        Messages messages = Http.Context.current().messages();

        Optional<UserModel> user = stage.toCompletableFuture().get();
        if(user.isPresent() && user.get() != null){

            return tryToUpdatePassword(password, messages, user);
        } else {

            return findUserByEmailRepository.findUserByEmail(session().get("username")).thenApplyAsync(currentUser -> {
                if(currentUser.isPresent() && (currentUser.get() != null)){

                    flash("danger", messages.at("account.password.passwordHasNotBeenUpdated"));
                    flash("oldPasswordWarning", messages.at("account.password.badOldPassword"));
                    return badRequest(views.html.account.user.render(currentUser.get()));
                }

                return redirect(controllers.account.routes.UserController.getUser());
            }, executionContext.current());
        }
    }

    private CompletionStage<Result> tryToUpdatePassword(Password password, Messages messages, Optional<UserModel> user) {

        UserModel userModel = user.get();
        userModel.setPassword(password.newPassword);
        userModel.updatedAt = new Date();

        return updateUserRepository.updateUser(userModel).thenApplyAsync(currentUser -> {

            if(currentUser.isPresent() && (currentUser.get() != null)){

                flash("success", messages.at("account.password.passwordHasBeenUpdated"));
                return ok(views.html.account.user.render(currentUser.get()));
            }

            flash("danger", messages.at("account.password.passwordHasNotBeenUpdated"));
            return badRequest(views.html.account.user.render(
                    UserByEmailFindable.super.findUserByEmail(session().get("username")))
            );
        }, executionContext.current());
    }

    @AddCSRFToken
    private CompletionStage<Result> passwordsMismatch(){

        Messages messages = Http.Context.current().messages();
        flash("danger", messages.at("account.password.passwordHasNotBeenUpdated"));
        flash("passwordsMismatchWarning", messages.at("account.password.passwordsMismatch"));
        return findUserByEmailRepository.findUserByEmail(session().get("username")).thenApplyAsync(user -> {
            if(user.isPresent() && (user.get() != null)){

                return badRequest(views.html.account.user.render(user.get()));
            }

            return redirect(controllers.account.routes.UserController.getUser());
        }, executionContext.current());
    }
}
