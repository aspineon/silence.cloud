package actors.sms;

import akka.actor.AbstractActor;
import models.sms.SingleSmsModel;
import pl.smsapi.BasicAuthClient;
import pl.smsapi.api.SmsFactory;
import pl.smsapi.api.action.sms.SMSSend;
import pl.smsapi.api.response.MessageResponse;
import pl.smsapi.api.response.StatusResponse;
import pl.smsapi.exception.ClientException;
import pl.smsapi.exception.SmsapiException;
import play.Logger;
import protocols.sms.SingleSmsProtocol;

import java.util.HashMap;
import java.util.Optional;

public class SingleSmsActor extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SingleSmsProtocol.class, singleSmsProtocol -> {

                    switch (singleSmsProtocol.getAction()){
                        case SEND:
                            sender().tell(
                                    sendSingleSms(singleSmsProtocol.getConfig(), singleSmsProtocol.getSingleSmsModel()
                                    ), self());
                            break;
                    }
                }).matchAny(any -> unhandled("unhandled action" + any.getClass())).build();
    }

    public Optional<SingleSmsModel> sendSingleSms(HashMap<String, String> config, SingleSmsModel singleSmsModel){

        try {
            BasicAuthClient client = getAuthorizationClient(config);

            SmsFactory smsApi = new SmsFactory(client);
            String phoneNumber = singleSmsModel.phone;
            SMSSend action = smsApi.actionSend()
                    .setText(singleSmsModel.message)
                    .setTo(phoneNumber);

            StatusResponse result = action.execute();

            for (MessageResponse status : result.getList() ) {
                System.out.println(status.getNumber() + " " + status.getStatus());
            }
            return Optional.of(singleSmsModel);
        } catch (ClientException e) {
            Logger.error(e.getMessage(), e);
            return Optional.empty();
        } catch (SmsapiException e) {
            Logger.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    private BasicAuthClient getAuthorizationClient(HashMap<String, String> config){

        try {
            return BasicAuthClient.createFromRawPassword(config.get("username"), config.get("password"));
        } catch (ClientException ex) {
            return null;
        }
    }
}
