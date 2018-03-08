package controllers.auth;

import forms.auth.SignIn;
import helpers.core.user.EmailValidatable;
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
import repositories.core.user.FindUserByEmailAndPasswordRepository;
import repositories.core.user.FindUserByPhoneAndPasswordRepository;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * User sign in controller.
 */
public class SignInController extends Controller implements EmailValidatable {

    public final String AUTH_TOKEN = "authToken";

    private final FindUserByEmailAndPasswordRepository findUserByEmailAndPasswordRepository;
    private final FindUserByPhoneAndPasswordRepository findUserByPhoneAndPasswordRepository;
    private final HttpExecutionContext executionContext;
    private final FormFactory formFactory;

    @Inject
    public SignInController(
            FindUserByEmailAndPasswordRepository findUserByEmailAndPasswordRepository,
            FindUserByPhoneAndPasswordRepository findUserByPhoneAndPasswordRepository,
            HttpExecutionContext executionContext, FormFactory formFactory
    ) {
        this.findUserByEmailAndPasswordRepository = findUserByEmailAndPasswordRepository;
        this.findUserByPhoneAndPasswordRepository = findUserByPhoneAndPasswordRepository;
        this.executionContext = executionContext;
        this.formFactory = formFactory;
    }

    /**
     * @return Result of get sign in form
     */
    @AddCSRFToken
    public CompletionStage<Result> signInForm(){

        Messages messages = Http.Context.current().messages();
        Form<SignIn> signInForm = formFactory.form(SignIn.class);

        return CompletableFuture.completedFuture(ok(views.html.auth.signIn.render(signInForm)));
    }

    /**
     * @return Result of user sign in
     */
    @RequireCSRFCheck
    public CompletionStage<Result> signInAction() {

        Messages messages = Http.Context.current().messages();

        Form<SignIn> signInForm  = formFactory.form(SignIn.class).bindFromRequest();
        if(signInForm.hasErrors()){
            return CompletableFuture.completedFuture(
                    badRequestAction("danger", messages.at("signIn.formError"))
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
     * @return Result sign in with e-mail
     */
    private CompletionStage<Result> signInWithEmail(SignIn signIn){

        Messages messages = Http.Context.current().messages();

        return findUserByEmailAndPasswordRepository.findUserByEmailAndPassword(signIn.userdata, signIn.password)
                .thenApplyAsync(user -> {
                    if(user.isPresent() && (user.get().id != null)){
                        return createSession(user);
                    }
                    return badRequestAction("danger", messages.at("signIn.badEmailOrPassword"));
                }, executionContext.current());
    }

    /**
     * @param signIn
     * @return Result of sign in with phone
     */
    private CompletionStage<Result> signInWithPhone(SignIn signIn){

        Messages messages = Http.Context.current().messages();

        return findUserByPhoneAndPasswordRepository.findUserByPhoneAndPassword(signIn.userdata, signIn.password)
                .thenApplyAsync(user -> {
                    if(user.isPresent() && (user.get().id != null)){
                        return createSession(user);
                    }
                    return badRequestAction("danger", messages.at("signIn.badPhoneOrPassword"));
                }, executionContext.current());
    }

    /**
     * @param user
     * @return Result ok
     */
    private Result createSession(Optional<UserModel> user) {
        response().setCookie(Http.Cookie.builder(AUTH_TOKEN, user.get().token)
                .withSecure(ctx().request().secure()).withHttpOnly(true).build());
        session().put("username", user.get().email);
        return redirect(controllers.routes.HomeController.index());
    }

    @AddCSRFToken
    private Result badRequestAction(String type, String message){

        Form<SignIn> signInForm = formFactory.form(SignIn.class);

        flash(type, message);
        return badRequest(views.html.auth.signIn.render(signInForm));
    }
}
