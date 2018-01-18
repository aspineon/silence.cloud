package controllers.auth;

import controllers.emails.SendEmailController;
import models.core.user.UserModel;
import play.Play;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.Map;
import java.util.concurrent.CompletionStage;

public class RemindPasswordMessageController extends Controller {

    private Map<String, String> settings;

    public void sendEmail(UserModel userModel, String newPassword) throws Exception {

        Messages messages = Http.Context.current().messages();

        settings.put("host", Play.application().configuration().getString("play.mail.host"));
        settings.put("auth", Play.application().configuration().getString("play.mail.auth"));
        settings.put("port", Play.application().configuration().getString("play.mail.port"));
        settings.put("ssl", Play.application().configuration().getString("play.mail.ssl"));
        settings.put("tls", Play.application().configuration().getString("play.mail.tls"));
        settings.put("tlsRequired", Play.application().configuration().getString("play.mail.tlsRequired"));
        settings.put("username", Play.application().configuration().getString("play.mail.username"));
        settings.put("password", Play.application().configuration().getString("play.mail.password"));
        settings.put("from", Play.application().configuration().getString("play.mail.username"));
        settings.put("to", Play.application().configuration().getString(userModel.email));
        settings.put("title", messages.at("remindPassword.email.title"));
        settings.put("message", messages.at("remindPassword.email.message", userModel.username, newPassword));

        SendEmailController sendEmailController = new SendEmailController();
        try {
            sendEmailController.sendEmail(settings);
        } catch (RuntimeException e) {
            throw new RuntimeException();
        }
    }
}
