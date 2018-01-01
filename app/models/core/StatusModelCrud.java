package models.core;

import io.ebean.Ebean;

import java.util.Date;
import java.util.List;

/**
 * models.core.StatusModelCrud.java
 * <p>
 * Crud operation of status model.
 */
public interface StatusModelCrud {

    /**
     * Create new status.
     *
     * @param name
     * @return status when success or null when failed
     */
    default StatusModel createStatus(String name) {

        StatusModel existsStatusModel = StatusModel.FINDER.query().where().eq("name", name).findOne();

        if (existsStatusModel == null) {

            StatusModel statusModel = new StatusModel();
            statusModel.id = System.currentTimeMillis();
            statusModel.name = name;
            statusModel.createdAt = new Date();
            statusModel.updateAt = new Date();
            statusModel.save();

            return StatusModel.FINDER.query().where().eq("name", name).findOne();
        }

        return null;
    }

    /**
     * Update status name.
     *
     * @param id
     * @param name
     * @return status when success or null when failed
     */
    default StatusModel updateStatus(Long id, String name) {

        StatusModel statusModel = StatusModel.FINDER.ref(id);
        StatusModel existsStatusModel = StatusModel.FINDER.query().where().eq("name", name).findOne();

        if ((statusModel != null) && (existsStatusModel == null)) {
            Ebean.beginTransaction();
            try {
                statusModel.name = name;
                statusModel.updateAt = new Date();
                statusModel.update();

                Ebean.commitTransaction();
            } finally {
                Ebean.endTransaction();
            }

            return StatusModel.FINDER.query().where().eq("id", id).eq("name", name).findOne();
        }

        return statusModel;
    }

    /**
     * Delete status.
     *
     * @param id
     * @return deleted status when success or null when failed
     */
    default StatusModel deleteStatus(Long id) {

        StatusModel statusModel = StatusModel.FINDER.ref(id);

        if (statusModel != null) {

            statusModel.delete();
            return statusModel;
        }

        return null;
    }

    /**
     * Find all statuses.
     *
     * @return list of statuses
     */
    default List<StatusModel> findAllStatuses(){

        return StatusModel.FINDER.all();
    }

    /**
     * Find status by id.
     *
     * @param id
     * @return status when success or null when failed
     */
    default StatusModel findStatusById(Long id){

        try {

            return StatusModel.FINDER.ref(id);
        } catch (NullPointerException e) {

            return null;
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    /**
     * Find status by name.
     *
     * @param name
     * @return status when success or null when failed
     */
    default StatusModel findStatusByName(String name){

        try {

            return StatusModel.FINDER.query().where().eq("name", name).findOne();
        } catch (NullPointerException e) {

            return null;
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

}
