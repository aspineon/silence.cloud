package models.core;

import io.ebean.Finder;
import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Table(name = "status")
@Entity
public class StatusModel extends BaseModel {

    @Column(name="status_name", length = 255, nullable = false, unique = true)
    @Constraints.Required
    @Constraints.MaxLength(255)
    public String name;

    public static Finder<Long, StatusModel> find = new Finder<Long, StatusModel>(StatusModel.class);

    public static void createStatus(String name){
        StatusModel statusModel = new StatusModel();
        statusModel.name = name;
        statusModel.createdAt = new Date();
        statusModel.updatedAt = new Date();
        statusModel.save();
    }

    public static void updateStatus(Long id, String name){
        StatusModel statusModel = find.byId(id);
        statusModel.name = name;
        statusModel.updatedAt = new Date();
        statusModel.update();
    }

    public static void deleteStatus(Long id){
        StatusModel.find.ref(id).delete();
    }

    public static List<StatusModel> findAllStatuses(){
        return find.all();
    }

    public static StatusModel findById(Long id){
        return find.ref(id);
    }

    public static StatusModel findByName(String name){
        return find.query().where().eq("name", name).findOne();
    }
}
