package controllers.userAdmin;

import controllers.core.AccountSecurityController;
import controllers.core.UserAdminSecurityController;
import controllers.userAdmin.routes;
import forms.core.NewUser;
import models.core.user.UserByEmailFindable;
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
import play.mvc.Security;
import repositories.core.user.CreateUserRepository;
import repositories.core.user.FindUserByEmailRepository;
import repositories.core.user.FindUserByPhoneRepository;

import javax.inject.Inject;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@Security.Authenticated(UserAdminSecurityController.class)
public class CreateUserController extends Controller implements UserByEmailFindable {

    private final FindUserByEmailRepository findUserByEmailRepository;
    private final FindUserByPhoneRepository findUserByPhoneRepository;
    private final CreateUserRepository createUserRepository;

    private final FormFactory formFactory;

    private final HttpExecutionContext executionContext;

    @Inject
    public CreateUserController(
            FindUserByEmailRepository findUserByEmailRepository, FindUserByPhoneRepository findUserByPhoneRepository,
            CreateUserRepository createUserRepository, FormFactory formFactory, HttpExecutionContext executionContext
    ) {

        this.findUserByEmailRepository = findUserByEmailRepository;
        this.findUserByPhoneRepository = findUserByPhoneRepository;
        this.createUserRepository = createUserRepository;
        this.formFactory = formFactory;
        this.executionContext = executionContext;
    }

    @RequireCSRFCheck
    public CompletionStage<Result> createUser(){

        return findUserByEmailRepository.findUserByEmail(session().get("username")).thenApplyAsync(user -> {
            if(user.isPresent() && (user.get() != null)){
                AccountSecurityController.generateSecurityToken(user);

                return tryToCreateAccount(user.get());
            }

            return redirect(controllers.auth.routes.SignInController.signInForm());
        }, executionContext.current());
    }

    @AddCSRFToken
    private Result tryToCreateAccount(UserModel userModel){

        Messages messages = Http.Context.current().messages();

        Form<NewUser> newUserForm = formFactory.form(NewUser.class).bindFromRequest();
        if(newUserForm.hasErrors()){

            flash("danger", messages.at("userAdmin.addUser.userHasNotBeenCreated"));
            return badRequest(views.html.userAdmin.addUser.render(userModel));
        }

        NewUser newUser= newUserForm.get();

        try {

            checkoutData(newUser);

            if(flash().containsKey("danger")){

                return badRequest(views.html.userAdmin.addUser.render(userModel));
            }

            return createNewUser(userModel, newUser);

        }catch (Exception e){

            e.printStackTrace();
            return badRequest(views.html.userAdmin.addUser.render(userModel));
        }
    }

    private void checkoutData(NewUser newUser) throws InterruptedException, java.util.concurrent.ExecutionException {

        Messages messages = Http.Context.current().messages();

        Optional<UserModel> userByEmail = findUserByEmailRepository.findUserByEmail(newUser.email)
                .toCompletableFuture().get();
        Optional<UserModel> userByPhone = findUserByPhoneRepository.findUserByPhone(newUser.phone)
                        .toCompletableFuture().get();

        if(userByEmail.isPresent()){

            flash("emailWarning", messages.at("account.email.emailExists", newUser.email));
        }

        if(userByPhone.isPresent()){

            flash("phoneWarning", messages.at("account.phone.phoneExists", newUser.phone));
        }

        if(!newUser.password.equals(newUser.confirmPassword)){

            flash("confirmPasswordWarning", messages.at("account.password.passwordsMismatch"));
        }

        if(flash().containsKey("emailWarning") || flash().containsKey("phoneWarning") || flash().containsKey("confirmPasswordWarning")){

            flash("danger", messages.at("userAdmin.addUser.userHasNotBeenCreated"));
        }
    }

    private Result createNewUser(UserModel userModel, NewUser newUser) throws InterruptedException, java.util.concurrent.ExecutionException {

        Messages messages = Http.Context.current().messages();

        UserModel currentUser = createNewUserModel(newUser);
        Optional<UserModel> createdUser = createUserRepository.createUser(currentUser).toCompletableFuture().get();
        if(createdUser.isPresent() && (createdUser.get() != null)){

            flash("success", messages.at("userAdmin.addUser.userHasBeenCreated"));
            return redirect(controllers.userAdmin.routes.AllUsersController.getAllUsers());
        }

        flash("danger", messages.at("userAdmin.addUser.userHasNotBeenCreated"));
        return badRequest(views.html.userAdmin.addUser.render(userModel));
    }

    private UserModel createNewUserModel(NewUser newUser) {

        UserModel user = new UserModel();
        user.id = System.currentTimeMillis();
        user.createdAt = new Date();
        user.updatedAt = new Date();
        user.setToken();
        user.setUuid();
        user.username = newUser.username;
        user.setEmail(newUser.email);
        user.phone = newUser.phone;
        user.setPassword(newUser.password);
        if(newUser.isAdmin != null){
            user.isAdmin = true;
        }else {
            user.isAdmin = false;
        }
        user.isActive = false;
        return user;
    }
}
