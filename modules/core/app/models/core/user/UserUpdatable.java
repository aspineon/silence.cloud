package models.core.user;

/**
 * Update user.
 */
public interface UserUpdatable extends UserByEmailFindable, UserByPhoneFindable {

    /**
     * Update current user.
     *
     * @param userModelId
     * @param userModel
     * @return UserModel user when success or null when failed
     */
    default UserModel updateUser(Long userModelId, UserModel userModel){

        try {

            UserModel currentUser = UserModel.FINDER.byId(userModelId);
            if(currentUser.id == null){

                throw new NullPointerException();
            }

            userModel.update();

            return UserModel.FINDER.query().where().eq("username", userModel.username)
                    .eq("email", userModel.email).eq("phone", userModel.phone).findOne();
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }
}
