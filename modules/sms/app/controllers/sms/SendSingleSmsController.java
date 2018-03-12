package controllers.sms;

import akka.actor.ActorRef;
import akka.pattern.PatternsCS;
import models.sms.SingleSmsModel;
import pl.smsapi.BasicAuthClient;
import pl.smsapi.api.SmsFactory;
import pl.smsapi.api.action.sms.SMSSend;
import pl.smsapi.api.response.MessageResponse;
import pl.smsapi.api.response.StatusResponse;
import pl.smsapi.exception.ClientException;
import pl.smsapi.exception.SmsapiException;
import play.Logger;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import protocols.sms.SingleSmsProtocol;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class SendSingleSmsController extends Controller {

    private final HttpExecutionContext executionContext;
    private final ActorRef singleSmsActor;

    @Inject
    public SendSingleSmsController(@Named("single-sms-actor") ActorRef singleSmsActor, HttpExecutionContext executionContext) {
        this.executionContext = executionContext;
        this.singleSmsActor = singleSmsActor;
    }

    public CompletionStage<Boolean> sendToken(SingleSmsModel singleSmsModel, HashMap<String, String> config) {

        return PatternsCS.ask(
                singleSmsActor, new SingleSmsProtocol(SingleSmsProtocol.Action.SEND, singleSmsModel, config), 1000
        ).thenApplyAsync(response -> (Optional<SingleSmsModel>) response).thenApplyAsync(singleSmsOptional -> {
            if (singleSmsOptional.isPresent()) {

                return true;
            }
            return false;
        }, executionContext.current());

    }
}
