package controllers.auth;

import forms.auth.RemindPassword;
import helpers.core.user.NewPasswordGeneratable;
import models.core.user.UserByEmailFindable;
import models.core.user.UserByPhoneFindable;
import models.core.user.UserModel;
import play.data.Form;
import play.data.FormFactory;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.Messages;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repositories.core.user.UpdateUserRepository;

import javax.inject.Inject;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Remind password controller actions.
 */
public class RemindPasswordController extends Controller implements UserByEmailFindable, UserByPhoneFindable,
        NewPasswordGeneratable {

    private final HttpExecutionContext executionContext;
    private final FormFactory formFactory;
    private final UpdateUserRepository updateUserRepository;

    @Inject
    public RemindPasswordController(
            HttpExecutionContext executionContext, FormFactory formFactory, UpdateUserRepository updateUserRepository
    ) {
        this.executionContext = executionContext;
        this.formFactory = formFactory;
        this.updateUserRepository = updateUserRepository;
    }

    /**
     * @return Result of get remind password form
     */
    @AddCSRFToken
    public CompletionStage<Result> getRemindPasswordForm(){

        Form<RemindPassword> remindPasswordForm = formFactory.form(RemindPassword.class);

        return CompletableFuture.completedFuture(
                ok(views.html.auth.remindPassword.render(remindPasswordForm))
        );
    }

    /**
     * @return Result of remind password
     */
    @RequireCSRFCheck
    public CompletionStage<Result> remindPasswordAction(){

        Messages messages = Http.Context.current().messages();

        Form<RemindPassword> remindPasswordForm = formFactory.form(RemindPassword.class).bindFromRequest();
        if(remindPasswordForm.hasErrors()){

            return CompletableFuture.completedFuture(
                    badRequestResult("danger", messages.at("remindPassword.formError"), remindPasswordForm)
            );
        }

        RemindPassword remindPassword = remindPasswordForm.get();
        if(UserByEmailFindable.super.validate(remindPassword.userdata)){

            return updateUserPasswordWhenUserdataIsEmail(remindPassword.userdata, remindPasswordForm);
        }
        return updateUserPasswordWhenUserdataIsPhone(remindPassword.userdata, remindPasswordForm);
    }

    /**
     * @param userdata
     * @param remindPasswordForm
     * @return Result of remind password with user email
     */
    private CompletionStage<Result> updateUserPasswordWhenUserdataIsEmail(
            String userdata, Form<RemindPassword> remindPasswordForm
    ){

        Messages messages = Http.Context.current().messages();

        UserModel userModel = UserByEmailFindable.super.findUserByEmail(userdata);
        if(userModel == null){

            return CompletableFuture.completedFuture(
                    badRequestResult("warning", messages.at("remindPassword.badEmail"), remindPasswordForm)
            );
        }

        String password = generateRandomPassword(userModel);

        return updateUserRepository.updateUser(userModel).thenApplyAsync(user -> {
            if(user.isPresent() && (user.get() != null)){
                flash("success", messages.at("remindPassword.success"));
                return ok(views.html.auth.remindPassword.render(remindPasswordForm));
            }
            return badRequestResult("danger", messages.at("remindPassword.formError"), remindPasswordForm);
        }, executionContext.current());
    }

    /**
     * @param userdata
     * @param remindPasswordForm
     * @return Result of remind password with user phone
     */
    private CompletionStage<Result> updateUserPasswordWhenUserdataIsPhone(
            String userdata, Form<RemindPassword> remindPasswordForm
    ){

        Messages messages = Http.Context.current().messages();

        UserModel userModel = UserByPhoneFindable.super.findUserByPhone(userdata);
        if(userModel == null){

            return CompletableFuture.completedFuture(
                    badRequestResult("warning", messages.at("remindPassword.badPhone"), remindPasswordForm)
            );
        }

        String password = generateRandomPassword(userModel);

        return updateUserRepository.updateUser(userModel).thenApplyAsync(user -> {
            if(user.isPresent() && (user.get() != null)){
                flash("success", messages.at("remindPassword.success"));
                return ok(views.html.auth.remindPassword.render(remindPasswordForm));
            }
            return badRequestResult("danger", messages.at("remindPassword.formError"), remindPasswordForm);
        }, executionContext.current());
    }

    /**
     * @param userModel
     * Return string of random password
     */
    private String generateRandomPassword(UserModel userModel) {
        String password = NewPasswordGeneratable.super.generateRandomPassword();
        userModel.setPassword("password");
        userModel.updateAt = new Date();

        return password;
    }

    /**
     * @param type
     * @param message
     * @param remindPasswordForm
     * @return Result of bad request
     */
    @AddCSRFToken
    private Result badRequestResult(
            String type, String message, Form<RemindPassword> remindPasswordForm
    ){

        flash(type, message);
        return badRequest(views.html.auth.remindPassword.render(remindPasswordForm));
    }
}
