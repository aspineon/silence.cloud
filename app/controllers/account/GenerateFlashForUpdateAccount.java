package controllers.account;

import forms.core.Account;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Http;

import java.util.Map;

public class GenerateFlashForUpdateAccount extends Controller {

    public void generateFlashMessages(Map<String, Map> result, Account account){

        generateSuccessMessageToUpdateAccount(result);
        generateDangerMessageToUpdateAccount(result);
        generateWarningMessageToUsername(result);
        generateWarningMessageToAccountEmail(result);
        generateDangerMessageToAccountEmail(result, account);
        generateWarningMessageToAccountPhone(result);
        generateDangerMessageToAccountPhone(result, account);
    }

    private void generateDangerMessageToAccountPhone(Map<String, Map> result, Account account) {

        Messages messages = Http.Context.current().messages();

        if((result.get("phone") != null) && (result.get("phone").get("danger") != null)){

            flash("phoneDanger", messages.at("account.phone.phoneExists", account.phone));
        }
    }

    private void generateWarningMessageToAccountPhone(Map<String, Map> result) {

        Messages messages = Http.Context.current().messages();

        if((result.get("phone") != null) && (result.get("phone").get("warning") != null)){

            flash("phoneWarning", messages.at("account.phone.oldAndNewPhoneAreTheSame"));
        }
    }

    private void generateWarningMessageToUsername(Map<String, Map> result) {

        Messages messages = Http.Context.current().messages();

        if((result.get("username") != null) && (result.get("username").get("warning") != null)){

            flash("usernameWarning", messages.at("account.username.oldAndNewNameAreTheSame"));
        }
    }

    private void generateDangerMessageToAccountEmail(Map<String, Map> result, Account account) {

        Messages messages = Http.Context.current().messages();

        if((result.get("email") != null) && (result.get("email").get("danger") != null)){

            flash("emailDanger", messages.at("account.email.emailExists", account.email));
        }
    }

    private void generateWarningMessageToAccountEmail(Map<String, Map> result) {

        Messages messages = Http.Context.current().messages();

        if((result.get("email") != null) && (result.get("email").get("warning") != null)){

            flash("emailWarning", messages.at("account.email.oldAndNewEmailAreTheSame"));
        }
    }

    private void generateDangerMessageToUpdateAccount(Map<String, Map> result) {
        if((result.get("updateResult") != null) && (result.get("updateResult").get("danger") != null)){

            flash("danger", result.get("updateResult").get("danger").toString());
        }
    }

    private void generateSuccessMessageToUpdateAccount(Map<String, Map> result) {
        if((result.get("updateResult") != null) && (result.get("updateResult").get("success") != null)){

            flash("success", result.get("updateResult").get("success").toString());
        }
    }
}
