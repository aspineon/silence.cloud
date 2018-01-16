package models.core.user;

import models.core.user.UserModel;

public interface UserDeletable {

    default UserModel deleteUser(Long id){

        try {

            UserModel userModel = UserModel.FINDER.byId(id);
            if(userModel == null){

                throw new Exception();
            }

            userModel.delete();

            return UserModel.FINDER.byId(id);
        } catch (Exception e) {

            e.printStackTrace();
            return new UserModel();
        }
    }
}
