package models.core.user;

import java.util.Date;

/**
 * Create new user.
 */
public interface UserCreatable extends UserByEmailFindable, UserByPhoneFindable {

    default UserModel createNewUser(UserModel userModel){

        try {

            if(
                    (
                            UserModel.FINDER.query().where().eq("email", userModel.email.toLowerCase())
                                    .findOne() != null
                    ) || (
                            UserModel.FINDER.query().where().eq("phone", userModel.phone)
                                    .findOne() != null)
                    ){

                throw  new NullPointerException();
            }

            userModel.id = System.currentTimeMillis();
            userModel.createdAt = new Date();
            userModel.updatedAt = new Date();
            userModel.save();

            return UserModel.FINDER.query().where().eq("username", userModel.username)
                    .eq("email", userModel.email).eq("isAdmin", userModel.isAdmin).findOne();
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

}
