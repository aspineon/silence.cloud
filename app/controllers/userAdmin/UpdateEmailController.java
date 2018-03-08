package controllers.userAdmin;

import controllers.core.AccountSecurityController;
import controllers.core.UserAdminSecurityController;
import forms.core.Email;
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
import java.util.concurrent.CompletionStage;

@Security.Authenticated(UserAdminSecurityController.class)
public class UpdateEmailController extends Controller {

    private final FindUserByEmailRepository findUserByEmailRepository;
    private final FindUserByIdRepository findUserByIdRepository;
    private final UpdateUserRepository updateUserRepository;
    private final HttpExecutionContext executionContext;
    private final FormFactory formFactory;

    @Inject
    public UpdateEmailController(
            FindUserByEmailRepository findUserByEmailRepository, FindUserByIdRepository findUserByIdRepository,
            UpdateUserRepository updateUserRepository, HttpExecutionContext executionContext, FormFactory formFactory
    ) {
        this.findUserByEmailRepository = findUserByEmailRepository;
        this.findUserByIdRepository = findUserByIdRepository;
        this.updateUserRepository = updateUserRepository;
        this.executionContext = executionContext;
        this.formFactory = formFactory;
    }

    @RequireCSRFCheck
    public CompletionStage<Result> updateEmail(Long id){

        AccountSecurityController.generateSecurityToken(
                Optional.ofNullable(
                        UserModel.FINDER.query().where().eq("email", session().get("username")).findOne()
                )
        );

        CompletionStage<Optional<UserModel>> sessionUser = findUserByEmailRepository.findUserByEmail(session().get("username"));
        return findUserByIdRepository.findUserById(id).thenCombineAsync(sessionUser, (futureUser, user) -> {

            if(futureUser.isPresent() && (futureUser.get() != null) && !futureUser.get().id.equals(user.get().id)){

                return tryToUpdateEmail(user.get(), futureUser.get());
            }

            return redirect(controllers.userAdmin.routes.AllUsersController.getAllUsers());
        }, executionContext.current());
    }

    @AddCSRFToken
    public Result tryToUpdateEmail(UserModel currentUser, UserModel userToUpdate){

        Messages messages = Http.Context.current().messages();
        Form<Email> emailForm = formFactory.form(Email.class).bindFromRequest();
        if(emailForm.hasErrors()){

            return getFormDanger(currentUser, userToUpdate, messages);
        }

        Email email = emailForm.get();
        if(userToUpdate.email.equals(email.email)){

            return emailsAreTheSame(currentUser, userToUpdate, messages);
        }

        if(UserModel.FINDER.query().where().eq("email", email.email).findOne() != null){

            return emailExists(currentUser, userToUpdate, messages);
        }

        userToUpdate.email = email.email;
        userToUpdate.updatedAt = new Date();

        try {

            return updateCurrentUser(currentUser, userToUpdate, messages);
        } catch (Exception e){

            return getUpdateUserException(currentUser, userToUpdate, messages, e);
        }
    }

    private Result getUpdateUserException(UserModel currentUser, UserModel userToUpdate, Messages messages, Exception e) {
        Logger.error(e.getMessage(), e);
        flash("emailDanger", messages.at("userAdmin.updateEmail.updateEmailErrors"));
        return badRequest(views.html.userAdmin.editUser.render(currentUser, userToUpdate));
    }

    private Result emailExists(UserModel currentUser, UserModel userToUpdate, Messages messages) {
        flash("emailDanger", messages.at("userAdmin.updateEmail.updateEmailErrors"));
        flash("emailWarning", messages.at("userAdmin.updateEmail.emailExists"));
        return badRequest(views.html.userAdmin.editUser.render(currentUser, userToUpdate));
    }

    private Result emailsAreTheSame(UserModel currentUser, UserModel userToUpdate, Messages messages) {
        flash("emailDanger", messages.at("userAdmin.updateEmail.updateEmailErrors"));
        flash("emailWarning", messages.at("userAdmin.updateEmail.emailsAreTheSame"));
        return badRequest(views.html.userAdmin.editUser.render(currentUser, userToUpdate));
    }

    private Result updateCurrentUser(UserModel currentUser, UserModel userToUpdate, Messages messages) throws InterruptedException, java.util.concurrent.ExecutionException {
        Optional<UserModel> stage = updateUserRepository.updateUser(userToUpdate).toCompletableFuture().get();
        if(stage.isPresent()){

            flash("emailSuccess", messages.at("userAdmin.updateEmail.updateEmailSuccess"));
            return ok(views.html.userAdmin.editUser.render(currentUser, userToUpdate));
        }

        flash("emailDanger", messages.at("userAdmin.updateEmail.updateEmailErrors"));
        return badRequest(views.html.userAdmin.editUser.render(currentUser, userToUpdate));
    }

    private Result getFormDanger(UserModel currentUser, UserModel userToUpdate, Messages messages) {
        flash(
                "emailDanger",
                messages.at("userAdmin.updateEmail.formErrors")
        );
        return badRequest(views.html.userAdmin.editUser.render(currentUser, userToUpdate));
    }
}
