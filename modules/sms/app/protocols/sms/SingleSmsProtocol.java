package protocols.sms;

import models.sms.SingleSmsModel;

import java.util.HashMap;

public class SingleSmsProtocol {

    public enum Action {
        SEND
    }

    private Action action;
    private SingleSmsModel singleSmsModel;
    private HashMap<String, String> config;

    public SingleSmsProtocol(Action action, SingleSmsModel singleSmsModel, HashMap<String, String> config){

        this.action = action;
        this.singleSmsModel = singleSmsModel;
        this.config = config;
    }

    public Action getAction() {
        return action;
    }

    public SingleSmsModel getSingleSmsModel() {
        return singleSmsModel;
    }

    public HashMap<String, String> getConfig() {
        return config;
    }
}
