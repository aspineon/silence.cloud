package helpers;

import models.core.ModuleModel;
import models.core.StatusModel;

import java.util.Date;
import java.util.List;

public class DefaultModules {

    private Long statusId = 1L;

    private Long moduleId = 1L;

    private String moduleName = "sampleModule";

    public void createModules() {
        createModule(moduleId, moduleName, statusId);
    }

    public void deleteModules() {
        List<ModuleModel> modules = ModuleModel.FINDER.all();
        for (ModuleModel module : modules) {
            module.delete();
        }
    }

    private void createModule(Long moduleId, String name, Long statusId) {
        ModuleModel moduleModel = new ModuleModel();
        moduleModel.id = moduleId;
        moduleModel.name = name;
        moduleModel.status = StatusModel.FINDER.ref(statusId);
        moduleModel.createdAt = new Date();
        moduleModel.updatedAt = new Date();
        moduleModel.save();
    }

}
