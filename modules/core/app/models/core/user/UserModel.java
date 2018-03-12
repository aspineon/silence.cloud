package models.core.user;

import io.ebean.Finder;
import models.core.company.CompanyModel;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

/**
 * User model.
 */
@Table(name = "user")
@Entity
public class UserModel extends BaseModel {

    @Column(nullable = false, unique = true)
    @Constraints.Required
    public UUID uuid;

    @Column(nullable = false, unique = true)
    @Constraints.Required
    public String token;

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

    @Column(name = "is_active")
    @Constraints.Required
    public boolean isActive;

    @Column(length = 64, nullable = false)
    private byte[] shaPassword;

    @OneToOne(mappedBy="user",optional = true, cascade = CascadeType.ALL)
    public TokenModel otpToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    public List<CompanyModel> companies;

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

    public void setUuid(){
        this.uuid = UUID.randomUUID();
    }

    public void setToken(){
        this.token = UUID.randomUUID().toString();
    }

}
