package models.core;

import io.ebean.Finder;
import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "status")
public class StatusModel extends BaseModel implements StatusModelInterface {

    @Column(name = "status_name", length = 255, nullable = false, unique = true)
    @Constraints.Required
    @Constraints.MaxLength(255)
    public String name;

    public static Finder<Long, StatusModel> FINDER = new Finder<Long, StatusModel>(StatusModel.class);

    @Override
    public void createStatus(String name) {
        StatusModel statusModel = new StatusModel();
        statusModel.id = System.currentTimeMillis();
        statusModel.name = name;
        statusModel.createdAt = new Date();
        statusModel.updateAt = new Date();
        statusModel.save();
    }

    @Override
    public void updateStatus(Long id, String name) {
        StatusModel statusModel = StatusModel.FINDER.ref(id);
        statusModel.name = name;
        statusModel.updateAt = new Date();
        statusModel.update();
    }

    @Override
    public void deleteStatus(Long id) {
        StatusModel statusModel = StatusModel.FINDER.ref(id);
        statusModel.delete();
    }

    @Override
    public List<StatusModel> findAllStatuses() {
        return StatusModel.FINDER.all();
    }

    @Override
    public StatusModel findStatusById(Long id) {
        try {
            return StatusModel.FINDER.ref(id);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public StatusModel findStatusByName(String name) {
        try {
            return StatusModel.FINDER.query().where().eq("name", name).findOne();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
