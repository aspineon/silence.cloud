package services.user;

import forms.auth.SignUp;
import models.core.user.UserModel;

import java.util.Date;

public class CreateUserServiceImplementation implements CreateUserServiceAbstraction {

    @Override
    public UserModel createUserService(SignUp signUp) {

        UserModel userModel = new UserModel();
        userModel.id = System.currentTimeMillis();
        userModel.createdAt = new Date();
        userModel.updateAt = new Date();
        userModel.username = signUp.username;
        userModel.setEmail(signUp.email);
        userModel.setPassword(signUp.password);
        userModel.phone = signUp.phone;
        userModel.isAdmin = true;
        userModel.setToken();
        userModel.setUuid();

        return userModel;
    }
}
