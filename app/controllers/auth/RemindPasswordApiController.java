package controllers.auth;

import controllers.JsonResponseController;
import forms.auth.RemindPassword;
import helpers.core.user.NewPasswordGeneratable;
import models.core.user.UserByEmailFindable;
import models.core.user.UserByPhoneFindable;
import models.core.user.UserModel;
import play.data.Form;
import play.data.FormFactory;
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
 * Remind password controller.
 */
public class RemindPasswordApiController extends Controller implements UserByEmailFindable, UserByPhoneFindable,
        NewPasswordGeneratable {

    private final HttpExecutionContext httpExecutionContext;
    private final FormFactory formFactory;
    private final UpdateUserRepository updateUserRepository;

    @Inject
    public RemindPasswordApiController(
            HttpExecutionContext httpExecutionContext, FormFactory formFactory,
            UpdateUserRepository updateUserRepository) {
        this.httpExecutionContext = httpExecutionContext;
        this.formFactory = formFactory;
        this.updateUserRepository = updateUserRepository;
    }

    /**
     * @return Result of remind password
     */
    public CompletionStage<Result> remindPasswordAction(){

        Messages messages = Http.Context.current().messages();

        Form<RemindPassword> remindPasswordForm = formFactory.form(RemindPassword.class).bindFromRequest();
        if(remindPasswordForm.hasErrors()){

            return CompletableFuture.completedFuture(badRequest(remindPasswordForm.errorsAsJson()));
        }

        RemindPassword remindPassword = remindPasswordForm.get();
        if(UserByEmailFindable.super.validate(remindPassword.userdata)){
            return remindPasswordByEmail(remindPassword);
        }

        return remindPasswordByPhone(remindPassword);
    }

    /**
     * @param remindPassword
     * @return Result remind password by e-mail
     */
    private CompletionStage<Result> remindPasswordByEmail(RemindPassword remindPassword){

        Messages messages = Http.Context.current().messages();

        UserModel userModel = UserByEmailFindable.super.findUserByEmail(remindPassword.userdata);
        if(userModel == null){
            return CompletableFuture.completedFuture(
                    badRequest(
                            JsonResponseController.buildJsonResponse(
                                    "error", messages.at("remindPassword.badEmail")
                            )
                    )
            );
        }

        String password = generateRandomPassword(userModel);

        return updateUserRepository.updateUser(userModel).thenApplyAsync(user -> {
            if(user.isPresent() && (user.get() != null)){
                return ok(
                        JsonResponseController.buildJsonResponse(
                                "success", messages.at("remindPassword.success ")
                        )
                );
            }
            return badRequest(
                    JsonResponseController.buildJsonResponse(
                            "error", messages.at("remindPassword.formError")
                    )
            );
        }, httpExecutionContext.current());
    }

    /**
     * @param remindPassword
     * @return Return of remind password by phone
     */
    private CompletionStage<Result> remindPasswordByPhone(RemindPassword remindPassword){

        Messages messages = Http.Context.current().messages();

        UserModel userModel = UserByPhoneFindable.super.findUserByPhone(remindPassword.userdata);
        if(userModel == null){

            return CompletableFuture.completedFuture(
                    badRequest(
                            JsonResponseController.buildJsonResponse(
                                    "error", messages.at("remindPassword.badPhone")
                            )
                    )
            );
        }

        String password = generateRandomPassword(userModel);

        return updateUserRepository.updateUser(userModel).thenApplyAsync(user -> {
            if(user.isPresent() && (user.get() != null)){
                return ok(
                        JsonResponseController.buildJsonResponse(
                                "success", messages.at("remindPassword.success ")
                        )
                );
            }
            return badRequest(
                    JsonResponseController.buildJsonResponse(
                            "error", messages.at("remindPassword.formError")
                    )
            );
        }, httpExecutionContext.current());
    }

    /**
     * @param userModel
     */
    private String generateRandomPassword(UserModel userModel) {
        String password = NewPasswordGeneratable.super.generateRandomPassword();
        userModel.setPassword("password");
        userModel.updatedAt = new Date();

        return password;
    }
}
