package controllers.userAdmin;

import controllers.core.AccountSecurityController;
import controllers.core.UserAdminSecurityController;
import forms.core.Phone;
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
import java.util.concurrent.ExecutionException;

@Security.Authenticated(UserAdminSecurityController.class)
public class UpdatePhoneController extends Controller {

    private final FindUserByIdRepository findUserByIdRepository;
    private final FindUserByEmailRepository findUserByEmailRepository;
    private final UpdateUserRepository updateUserRepository;
    private final FormFactory formFactory;
    private final HttpExecutionContext executionContext;

    @Inject
    public UpdatePhoneController(
            FindUserByIdRepository findUserByIdRepository, FindUserByEmailRepository findUserByEmailRepository,
            UpdateUserRepository updateUserRepository, FormFactory formFactory, HttpExecutionContext executionContext
    ) {
        this.findUserByIdRepository = findUserByIdRepository;
        this.findUserByEmailRepository = findUserByEmailRepository;
        this.updateUserRepository = updateUserRepository;
        this.formFactory = formFactory;
        this.executionContext = executionContext;
    }

    @RequireCSRFCheck
    public CompletionStage<Result> updatePhone(Long id){

        AccountSecurityController.generateSecurityToken(
                Optional.ofNullable(
                        UserModel.FINDER.query().where().eq("email", session().get("username")).findOne()
                )
        );

        CompletionStage<Optional<UserModel>> userStage = findUserByEmailRepository
                .findUserByEmail(session().get("username"));
        return findUserByIdRepository.findUserById(id).thenCombineAsync(userStage, (futureUser, user) -> {
            if(futureUser.isPresent() && (futureUser.get() != null) && !user.get().id.equals(futureUser.get().id)){

                return tryToUpdatePhone(user.get(), futureUser.get());
            }

            return redirect(controllers.userAdmin.routes.AllUsersController.getAllUsers());
        }, executionContext.current());
    }

    @AddCSRFToken
    private Result tryToUpdatePhone(UserModel user, UserModel futureUser){

        Messages messages = Http.Context.current().messages();

        Form<Phone> phoneForm = formFactory.form(Phone.class).bindFromRequest();
        if(phoneForm.hasErrors()){
            return updatePhoneFormError(user, futureUser, messages);
        }

        Phone phone = phoneForm.get();
        if(futureUser.phone.equals(phone.phone)){

            return oldAndNewPhoneAreTheSame(user, futureUser, messages);
        }

        if(UserModel.FINDER.query().where().eq("phone", phone.phone).findOne() != null){

            return phoneExists(user, futureUser, messages);
        }

        futureUser.phone = phone.phone;
        futureUser.updatedAt = new Date();

        try {

            return updateUserPhone(user, futureUser, messages);
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            flash("phoneDanger", messages.at("userAdmin.updatePhone.updatePhoneErrors"));
            return badRequest(views.html.userAdmin.editUser.render(user, futureUser));
        }
    }

    private Result updateUserPhone(UserModel user, UserModel futureUser, Messages messages) throws InterruptedException,
            ExecutionException {
        Optional<UserModel> optionalUser = updateUserRepository.updateUser(futureUser).toCompletableFuture().get();
        if(optionalUser.isPresent() && (optionalUser.get() != null)){

            flash("phoneSuccess", messages.at("userAdmin.updatePhone.updatePhoneSuccess"));
            return ok(views.html.userAdmin.editUser.render(user, futureUser));
        }
        flash("phoneDanger", messages.at("userAdmin.updatePhone.updatePhoneErrors"));
        return badRequest(views.html.userAdmin.editUser.render(user, futureUser));
    }

    private Result phoneExists(UserModel user, UserModel futureUser, Messages messages) {
        flash("phoneDanger", messages.at("userAdmin.updatePhone.updatePhoneErrors"));
        flash("phoneWarning", messages.at("userAdmin.updatePhone.phoneExists"));
        return badRequest(views.html.userAdmin.editUser.render(user, futureUser));
    }

    private Result oldAndNewPhoneAreTheSame(UserModel user, UserModel futureUser, Messages messages) {
        flash("phoneDanger", messages.at("userAdmin.updatePhone.updatePhoneErrors"));
        flash("phoneWarning", messages.at("userAdmin.updatePhone.phonesAreTheSame"));
        return badRequest(views.html.userAdmin.editUser.render(user, futureUser));
    }

    private Result updatePhoneFormError(UserModel user, UserModel futureUser, Messages messages) {
        flash("phoneDanger", messages.at("userAdmin.updatePhone.formErrors"));
        return badRequest(views.html.userAdmin.editUser.render(user, futureUser));
    }
}
