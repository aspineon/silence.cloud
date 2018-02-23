package controllers.auth;

import models.core.user.UserModel;

import java.util.Date;

public class CreateDefaultUser {

    private String existsEmail = "john@doe.com";
    private String existsPhone = "000000000";

    private String existsPassword = "R3v3l@t104LoA";

    public void createDefaultUser() {
        UserModel userModel = new UserModel();
        userModel.id = System.currentTimeMillis();
        userModel.updateAt = new Date();
        userModel.createdAt = new Date();
        userModel.username = "john doe";
        userModel.setEmail(existsEmail);
        userModel.phone = existsPhone;
        userModel.setPassword(existsPassword);
        userModel.isAdmin = true;
        userModel.setUuid();
        userModel.setToken();
        userModel.save();
    }
}
