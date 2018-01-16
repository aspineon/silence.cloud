package models.core.user;

/**
 * Find user by phone and password.
 */
public interface UserByPhoneAndPasswordFindable {

    /**
     * Find user by phone and password.
     * @param phone
     * @param password
     * @return UserModel user when succes or null when failed.
     */
    default UserModel findUserByPhoneAndPassword(String phone, String password) {

        try {

            if((phone == null) || (password == null)){

                return null;
            }

            return UserModel.FINDER.query().where().eq("phone", phone)
                    .eq("shaPassword", UserModel.getSha512(password)).findOne();
        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

}
