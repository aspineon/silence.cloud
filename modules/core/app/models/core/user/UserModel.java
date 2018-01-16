package models.core.user;

import io.ebean.Finder;
import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * User model.
 */
@Table(name = "user")
@Entity
public class UserModel extends BaseModel {

    @Column(length = 255, nullable = false, name = "username")
    @Constraints.MaxLength(255)
    @Constraints.Required
    public String username;

    @Column(length = 255, unique = true, nullable = false, name = "email")
    @Constraints.MaxLength(255)
    @Constraints.Required
    @Constraints.Email
    public String email;

    @Column(length = 255, nullable = false, unique = true)
    @Constraints.MaxLength(255)
    @Constraints.Required
    public String phone;

    @Column(name = "is_admin")
    @Constraints.Required
    public boolean isAdmin;

    @Column(length = 64, nullable = false)
    private byte[] shaPassword;

    /**
     * Set models.core.user email.
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    /**
     * Set models.core.user password.
     *
     * @param password
     */
    public void setPassword(String password){
        this.shaPassword = getSha512(password);
    }

    public static byte[] getSha512(String value) {
        try {
            return MessageDigest.getInstance("SHA-512").digest(value.getBytes("UTF-8"));
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Finder<Long, UserModel> FINDER = new Finder<>(UserModel.class);

}
