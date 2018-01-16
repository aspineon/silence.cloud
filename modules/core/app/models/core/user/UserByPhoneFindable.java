package models.core.user;

/**
 * Find user by phone.
 */
public interface UserByPhoneFindable {


    default UserModel findUserByPhone(String phone){

        try {

            if(phone == null){

                throw new NullPointerException();
            }

            return UserModel.FINDER.query().where().eq("phone", phone).findOne();
        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

}
