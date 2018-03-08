package controllers.account;

import controllers.account.routes;
import controllers.core.AccountSecurityController;
import forms.core.Account;
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
import repositories.core.user.FindUserByEmailRepository;
import repositories.core.user.UpdateUserRepository;
import services.core.account.CheckAccountData;
import views.html.account.user;

import javax.inject.Inject;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Security.Authenticated(AccountSecurityController.class)
public class UpdateAccountController extends Controller implements UserByEmailFindable {

    private final FindUserByEmailRepository findUserByEmailRepository;
    private final UpdateUserRepository updateUserRepository;
    private final HttpExecutionContext executionContext;
    private final FormFactory formFactory;
    private final CheckAccountData checkAccountData;

    @Inject
    public UpdateAccountController(
            FindUserByEmailRepository findUserByEmailRepository, UpdateUserRepository updateUserRepository,
            HttpExecutionContext executionContext, FormFactory formFactory, CheckAccountData checkAccountData
    ) {
        this.findUserByEmailRepository = findUserByEmailRepository;
        this.updateUserRepository = updateUserRepository;
        this.executionContext = executionContext;
        this.formFactory = formFactory;
        this.checkAccountData = checkAccountData;
    }

    @RequireCSRFCheck
    public CompletionStage<Result> updateAccount(){

        Messages messages = Http.Context.current().messages();

        Form<Account> accountForm = formFactory.form(Account.class).bindFromRequest();
        if(accountForm.hasErrors()){
            flash("danger", messages.at("account.accountHasNotBeenUpdated"));
            return CompletableFuture.completedFuture(redirect(controllers.account.routes.UserController.getUser()));
        }

        Account account = accountForm.get();

        Map<String, Map> checkOutResult = checkAccountData.checkAccount(
                account, UserByEmailFindable.super.findUserByEmail(session().get("username"))
        );

        if(
                ((checkOutResult.get("phone") != null) && (checkOutResult.get("phone").get("danger") != null)) ||
                        ((checkOutResult.get("email") != null) && (checkOutResult.get("email").get("danger") != null))
                ){

            Map<String, String> updateAccountResult = new HashMap<>();
            updateAccountResult.put("danger", messages.at("account.accountHasNotBeenUpdated"));
            checkOutResult.put("updateResult", updateAccountResult);
            return accountHasNotBeenUpdated(checkOutResult, account);
        } else {

            return updateUserAccount(account, checkOutResult);
        }
    }

    @AddCSRFToken
    public CompletionStage<Result> updateUserAccount(Account account, Map<String, Map> result){

        Messages messages = Http.Context.current().messages();

        UserModel userModel = UserByEmailFindable.super.findUserByEmail(session().get("username"));
        userModel.username = account.username;
        userModel.email = account.email;
        userModel.phone = account.phone;

        return updateUserRepository.updateUser(userModel).thenApplyAsync(user -> {

            if(user.isPresent() && (user.get() != null)){

                AccountSecurityController.generateSecurityToken(user);

                Map<String, String> updateAccountResult = new HashMap<>();

                updateAccountResult.put("success", messages.at("account.accountHasBeenUpdated"));
                result.put("updateResult", updateAccountResult);

                generateFlashMessages(result, account);

                return ok(views.html.account.user.render(user.get()));
            }

            Map<String, String> updateAccountResult = new HashMap<>();

            updateAccountResult.put("danger", messages.at("account.accountHasNotBeenUpdated"));
            result.put("updateResult", updateAccountResult);

            generateFlashMessages(result, account);

            AccountSecurityController.generateSecurityToken(
                    Optional.ofNullable(UserByEmailFindable.super.findUserByEmail(session().get("username")))
            );

            return badRequest(
                    views.html.account.user.render(UserByEmailFindable.super.findUserByEmail(session().get("username")))
            );
        }, executionContext.current());
    }

    @AddCSRFToken
    public CompletionStage<Result> accountHasNotBeenUpdated(Map<String, Map> result, Account account){

        Messages messages = Http.Context.current().messages();

        return findUserByEmailRepository.findUserByEmail(session().get("username")).thenApplyAsync(user -> {

            if(user.isPresent() && (user.get() != null)){

                AccountSecurityController.generateSecurityToken(user);

                generateFlashMessages(result, account);
                return badRequest(views.html.account.user.render(user.get()));
            }

            return redirect(controllers.account.routes.UserController.getUser());
        }, executionContext.current());
    }

    private void generateFlashMessages(Map<String, Map> result, Account account){

        GenerateFlashForUpdateAccount generateFlashForUpdateAccount = new GenerateFlashForUpdateAccount();
        generateFlashForUpdateAccount.generateFlashMessages(result, account);
    }
}
