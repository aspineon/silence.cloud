package models.core.user;

import java.util.Optional;

public interface UserByIdFindable {

    default Optional<UserModel> findUserById(Long id){

        Optional<UserModel>  userModel = Optional.empty();

        try {

            Optional<UserModel> currentUser = Optional.ofNullable(UserModel.FINDER.byId(id));
            if(!currentUser.isPresent() || userModel == null){

                throw  new NullPointerException();
            }

            userModel = currentUser;
        } catch (Exception e){

            e.printStackTrace();
        } finally {

            return userModel;
        }
    }

}
