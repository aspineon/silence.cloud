package models.core.user;

import io.ebean.Model;
import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * models.core.BaseModel.java
 * <p>
 * Super class of others models.
 */
@MappedSuperclass
public class BaseModel extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue
    public Long id;

    @Constraints.Required
    @Formats.DateTime(pattern = "yyyy/mm/dd")
    public Date createdAt;

    @Constraints.Required
    @Formats.DateTime(pattern = "yyyy/mm/dd")
    public Date updatedAt;

}
