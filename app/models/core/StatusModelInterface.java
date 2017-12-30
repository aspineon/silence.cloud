package models.core;

import io.ebean.Ebean;

import java.util.Date;
import java.util.List;

public interface StatusModelInterface {

    default void createStatus(String name){
        StatusModel statusModel = new StatusModel();
        statusModel.id = System.currentTimeMillis();
        statusModel.name = name;
        statusModel.createdAt = new Date();
        statusModel.updateAt = new Date();
        statusModel.save();
    }

    default void updateStatus(Long id, String name){
        Ebean.beginTransaction();
        try {
            StatusModel statusModel = StatusModel.FINDER.ref(id);
            statusModel.name = name;
            statusModel.updateAt = new Date();
            statusModel.update();

            Ebean.commitTransaction();
        }finally {
            Ebean.endTransaction();
        }
    }

    default void deleteStatus(Long id){
        StatusModel statusModel = StatusModel.FINDER.ref(id);
        statusModel.delete();
    }

    default List<StatusModel> findAllStatuses(){
        return StatusModel.FINDER.all();
    }

    default StatusModel findStatusById(Long id){
        try {
            return StatusModel.FINDER.ref(id);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    default StatusModel findStatusByName(String name){
        try {
            return StatusModel.FINDER.query().where().eq("name", name).findOne();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
