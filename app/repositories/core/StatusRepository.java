package repositories.core;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.Model;
import io.ebean.Transaction;
import models.core.StatusModel;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class StatusRepository implements StatusRepositoryInterface {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public StatusRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<List<StatusModel>> getStatusesList() {
        return supplyAsync(() -> {
            return ebeanServer.find(StatusModel.class).findList();
        }, executionContext);
    }

    @Override
    public CompletionStage<Optional<StatusModel>> lookupStatus(Long id) {
        return supplyAsync(() -> {
            return Optional.ofNullable(ebeanServer.find(StatusModel.class).setId(id).findOne());
        }, executionContext);
    }

    @Override
    public CompletionStage<Optional<StatusModel>> getStatusByName(String name) {
        return supplyAsync(() -> {
            return Optional.ofNullable(ebeanServer.find(
                    StatusModel.class).where().eq("name", name).findOne()
            );
        }, executionContext);
    }

    @Override
    public CompletionStage<Optional<StatusModel>> insert(StatusModel statusModel) {
        return supplyAsync(() -> {
            statusModel.id = System.currentTimeMillis();
            ebeanServer.insert(statusModel);
            return Optional.ofNullable(ebeanServer.find(StatusModel.class).setId(statusModel.id).findOne());
        }, executionContext);
    }

    @Override
    public CompletionStage<Optional<StatusModel>> update(Long id, StatusModel newStatusModelData) {
        return supplyAsync(() -> {
            Transaction txn = ebeanServer.beginTransaction();
            Optional<StatusModel> optionalStatus = Optional.empty();
            try {
                StatusModel statusModel = ebeanServer.find(StatusModel.class).setId(id).findOne();
                if(statusModel != null){
                    statusModel.name = newStatusModelData.name;
                    statusModel.updatedAt = newStatusModelData.updatedAt;

                    statusModel.update();
                    txn.commit();
                    optionalStatus = Optional.ofNullable(
                            ebeanServer.find(StatusModel.class).where().eq("name", newStatusModelData.name)
                                    .findOne()
                    );
                }
            } finally {
                txn.end();
            }
            return optionalStatus;
        }, executionContext);
    }

    @Override
    public CompletionStage<Optional<StatusModel>> delete(Long id) {
        return supplyAsync(() -> {
            try {
                final Optional<StatusModel> optionalStatusModel = Optional.ofNullable(
                        ebeanServer.find(StatusModel.class).setId(id).findOne()
                );
                optionalStatusModel.ifPresent(Model::delete);
                return optionalStatusModel;
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }, executionContext);
    }
}
