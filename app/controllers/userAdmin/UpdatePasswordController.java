package controllers.userAdmin;

import controllers.core.AccountSecurityController;
import controllers.core.UserAdminSecurityController;
import forms.core.UpdatePassword;
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
import repositories.core.user.FindUserByIdRepository;
import repositories.core.user.UpdateUserRepository;

import javax.inject.Inject;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

@Security.Authenticated(UserAdminSecurityController.class)
public class UpdatePasswordController extends Controller {

    private final FindUserByIdRepository findUserByIdRepository;
    private final FindUserByEmailRepository findUserByEmailRepository;
    private final FindUserByEmailAndPasswordRepository findUserByEmailAndPasswordRepository;
    private final UpdateUserRepository updateUserRepository;

    private final FormFactory formFactory;

    private final HttpExecutionContext executionContext;

    @Inject
    public UpdatePasswordController(
            FindUserByIdRepository findUserByIdRepository, FindUserByEmailRepository findUserByEmailRepository,
            FindUserByEmailAndPasswordRepository findUserByEmailAndPasswordRepository,
            UpdateUserRepository updateUserRepository, FormFactory formFactory, HttpExecutionContext executionContext
    ) {

        this.findUserByIdRepository = findUserByIdRepository;
        this.findUserByEmailRepository = findUserByEmailRepository;
        this.findUserByEmailAndPasswordRepository = findUserByEmailAndPasswordRepository;
        this.updateUserRepository = updateUserRepository;
        this.formFactory = formFactory;
        this.executionContext = executionContext;
    }


    @RequireCSRFCheck
    public CompletionStage<Result> updatePassword(Long id){

        AccountSecurityController.generateSecurityToken(
                Optional.ofNullable(
                        UserModel.FINDER.query().where().eq("email", session().get("username")).findOne()
                )
        );

        CompletionStage<Optional<UserModel>> userStage = findUserByEmailRepository.findUserByEmail(
                session().get("username")
        );

        return findUserByIdRepository.findUserById(id).thenCombineAsync(userStage, (futureUser, user) -> {
            if(futureUser.isPresent() && (futureUser.get() != null) && (!futureUser.get().id.equals(user.get().id))) {

                return tryToUpdatePassword(user.get(), futureUser.get());
            }

            return redirect(controllers.userAdmin.routes.AllUsersController.getAllUsers());
        }, executionContext.current());
    }

    @AddCSRFToken
    private Result tryToUpdatePassword(UserModel user, UserModel futureUser){

        Messages messages = Http.Context.current().messages();

        Form<UpdatePassword> updatePasswordForm = formFactory.form(UpdatePassword.class).bindFromRequest();
        if(updatePasswordForm.hasErrors()){

            return passwordFormErrors(user, futureUser, messages);
        }

        UpdatePassword updatePassword = updatePasswordForm.get();

        try {

            Optional<UserModel> oldUser = findUserByEmailAndPasswordRepository
                    .findUserByEmailAndPassword(futureUser.email, updatePassword.password).toCompletableFuture().get();
            if(oldUser.isPresent()){

                return oldAndNewPasswordAreTheSame(user, futureUser, messages);
            }

            if(!updatePassword.password.equals(updatePassword.confirmPassword)){

                return passwordsMismatch(user, futureUser, messages);
            }

            UserModel updatedUser = updateUserPassword(futureUser, updatePassword);

            return checkPasswordIsUpdated(user, updatedUser, messages);
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return badRequest(views.html.userAdmin.editUser.render(user, futureUser));
        }
    }

    private Result checkPasswordIsUpdated(UserModel user, UserModel futureUser, Messages messages)
            throws InterruptedException, ExecutionException {

        Optional<UserModel> updatedUser = updateUserRepository.updateUser(futureUser).toCompletableFuture().get();
        if(updatedUser.isPresent() && (updatedUser.get() != null)){

            flash("passwordSuccess", messages.at("userAdmin.updatePassword.passwordHasBeenUpdated"));
            return ok(views.html.userAdmin.editUser.render(user, futureUser));
        }

        flash("passwordDanger", messages.at("userAdmin.updatePassword.passwordHasNotBeenUpdated"));
        return badRequest(views.html.userAdmin.editUser.render(user, futureUser));
    }

    private UserModel updateUserPassword(UserModel futureUser, UpdatePassword updatePassword) {

        futureUser.setPassword(updatePassword.password);
        futureUser.updatedAt = new Date();
        return futureUser;
    }

    private Result passwordsMismatch(UserModel user, UserModel futureUser, Messages messages) {

        flash("passwordDanger", messages.at("userAdmin.updatePassword.updatePasswordErrors"));
        flash("passwordWarning", messages.at("userAdmin.updatePassword.passwordsMismatch"));
        return badRequest(views.html.userAdmin.editUser.render(user, futureUser));
    }

    private Result passwordFormErrors(UserModel user, UserModel futureUser, Messages messages) {
        flash("passwordDanger", messages.at("userAdmin.updatePassword.formErrors"));
        return badRequest(views.html.userAdmin.editUser.render(user, futureUser));
    }

    private Result oldAndNewPasswordAreTheSame(UserModel user, UserModel futureUser, Messages messages) {
        flash("passwordDanger", messages.at("userAdmin.updatePassword.updatePasswordErrors"));
        flash("passwordWarning", messages.at("userAdmin.updatePassword.oldAndNewPasswordAreTheSame"));
        return badRequest(views.html.userAdmin.editUser.render(user, futureUser));
    }

}
