package repositories.core;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.Transaction;
import models.core.ModuleModel;
import models.core.StatusModel;
import play.db.ebean.EbeanConfig;
import play.db.ebean.EbeanDynamicEvolutions;

import javax.inject.Inject;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * repositories.core.ModuleRepository.java
 * <p>
 * All async operations of ModuleModel.java
 */
public class ModuleRepository implements ModuleRepositoryInterface {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;
    private final EbeanDynamicEvolutions ebeanDynamicEvolutions;

    /**
     * Constructor.
     *
     * @param ebeanConfig
     * @param executionContext
     * @param ebeanDynamicEvolutions
     */
    @Inject
    public ModuleRepository(
            EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext,
            EbeanDynamicEvolutions ebeanDynamicEvolutions
    ) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
        this.ebeanDynamicEvolutions = ebeanDynamicEvolutions;
    }

    /**
     * Find all modules
     *
     * @return list of modules
     */
    @Override
    public CompletionStage<List<ModuleModel>> findAllModules() {
        return supplyAsync(() -> {

            List<ModuleModel> moduleModels = new LinkedList<>();
            moduleModels = ebeanServer.find(ModuleModel.class).findList();
            return moduleModels;

        }, executionContext);
    }

    /**
     * Find all modules by status.
     *
     * @param statusId
     * @return all modules by status
     */
    @Override
    public CompletionStage<List<ModuleModel>> findAllModulesByStatus(Long statusId) {
        return supplyAsync(() -> {

            List<ModuleModel> moduleModels = new LinkedList<>();
            StatusModel status = StatusModel.FINDER.ref(statusId);

            if (status != null) {
                moduleModels = ebeanServer.find(ModuleModel.class).where().eq("status", status).findList();
            }

            return moduleModels;

        }, executionContext);
    }

    /**
     * Find module by name.
     *
     * @param moduleId
     * @return module when success or null when failed
     */
    @Override
    public CompletionStage<Optional<ModuleModel>> findModuleById(Long moduleId) {
        return supplyAsync(() -> {

            return Optional.ofNullable(ebeanServer.find(ModuleModel.class).setId(moduleId).findOne());
        }, executionContext);
    }

    /**
     * Find module by name.
     *
     * @param moduleName
     * @return module when success or null when failed
     */
    @Override
    public CompletionStage<Optional<ModuleModel>> findModuleByName(String moduleName) {
        return supplyAsync(() -> {

            return Optional.ofNullable(
                    ebeanServer.find(ModuleModel.class).where().eq("name", moduleName).findOne()
            );
        }, executionContext);
    }

    /**
     * Create module.
     *
     * @param name
     * @param statusId
     * @return module when success or null when failed
     */
    @Override
    public CompletionStage<Optional<ModuleModel>> createModule(String name, Long statusId) {
        return supplyAsync(() -> {

            StatusModel statusModel = ebeanServer.find(StatusModel.class).setId(statusId).findOne();
            ModuleModel existsModuleModel = ebeanServer.find(ModuleModel.class).where().eq("name", name)
                    .findOne();

            if ((statusModel != null) && (existsModuleModel == null)) {

                ModuleModel moduleModel = new ModuleModel();
                moduleModel.name = name;
                moduleModel.status = statusModel;
                moduleModel.createdAt = new Date();
                moduleModel.updateAt = new Date();
                ebeanServer.insert(moduleModel);

                return Optional.ofNullable(
                        ebeanServer.find(ModuleModel.class).where().eq("name", name)
                                .eq("status", statusModel).findOne()
                );
            }

            return Optional.empty();
        }, executionContext);
    }

    /**
     * Update module name.
     *
     * @param moduleId
     * @param moduleName
     * @return module when success or null when failed
     */
    @Override
    public CompletionStage<Optional<ModuleModel>> updateModuleName(Long moduleId, String moduleName) {
        return supplyAsync(() -> {

            Transaction txn = ebeanServer.beginTransaction();
            Optional<ModuleModel> updatedModule = Optional.empty();

            try {

                ModuleModel existsModuleModel = ebeanServer.find(ModuleModel.class).where()
                        .eq("name", moduleName).findOne();
                ModuleModel moduleModel = ebeanServer.find(ModuleModel.class).setId(moduleId).findOne();

                if ((moduleModel != null) && (existsModuleModel == null)) {

                    moduleModel.name = moduleName;
                    moduleModel.updateAt = new Date();
                    ebeanServer.update(moduleModel);

                    txn.commit();

                    updatedModule = Optional.ofNullable(
                            ebeanServer.find(ModuleModel.class).where().eq("id", moduleModel.id)
                                    .eq("name", moduleName).findOne()
                    );
                }
            } finally {

                txn.end();
            }

            return updatedModule;
        }, executionContext);
    }

    /**
     * Update module status.
     *
     * @param moduleId
     * @param statusId
     * @return module when success or null when failed
     */
    @Override
    public CompletionStage<Optional<ModuleModel>> updateModuleStatus(Long moduleId, Long statusId) {
        return supplyAsync(() -> {

            Transaction txn = ebeanServer.beginTransaction();
            Optional<ModuleModel> updatedModule = Optional.empty();

            ModuleModel moduleModel = ebeanServer.find(ModuleModel.class).setId(moduleId).findOne();
            StatusModel statusModel = ebeanServer.find(StatusModel.class).setId(statusId).findOne();

            try {

                if ((statusModel != null) && (moduleModel != null)) {

                    moduleModel.status = statusModel;
                    moduleModel.updateAt = new Date();
                    ebeanServer.update(moduleModel);

                    txn.commit();

                    updatedModule = Optional.ofNullable(
                            ebeanServer.find(ModuleModel.class).where().eq("id", moduleModel.id)
                                    .eq("status", statusModel).findOne()
                    );
                }
            } finally {

                txn.end();
            }

            return updatedModule;
        }, executionContext);
    }

    /**
     * Delete module.
     *
     * @param moduleId
     * @return nuu when success or module when failed
     */
    @Override
    public CompletionStage<Optional<ModuleModel>> deleteModule(Long moduleId) {
        return supplyAsync(() -> {
            Optional<ModuleModel> deletedModel = Optional.empty();
            ModuleModel moduleModel = ebeanServer.find(ModuleModel.class).setId(moduleId).findOne();
            if (moduleModel != null) {
                ebeanServer.delete(moduleModel);
            }else {
                return Optional.of(new ModuleModel());
            }
            if (ebeanServer.find(ModuleModel.class).setId(moduleId).findOne() != null) {
                deletedModel = Optional.of(moduleModel);
            }
            return deletedModel;
        }, executionContext);
    }
}
