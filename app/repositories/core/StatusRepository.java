package repositories.core;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.Transaction;
import models.core.StatusModel;
import play.db.ebean.EbeanConfig;
import play.db.ebean.EbeanDynamicEvolutions;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * repositories.core.StatusRepository.java
 * <p>
 * Implements aync non-blocking status model crud operations.
 */
public class StatusRepository implements StatusRepositoryInterface {

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
    public StatusRepository(
            EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext,
            EbeanDynamicEvolutions ebeanDynamicEvolutions
    ) {

        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
        this.ebeanDynamicEvolutions = ebeanDynamicEvolutions;
    }

    /**
     * Create status.
     *
     * @param name
     * @return status when success or null when failed
     */
    @Override
    public CompletionStage<Optional<StatusModel>> createStatusModel(String name) {

        return supplyAsync(() -> {

            StatusModel existsStatusModel = ebeanServer.find(StatusModel.class).where().eq("name", name)
                    .findOne();

            if (existsStatusModel == null) {

                StatusModel statusModel = new StatusModel();
                statusModel.id = System.currentTimeMillis();
                statusModel.name = name;
                statusModel.createdAt = new Date();
                statusModel.updateAt = new Date();
                ebeanServer.insert(statusModel);

                return Optional.ofNullable(ebeanServer.find(StatusModel.class).setId(statusModel.id).findOne());
            }

            return Optional.empty();
        }, executionContext);
    }

    /**
     * @param id
     * @param name
     * @return status when success or null when failed
     */
    @Override
    public CompletionStage<Optional<StatusModel>> updateStatusModel(Long id, String name) {

        return supplyAsync(() -> {

            Transaction txn = ebeanServer.beginTransaction();
            Optional<StatusModel> updatedStatus = Optional.empty();

            try {

                StatusModel existsStatusModel = ebeanServer.find(StatusModel.class).where()
                        .eq("name", name).findOne();
                StatusModel savedStatus = ebeanServer.find(StatusModel.class).setId(id).findOne();

                if ((savedStatus != null) && (existsStatusModel == null)) {

                    savedStatus.name = name;
                    savedStatus.updateAt = new Date();
                    ebeanServer.update(savedStatus);

                    txn.commit();

                    updatedStatus = Optional.ofNullable(
                            ebeanServer.find(StatusModel.class).where().eq("name", name).findOne()
                    );
                }
            } finally {

                txn.end();
            }

            return updatedStatus;
        }, executionContext);
    }

    /**
     * Delete status.
     *
     * @param id
     * @return null when success or status when success
     */
    @Override
    public CompletionStage<Optional<StatusModel>> deleteStatusModel(Long id) {

        return supplyAsync(() -> {

            StatusModel savedStatus = ebeanServer.find(StatusModel.class).setId(id).findOne();

            if(savedStatus != null) {

                ebeanServer.delete(savedStatus);
            }

            return Optional.ofNullable(ebeanServer.find(StatusModel.class).setId(id).findOne());
        }, executionContext);
    }

    /**
     * Find all statuses.
     *
     * @return list of statuses
     */
    @Override
    public CompletionStage<List<StatusModel>> findAllStatuses() {

        return supplyAsync(() -> {

            return ebeanServer.find(StatusModel.class).findList();
        }, executionContext);
    }

    /**
     * Find status by id.
     *
     * @param id
     * @return status when success or null when failed
     */
    @Override
    public CompletionStage<Optional<StatusModel>> findStatusById(Long id) {

        return supplyAsync(() -> {

            return Optional.ofNullable(ebeanServer.find(StatusModel.class).setId(id).findOne());
        }, executionContext);
    }

    /**
     * Find status by name.
     *
     * @param name
     * @return status when success or null when failed
     */
    @Override
    public CompletionStage<Optional<StatusModel>> findStatusByName(String name) {

        return supplyAsync(() -> {

            return Optional.ofNullable(
                    ebeanServer.find(StatusModel.class).where().eq("name", name).findOne()
            );
        }, executionContext);
    }
}
