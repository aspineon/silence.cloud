package controllers.auth;

import controllers.JsonResponseController;
import forms.auth.SignIn;
import helpers.core.user.EmailValidatable;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.Messages;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repositories.core.user.FindUserByEmailAndPasswordRepository;
import repositories.core.user.FindUserByPhoneAndPasswordRepository;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * User sign in api controller.
 */
public class SignInApiController extends Controller implements EmailValidatable {

    private final HttpExecutionContext executionContext;
    private final FormFactory formFactory;
    private final FindUserByPhoneAndPasswordRepository findUserByPhoneAndPasswordRepository;
    private final FindUserByEmailAndPasswordRepository findUserByEmailAndPasswordRepository;

    @Inject
    public SignInApiController(
            HttpExecutionContext executionContext, FormFactory formFactory,
            FindUserByPhoneAndPasswordRepository findUserByPhoneAndPasswordRepository,
            FindUserByEmailAndPasswordRepository findUserByEmailAndPasswordRepository
    ) {
        this.executionContext = executionContext;
        this.formFactory = formFactory;
        this.findUserByPhoneAndPasswordRepository = findUserByPhoneAndPasswordRepository;
        this.findUserByEmailAndPasswordRepository = findUserByEmailAndPasswordRepository;
    }

    /**
     * @return Result of user sign in action.
     */
    public CompletionStage<Result> signInAction(){

        Messages messages = Http.Context.current().messages();

        Form<SignIn> signInForm = formFactory.form(SignIn.class).bindFromRequest();
        if(signInForm.hasErrors()){

            return CompletableFuture.completedFuture(
                    badRequest(
                            signInForm.errorsAsJson()
                    )
            );
        }

        SignIn signIn = signInForm.get();

        if(EmailValidatable.super.validate(signIn.userdata)){

            return signInWithEmail(signIn);
        }

        return signInWithPhone(signIn);
    }

    /**
     * @param signIn
     * @return Result of sign in with e-mail
     */
    private CompletionStage<Result> signInWithEmail(SignIn signIn){

        Messages messages = Http.Context.current().messages();

        return findUserByEmailAndPasswordRepository.findUserByEmailAndPassword(signIn.userdata, signIn.password)
                .thenApplyAsync(user -> {
                    if(user.isPresent() && user.get() != null){
                        session().put("username", user.get().username);
                        return ok(
                                JsonResponseController.buildJsonResponse(
                                        "success", messages.at("signIn.success")
                                )
                        );
                    }
                    return badRequest(
                            JsonResponseController.buildJsonResponse(
                                    "error", messages.at("signIn.badEmailOrPassword")
                            )
                    );
                }, executionContext.current());
    }

    /**
     * @param signIn
     * @return Result sign in with phone
     */
    private CompletionStage<Result> signInWithPhone(SignIn signIn){

        Messages messages = Http.Context.current().messages();

        return findUserByPhoneAndPasswordRepository.findUserByPhoneAndPassword(signIn.userdata, signIn.password)
                .thenApplyAsync(user -> {
                    if(user.isPresent() && user.get() != null){
                        session().put("username", user.get().username);
                        return ok(
                                JsonResponseController.buildJsonResponse(
                                        "success", messages.at("signIn.success")
                                )
                        );
                    }
                    return badRequest(
                            JsonResponseController.buildJsonResponse(
                                    "success", messages.at("signIn.badPhoneOrPassword")
                            )
                    );
                }, executionContext.current());
    }
}
