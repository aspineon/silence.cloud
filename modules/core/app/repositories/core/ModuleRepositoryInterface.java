package repositories.core;

import models.core.ModuleModel;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

/**
 * repositories.core.ModuleRepositoryInterface.java
 * <p>
 * Implements all methods to repositories.core.ModuleRepository.java
 */
public interface ModuleRepositoryInterface {

    /**
     * Find all modules.
     *
     * @return list of all find modules
     */
    CompletionStage<List<ModuleModel>> findAllModules();

    /**
     * Find all modules by status.
     *
     * @param statusId
     * @return list of all modules find by status
     */
    CompletionStage<List<ModuleModel>> findAllModulesByStatus(Long statusId);

    /**
     * Find module by id.
     *
     * @param moduleId
     * @return module when success or null when failed
     */
    CompletionStage<Optional<ModuleModel>> findModuleById(Long moduleId);

    /**
     * Find module by name.
     *
     * @param moduleName
     * @return module when success or null when failed
     */
    CompletionStage<Optional<ModuleModel>> findModuleByName(String moduleName);

    /**
     * Create new module.
     *
     * @param name
     * @param statusId
     * @return module when success or null when failed
     */
    CompletionStage<Optional<ModuleModel>> createModule(String name, Long statusId);

    /**
     * Update module name.
     *
     * @param moduleId
     * @param moduleName
     * @return module when success or null when failed
     */
    CompletionStage<Optional<ModuleModel>> updateModuleName(Long moduleId, String moduleName);

    /**
     * Update module status/
     *
     * @param moduleId
     * @param statusId
     * @return module when success or null when failed
     */
    CompletionStage<Optional<ModuleModel>> updateModuleStatus(Long moduleId, Long statusId);

    /**
     * Delete module.
     *
     * @param moduleId
     * @return delete module when success or null when failed
     */
    CompletionStage<Optional<ModuleModel>> deleteModule(Long moduleId);

}
