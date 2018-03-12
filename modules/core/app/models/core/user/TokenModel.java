package models.core.user;

import play.data.validation.Constraints;

import javax.persistence.*;

@Entity
@Table(name = "token")
public class TokenModel extends BaseModel {

    @Column(nullable = false, unique = true)
    @OneToOne(optional=false)
    @PrimaryKeyJoinColumn
    public UserModel user;

    @Column(nullable = false, unique = true)
    @Constraints.Required
    @Constraints.MaxLength(255)
    public String token;
}
