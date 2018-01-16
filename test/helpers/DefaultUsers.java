package helpers;

import models.core.user.UserModel;

import java.util.Date;
import java.util.List;

public class DefaultUsers {

    private Long    firstId             = 1L;
    private String  firstUsername       = "John Doe";
    private String  firstEmail          = "john@doe.com";
    private String  firstPhone          = "000000000";
    private String  firstPassword       = "R3v3l@t104LoA";

    private Long    secondId            = 2L;
    private String  secondUsername      = "John Smith";
    private String  secondEmail         = "john@smith.com";
    private String  secondPhone         = "1111111111";
    private String  secondPassword      = "R3v3l@t104LoA";

    private boolean isAdmin = true;

    public void createUsers(){

        createUser(firstId, firstUsername, firstEmail, firstPhone, firstPassword, isAdmin);
        createUser(secondId, secondUsername, secondEmail, secondPhone, secondPassword, isAdmin);
    }

    public void deleteUsers(){

        List<UserModel> users = UserModel.FINDER.all();
        users.forEach(user -> user.delete());
    }

    public void createUser(Long id, String username, String email, String phone, String password, boolean isAdmin){

        UserModel userModel = new UserModel();

        userModel.id = id;
        userModel.createdAt = new Date();
        userModel.updateAt = new Date();
        userModel.username = username;
        userModel.phone = phone;
        userModel.setEmail(email);
        userModel.setPassword(password);
        userModel.isAdmin = isAdmin;

        userModel.save();
    }

}
