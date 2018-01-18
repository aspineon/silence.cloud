package controllers.emails;

import play.Logger;
import play.mvc.Controller;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Properties;

public class SendEmailController extends Controller {

    public void sendEmail(Map<String, String> data) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", data.get("auth"));
        props.put("mail.smtp.starttls.enable", data.get("tls"));
        props.put("mail.smtp.host", data.get("host"));
        props.put("mail.smtp.port", data.get("port"));

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(data.get("username"), data.get("password"));
            }
        });

        try {
            // Creation of a default MimeMessage object.
            Message msge = new MimeMessage(session);
            // Setting From: the header field of the header.
            msge.setFrom(new InternetAddress(data.get("from")));
            // Setting To: the header field of the header.
            msge.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(data.get("to")));
            // Setting the Subject: the header field
            msge.setSubject(data.get("subject"));
            // Now, we set the actual message to be sent
            msge.setText(data.get("text"));
            // Sending of the message
            Transport.send(msge);
            Logger.info("The message has been successfully sent....");
        } catch (MessagingException excp) {
            throw new RuntimeException(excp);
        }
    }
}
