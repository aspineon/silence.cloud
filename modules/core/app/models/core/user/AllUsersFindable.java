package models.core.user;

import java.util.List;

/**
 * Find all users.
 */
public interface AllUsersFindable {

    /**
     * Find all users.
     *
     * @return users list
     */
    default List<UserModel> findAllUsers(){

        List<UserModel> users = UserModel.FINDER.all();
        return users;
    }

}
