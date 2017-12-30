package models.core;

import io.ebean.Finder;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "status")
public class StatusModel extends BaseModel {

    @Column(name = "status_name", length = 255, nullable = false, unique = true)
    @Constraints.Required
    @Constraints.MaxLength(255)
    public String name;

    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
    public List<RoleModel> roles = new LinkedList<>();

    public static Finder<Long, StatusModel> FINDER = new Finder<Long, StatusModel>(StatusModel.class);
}
