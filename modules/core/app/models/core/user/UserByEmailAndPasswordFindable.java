package models.core.user;

/**
 * Find user by email and password.
 */
public interface UserByEmailAndPasswordFindable {

    /**
     * Find user by email and phone.
     *
     * @param email
     * @param password
     * @return UserModel user when success or null when failed.
     */
    default UserModel findUserByEmailAndPassword(String email, String password){

        try {

            if((email == null) || (password == null)){

                throw new NullPointerException();
            }
            return UserModel.FINDER.query().where().eq("email", email)
                    .eq("shaPassword", UserModel.getSha512(password)).findOne();
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

}
