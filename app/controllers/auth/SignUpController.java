package controllers.auth;

import forms.auth.SignUp;
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
import repositories.core.user.CreateUserRepository;
import services.user.CreateUserServiceAbstraction;
import services.user.CreateUserServiceImplementation;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class SignUpController extends Controller implements UserByEmailFindable, UserByPhoneFindable,
        CreateUserServiceAbstraction {

    private final CreateUserRepository createUserRepository;
    private final FormFactory formFactory;
    private final HttpExecutionContext executionContext;

    @Inject
    public SignUpController(
            CreateUserRepository createUserRepository, FormFactory formFactory,
            HttpExecutionContext executionContext
    ) {
        this.createUserRepository = createUserRepository;
        this.formFactory = formFactory;
        this.executionContext = executionContext;
    }

    @AddCSRFToken
    public CompletionStage<Result> getSignUpForm(){

        Form<SignUp> signUpForm = formFactory.form(SignUp.class);
        return CompletableFuture.completedFuture(ok(views.html.auth.signUp.render(signUpForm)));
    }

    @RequireCSRFCheck
    public CompletionStage<Result> signUpAction(){

        Messages messages = Http.Context.current().messages();

        Form<SignUp> signUpForm = formFactory.form(SignUp.class).bindFromRequest();
        if(signUpForm.hasErrors()){
            flash("danger", messages.at("signUp.formError"));
            return CompletableFuture.completedFuture(
                    badRequestAction("danger", messages.at("signUp.formError"))
            );
        }

        SignUp signUp = signUpForm.get();

        if(!signUp.password.equals(signUp.confirmPassword)){

            flash("warning", messages.at("signUp.passwordsMismatch"));
            return CompletableFuture.completedFuture(
                    badRequestAction("warning", messages.at("signUp.passwordsMismatch"))
            );
        }

        if(UserByEmailFindable.super.findUserByEmail(signUp.email) != null) {

            flash("waring", messages.at("signUp.emailExists"));
            return CompletableFuture.completedFuture(
                    badRequestAction("warning", messages.at("signUp.emailExists"))
            );
        }

        if(UserByPhoneFindable.super.findUserByPhone(signUp.phone) != null) {

            flash("waring", messages.at("signUp.phoneExists"));
            return CompletableFuture.completedFuture(
                    badRequestAction("warning", messages.at("signUp.phoneExists"))
            );
        }

        return createUserRepository.createUser(createUserService(signUp)).thenApplyAsync(user -> {
            if(user.isPresent() && user.get() != null) {
                flash("success", messages.at("signUp.success"));
                return ok(views.html.auth.signUp.render(signUpForm));
            }

            return badRequestAction("danger", messages.at("signUp.error"));
        }, executionContext.current());
    }


    @AddCSRFToken
    private Result badRequestAction(String type, String message){

        Form<SignUp> signUpForm = formFactory.form(SignUp.class);

        flash(type, message);
        return badRequest(views.html.auth.signUp.render(signUpForm));
    }

    /**
     * @param signUp
     * @return new UserModel
     */
    @Override
    public UserModel createUserService(SignUp signUp){

        CreateUserServiceImplementation createUserServiceImplementation = new CreateUserServiceImplementation();
        return createUserServiceImplementation.createUserService(signUp);
    }
}
