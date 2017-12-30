package models.core;

import io.ebean.Finder;
import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "status")
public class StatusModel extends BaseModel {

    @Column(name = "status_name", length = 255, nullable = false, unique = true)
    @Constraints.Required
    @Constraints.MaxLength(255)
    public String name;

    public static Finder<Long, StatusModel> FINDER = new Finder<Long, StatusModel>(StatusModel.class);
}
