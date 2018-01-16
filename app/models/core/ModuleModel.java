package models.core;

import io.ebean.Finder;
import models.core.user.BaseModel;
import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * models.core.ModuleModel.java
 * <p>
 * Model of application modules.
 */
@Table(name = "module")
@Entity
public class ModuleModel extends BaseModel {

    @Column(name = "status_id")
    @ManyToOne
    @Constraints.Required
    public StatusModel status;

    @Column(name = "module_name")
    @Constraints.Required
    @Constraints.MaxLength(255)
    public String name;

    public static Finder<Long, ModuleModel> FINDER = new Finder<Long, ModuleModel>(ModuleModel.class);

}
