package models.core;

import io.ebean.Finder;
import models.core.user.BaseModel;
import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * models.core.RoleModel.java
 * <p>
 * Model of application roles.
 */
@Table(name = "role")
@Entity
public class RoleModel extends BaseModel {

    @ManyToOne
    @Column(name = "status_id")
    @Constraints.Required
    public StatusModel status;

    @Column(name = "role_name", length = 255, nullable = false, unique = true)
    @Constraints.Required
    @Constraints.MaxLength(255)
    public String name;

    public static Finder<Long, RoleModel> FINDER = new Finder<Long, RoleModel>(RoleModel.class);

}
