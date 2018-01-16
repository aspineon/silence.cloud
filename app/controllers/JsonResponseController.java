package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;

public class JsonResponseController extends Controller {

    public ObjectNode buildJsonResponse(String type, String message){

        ObjectNode wrapper = Json.newObject();
        ObjectNode msg = Json.newObject();
        msg.put("message", message);
        wrapper.put(type, message);
        return wrapper;
    }
}
