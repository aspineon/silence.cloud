package services.core.account;

import forms.core.Account;
import models.core.user.UserByEmailFindable;
import models.core.user.UserByPhoneFindable;
import models.core.user.UserModel;
import play.i18n.Lang;
import play.i18n.Messages;
import play.i18n.MessagesApi;

import javax.inject.Inject;
import java.util.*;

public class CheckAccountData implements UserByEmailFindable, UserByPhoneFindable {

    private final play.i18n.MessagesApi messagesApi;

    @Inject
    public CheckAccountData(MessagesApi messagesApi) {
        this.messagesApi = messagesApi;
    }

    public Map<String, Map> checkAccount(Account account, UserModel user){

        Map<String, Map> messages = new HashMap<>();

        if(account.username.equals(user.username)){
            messages.put("username", oldAndNewUsernameAreTheSame());
        }

        if(account.email.equals(user.email)){
            messages.put("email", oldAndNewEmailAreTheSame());
        }

        if(account.phone.equals(user.phone)){
            messages.put("phone", oldAndNewPhoneAreTheSame());
        }

        if((UserByEmailFindable.super.findUserByEmail(account.email) != null) && !account.email.equals(user.email)){
            messages.put("email", emailExists(account.email));
        }

        if((UserByPhoneFindable.super.findUserByPhone(account.phone) != null) && !account.phone.equals(user.phone)){
            messages.put("phone", phoneExists(account.phone));
        }

        return messages;
    }

    private Map<String, String> oldAndNewUsernameAreTheSame(){


        Map<String, String> usernameExists = new HashMap<>();

        Collection<Lang> candidates = Collections.singletonList(new Lang(Locale.US));
        Messages messages = messagesApi.preferred(candidates);
        usernameExists.put("warning", messages.at("account.email.oldAndNewUsernameAreTheSame"));

        return usernameExists;
    }

    private Map<String, String> oldAndNewEmailAreTheSame(){


        Map<String, String> emailExists = new HashMap<>();

        Collection<Lang> candidates = Collections.singletonList(new Lang(Locale.US));
        Messages messages = messagesApi.preferred(candidates);
        emailExists.put("warning", messages.at("account.email.oldAndNewEmailAreTheSame"));

        return emailExists;
    }

    private Map<String, String> oldAndNewPhoneAreTheSame(){

        Map<String, String> phoneExists = new HashMap<>();

        Collection<Lang> candidates = Collections.singletonList(new Lang(Locale.US));
        Messages messages = messagesApi.preferred(candidates);
        phoneExists.put("warning", messages.at("account.phone.oldAndNewPhoneAreTheSame"));

        return phoneExists;
    }

    private Map<String, String> emailExists(String email){

        Map<String, String> emailExists = new HashMap<>();

        Collection<Lang> candidates = Collections.singletonList(new Lang(Locale.US));
        Messages messages = messagesApi.preferred(candidates);
        emailExists.put("danger", messages.at("account.email.emailExists", email));

        return emailExists;
    }

    private Map<String, String> phoneExists(String phone){

        Map<String, String> phoneExists = new HashMap<>();

        Collection<Lang> candidates = Collections.singletonList(new Lang(Locale.US));
        Messages messages = messagesApi.preferred(candidates);
        phoneExists.put("danger", messages.at("account.phone.phoneExists", phone));

        return phoneExists;
    }
}
