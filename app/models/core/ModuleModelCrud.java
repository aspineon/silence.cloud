package models.core;

import io.ebean.Ebean;

import java.util.Date;
import java.util.List;

/**
 * models.core.ModuleModelCrud.java
 * <p>
 * Crud operation with module model.
 */
public interface ModuleModelCrud {

    /**
     * Create new module.
     *
     * @param name
     * @param statusId
     * @return new module when success or null when failed.
     */
    default ModuleModel createModule(String name, Long statusId) {

        StatusModel statusModel = StatusModel.FINDER.ref(statusId);
        ModuleModel existsModuleModel = ModuleModel.FINDER.query().where().eq("name", name).findOne();

        if ((statusModel != null) && (existsModuleModel == null)) {

            ModuleModel moduleModel = new ModuleModel();
            moduleModel.id = System.currentTimeMillis();
            moduleModel.name = name;
            moduleModel.status = StatusModel.FINDER.ref(statusId);
            moduleModel.createdAt = new Date();
            moduleModel.updateAt = new Date();
            moduleModel.save();

            return ModuleModel.FINDER.query().where().eq("name", name)
                    .eq("status", statusModel).findOne();
        }

        return null;
    }

    /**
     * Update exists module name.
     *
     * @param moduleId
     * @param name
     * @return updated module when success or null when failed
     */
    default ModuleModel updateModuleName(Long moduleId, String name) {

        ModuleModel moduleModel = ModuleModel.FINDER.ref(moduleId);
        ModuleModel existsModuleModel = ModuleModel.FINDER.query().where().eq("name", name).findOne();

        if ((moduleModel != null) && (existsModuleModel == null)) {

            Ebean.beginTransaction();

            try {
                moduleModel.name = name;
                moduleModel.updateAt = new Date();
                moduleModel.update();

                Ebean.commitTransaction();
            } finally {

                Ebean.endTransaction();
            }

            return ModuleModel.FINDER.query().where().eq("id", moduleId).eq("name", name)
                    .findOne();
        }

        return null;
    }

    /**
     * Update exits module status.
     *
     * @param moduleId
     * @param statusId
     * @return updated module when success or null when failed
     */
    default ModuleModel updateModuleStatus(Long moduleId, Long statusId) {

        ModuleModel moduleModel = ModuleModel.FINDER.ref(moduleId);
        StatusModel statusModel = StatusModel.FINDER.ref(statusId);

        if ((moduleModel != null) && (statusModel != null)) {

            Ebean.beginTransaction();

            try {
                moduleModel.status = statusModel;
                moduleModel.updateAt = new Date();
                moduleModel.update();

                Ebean.commitTransaction();
            } finally {

                Ebean.endTransaction();
            }

            return ModuleModel.FINDER.query().where().eq("id", moduleId)
                    .eq("status", statusModel).findOne();
        }

        return null;
    }

    /**
     * Delete module
     *
     * @param moduleId
     * @return null when success or exists module when failed
     */
    default ModuleModel deleteModule(Long moduleId) {

        ModuleModel moduleModel = ModuleModel.FINDER.ref(moduleId);
        if (moduleModel != null) {

            moduleModel.delete();
        }

        ModuleModel deleteModuleModel = ModuleModel.FINDER.query().where().eq("id", moduleId).findOne();

        if (deleteModuleModel == null) {

            return null;

        }

        return moduleModel;
    }

    /**
     * Find all modules.
     *
     * @return List of modules
     */
    default List<ModuleModel> findAllModules() {

        return ModuleModel.FINDER.all();

    }

    /**
     * Find all modules by status.
     *
     * @param statusId
     * @return list of modules
     */
    default List<ModuleModel> findAllModulesByStatus(Long statusId) {

        StatusModel status = StatusModel.FINDER.ref(statusId);
        return ModuleModel.FINDER.query().where().eq("status", status).findList();

    }

    /**
     * Find module by id.
     *
     * @param id
     * @return module when success or null when failed
     */
    default ModuleModel findModuleById(Long id) {

        try {

            return ModuleModel.FINDER.ref(id);
        } catch (NullPointerException e) {

            return null;
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }

    }

    /**
     * Find module by name.
     *
     * @param name
     * @return module when succes or null when failed
     */
    default ModuleModel findModuleByName(String name) {

        try {

            return ModuleModel.FINDER.query().where().eq("name", name).findOne();
        } catch (NullPointerException e) {

            return null;
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

}
