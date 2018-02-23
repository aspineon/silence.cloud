package services.user;

import forms.auth.SignUp;
import models.core.user.UserModel;

public interface CreateUserServiceAbstraction {

    UserModel createUserService(SignUp signUp);
}
