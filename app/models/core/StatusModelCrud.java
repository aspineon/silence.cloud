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
        StatusModel currentStatus = null;

        try {
            if (existsStatusModel == null) {

                StatusModel statusModel = new StatusModel();
                statusModel.id = System.currentTimeMillis();
                statusModel.name = name;
                statusModel.createdAt = new Date();
                statusModel.updateAt = new Date();
                statusModel.save();


            } else {

                throw new NullPointerException();
            }

            currentStatus = findStatusByName(name);
        } catch (Exception e){

            e.printStackTrace();
        } finally {

            return currentStatus;
        }
    }

    /**
     * Update status name.
     *
     * @param id
     * @param name
     * @return status when success or null when failed
     */
    default StatusModel updateStatus(Long id, String name) {

        StatusModel statusModel = StatusModel.FINDER.byId(id);
        StatusModel existsStatusModel = StatusModel.FINDER.query().where().eq("name", name).findOne();
        StatusModel updatedStatus = null;

        try {
            if ((statusModel != null) && (existsStatusModel == null)) {
                Ebean.beginTransaction();
                statusModel.name = name;
                statusModel.updateAt = new Date();
                statusModel.update();

                Ebean.commitTransaction();

                Ebean.endTransaction();

                updatedStatus = findStatusByName(name);
            } else {

                throw new NullPointerException();
            }

        }  catch ( Exception e){

            e.printStackTrace();
        } finally {

            return updatedStatus;
        }
    }

    /**
     * Delete status.
     *
     * @param id
     * @return deleted status when success or null empty status model when failed
     */
    default StatusModel deleteStatus(Long id) {

        StatusModel statusModel = StatusModel.FINDER.byId(id);

        if (statusModel != null) {

            statusModel.delete();
            return StatusModel.FINDER.query().where().eq("id", id).findOne();
        }

        return new StatusModel();
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
    default StatusModel findStatusById(Long id) {

        try {

            StatusModel statusModel = StatusModel.FINDER.byId(id);

            if(statusModel == null){

                throw new NullPointerException();
            }

            return statusModel;
        } catch (Exception e) {

            e.printStackTrace();
            throw new NullPointerException();
        }
    }

    /**
     * Find status by name.
     *
     * @param name
     * @return status when success or null when failed
     */
    default StatusModel findStatusByName(String name) {

        try {

            StatusModel statusModel = StatusModel.FINDER.query().where().eq("name", name).findOne();

            if(statusModel == null){

                throw new NullPointerException();
            }

            return statusModel;
        } catch (Exception e) {

            e.printStackTrace();
            throw new NullPointerException();
        }
    }

}
