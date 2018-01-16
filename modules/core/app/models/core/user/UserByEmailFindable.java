package models.core.user;

import helpers.core.user.EmailValidatable;

import java.util.Optional;

public interface UserByEmailFindable extends EmailValidatable {

    default UserModel findUserByEmail(String email){

        try {

            if(email == null){

                throw new NullPointerException();
            }

            return UserModel.FINDER.query().where().eq("email", email.toLowerCase()).findOne();
        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }
}
