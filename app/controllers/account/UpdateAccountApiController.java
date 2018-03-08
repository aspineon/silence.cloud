package controllers.account;

import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.core.AccountApiSecurityController;
import controllers.core.AccountSecurityController;
import forms.core.Account;
import models.core.user.UserByEmailFindable;
import models.core.user.UserModel;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import repositories.core.user.FindUserByEmailRepository;
import repositories.core.user.UpdateUserRepository;
import services.core.account.CheckAccountData;

import javax.inject.Inject;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Security.Authenticated(AccountApiSecurityController.class)
public class UpdateAccountApiController extends Controller implements UserByEmailFindable {

    private final FindUserByEmailRepository findUserByEmailRepository;
    private final UpdateUserRepository updateUserRepository;
    private final HttpExecutionContext executionContext;
    private final FormFactory formFactory;
    private final CheckAccountData checkAccountData;

    @Inject
    public UpdateAccountApiController(
            FindUserByEmailRepository findUserByEmailRepository, UpdateUserRepository updateUserRepository,
            HttpExecutionContext executionContext, FormFactory formFactory, CheckAccountData checkAccountData
    ) {
        this.findUserByEmailRepository = findUserByEmailRepository;
        this.updateUserRepository = updateUserRepository;
        this.executionContext = executionContext;
        this.formFactory = formFactory;
        this.checkAccountData = checkAccountData;
    }

    public CompletionStage<Result> updateAccount(){

        Form<Account> accountForm = formFactory.form(Account.class).bindFromRequest();
        if(accountForm.hasErrors()){
            AccountSecurityController.generateSecurityToken(
                    Optional.of(UserByEmailFindable.super.findUserByEmail(session().get("username")))
            );
            return CompletableFuture.completedFuture(badRequest(accountForm.errorsAsJson()));
        }

        Account account = accountForm.get();

        Map<String, Map> checkOutResult = checkAccountData.checkAccount(
                account, UserByEmailFindable.super.findUserByEmail(session().get("username"))
        );

        if(
                ((checkOutResult.get("phone") != null) && (checkOutResult.get("phone").get("danger") != null)) ||
                        ((checkOutResult.get("email") != null) && (checkOutResult.get("email").get("danger") != null))
                ){

            return badUpdateRequest();
        }

        return updateUserRequest(account);
    }

    private CompletionStage<Result> updateUserRequest(Account account) {

        UserModel userModel = UserByEmailFindable.super.findUserByEmail(session().get("username"));
        userModel.username = account.username;
        userModel.email = account.email;
        userModel.phone = account.phone;
        userModel.updatedAt = new Date();

        return updateUserRepository.updateUser(userModel).thenApplyAsync(user -> {
            if(user.isPresent() && (user.get() != null)){

                ObjectNode wrapper = Json.newObject();
                wrapper.put("success", "true");
                wrapper.put("messages", Json.toJson(checkAccountData));
                wrapper.put("user", Json.toJson(user));
                AccountSecurityController.generateSecurityToken(user);
                return badRequest(wrapper);
            }

            return unauthorized();
        }, executionContext.current());
    }

    private CompletionStage<Result> badUpdateRequest() {

        ObjectNode wrapper = Json.newObject();
        wrapper.put("error", "true");
        wrapper.put("messages", Json.toJson(checkAccountData));

        return findUserByEmailRepository.findUserByEmail(session().get("email")).thenApplyAsync(user -> {

            if(user.isPresent() && (user.get() != null)){

                wrapper.put("user", Json.toJson(user));
                AccountSecurityController.generateSecurityToken(user);
                return badRequest(wrapper);
            }

            return unauthorized();
        }, executionContext.current());
    }
}
