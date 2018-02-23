package controllers.auth;

import com.fasterxml.jackson.databind.node.ObjectNode;
import forms.auth.SignUp;
import models.core.user.UserByEmailFindable;
import models.core.user.UserByPhoneFindable;
import models.core.user.UserModel;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.Messages;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repositories.core.user.CreateUserRepository;
import services.user.CreateUserServiceAbstraction;
import services.user.CreateUserServiceImplementation;

import javax.inject.Inject;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Sign up api.
 */
public class SignUpApiController extends Controller implements UserByEmailFindable, UserByPhoneFindable,
        CreateUserServiceAbstraction {

    private final CreateUserRepository createUserRepository;
    private final FormFactory formFactory;
    private final HttpExecutionContext executionContext;

    @Inject
    public SignUpApiController(
            CreateUserRepository createUserRepository, FormFactory formFactory,
            HttpExecutionContext executionContext
    ) {
        this.createUserRepository = createUserRepository;
        this.formFactory = formFactory;
        this.executionContext = executionContext;
    }

    /**
     * @return Result create user actions with json response
     */
    public CompletionStage<Result> signUpAction(){

        Messages messages = Http.Context.current().messages();

        Form<SignUp> signUpForm = formFactory.form(SignUp.class).bindFromRequest();
        if(signUpForm.hasErrors()){

            return CompletableFuture.completedFuture(badRequest(signUpForm.errorsAsJson()));
        }

        SignUp signUp = signUpForm.get();

        if(!signUp.password.equals(signUp.confirmPassword)){

            return CompletableFuture.completedFuture(
                    badRequest(buildJsonResponse("error", messages.at("signUp.passwordsMismatch")))
            );
        }
        
        if(!checkEmail(signUp.email)){
            return CompletableFuture.completedFuture(
                    badRequest(buildJsonResponse("error", messages.at("signUp.emailExists")))
            );
        }

        if(!checkPhone(signUp.phone)){
            return CompletableFuture.completedFuture(
                    badRequest(buildJsonResponse("error", messages.at("signUp.phoneExists")))
            );
        }

        return createUserRepository.createUser(createUserService(signUp)).thenApplyAsync(user -> {
            if(user.isPresent() && (user.get() != null)){
                return ok(buildJsonResponse("success", messages.at("signUp.success")));
            }
            return badRequest(buildJsonResponse("error", messages.at("signUp.error")));
        }, executionContext.current());
    }

    /**
     * @param email
     * @return true when user is null and false when user is not null
     */
    public boolean checkEmail(String email) {

        UserModel userModel = UserByEmailFindable.super.findUserByEmail(email);
        if(userModel == null){

            return true;
        }

        return false;
    }

    /**
     * @param phone
     * @return true when phone not exists and false when phone exists
     */
    public boolean checkPhone(String phone) {

        UserModel userModel = UserByPhoneFindable.super.findUserByPhone(phone);
        if(userModel == null){

            return true;
        }
        return false;
    }

    private ObjectNode buildJsonResponse(String type, String message){

        ObjectNode wrapper = Json.newObject();
        ObjectNode msg = Json.newObject();
        msg.put("message", message);
        wrapper.put(type, message);
        return wrapper;
    }

    /**
     * @param signUp
     * @return UserModel new user model
     */
    @Override
    public UserModel createUserService(SignUp signUp) {

        CreateUserServiceImplementation createUserServiceImplementation = new CreateUserServiceImplementation();
        return createUserServiceImplementation.createUserService(signUp);
    }
}