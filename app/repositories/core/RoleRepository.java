package repositories.core;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.Transaction;
import models.core.RoleModel;
import models.core.StatusModel;
import play.db.ebean.EbeanConfig;
import play.db.ebean.EbeanDynamicEvolutions;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class RoleRepository implements RoleRepositoryInterface {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;
    private final EbeanDynamicEvolutions ebeanDynamicEvolutions;

    @Inject
    public RoleRepository(
            EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext,
            EbeanDynamicEvolutions ebeanDynamicEvolutions
    ) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
        this.ebeanDynamicEvolutions = ebeanDynamicEvolutions;
    }

    @Override
    public CompletionStage<List<RoleModel>> findAllRoles() {
        return supplyAsync(() -> {
            return ebeanServer.find(RoleModel.class).findList();
        }, executionContext);
    }

    @Override
    public CompletionStage<List<RoleModel>> findAllRolesByStatus(Long statusId) {
        return supplyAsync(() -> {
            StatusModel status = ebeanServer.find(StatusModel.class).setId(statusId).findOne();
            return ebeanServer.find(RoleModel.class).where().eq("status", status).findList();
        }, executionContext);
    }

    @Override
    public CompletionStage<Optional<RoleModel>> findRoleById(Long id) {
        return supplyAsync(() -> {
            return Optional.ofNullable(ebeanServer.find(RoleModel.class).setId(id).findOne());
        }, executionContext);
    }

    @Override
    public CompletionStage<Optional<RoleModel>> findRoleByName(String name) {
        return supplyAsync(() -> {
            return Optional.ofNullable(
                    ebeanServer.find(RoleModel.class).where().eq("name", name).findOne()
            );
        }, executionContext);
    }

    @Override
    public CompletionStage<Optional<RoleModel>> createRole(String name, Long statusId) {
        return supplyAsync(() -> {
            StatusModel status = ebeanServer.find(StatusModel.class).setId(statusId).findOne();
            RoleModel roleModel = new RoleModel();
            roleModel.id = System.currentTimeMillis();
            roleModel.name = name;
            roleModel.status = status;
            roleModel.createdAt = new Date();
            roleModel.updateAt = new Date();
            roleModel.save();
            return Optional.ofNullable(
                    ebeanServer.find(RoleModel.class).where().eq("name", name)
                            .eq("status", status).findOne()
            );
        }, executionContext);
    }

    @Override
    public CompletionStage<Optional<RoleModel>> updateRoleName(Long roleId, String name) {
        return supplyAsync(() -> {
            Transaction txn = ebeanServer.beginTransaction();
            Optional<RoleModel> updatedRole = Optional.empty();
            try {
                RoleModel currentRole = ebeanServer.find(RoleModel.class).setId(roleId).findOne();
                if (currentRole != null) {
                    currentRole.name = name;
                    currentRole.updateAt = new Date();
                    ebeanServer.update(currentRole);
                    txn.commit();
                }
                updatedRole = Optional.ofNullable(
                        ebeanServer.find(RoleModel.class).where().eq("name", name).findOne()
                );
            }finally {
                txn.end();
            }
            return updatedRole;
        }, executionContext);
    }

    @Override
    public CompletionStage<Optional<RoleModel>> updateRoleStatus(Long roleId, Long statusId) {
        return supplyAsync(() -> {
            Transaction txn = ebeanServer.beginTransaction();
            Optional<RoleModel> updatedRole = Optional.empty();
            try {
                StatusModel status = ebeanServer.find(StatusModel.class).setId(statusId).findOne();
                RoleModel currentRole = ebeanServer.find(RoleModel.class).setId(roleId).findOne();
                if (currentRole != null) {
                    currentRole.status = status;
                    currentRole.updateAt = new Date();
                    ebeanServer.update(currentRole);
                    txn.commit();
                }
                updatedRole = Optional.ofNullable(
                        ebeanServer.find(RoleModel.class).where().eq("status", status).findOne()
                );
            } finally {
                txn.end();
            }
            return updatedRole;
        }, executionContext);
    }

    @Override
    public CompletionStage<Optional<RoleModel>> deleteRole(Long roleId) {
        return supplyAsync(() -> {
            RoleModel role = ebeanServer.find(RoleModel.class).setId(roleId).findOne();
            if(role != null) {
                ebeanServer.delete(role);
            }
            return Optional.ofNullable(ebeanServer.find(RoleModel.class).setId(role.id).findOne());
        }, executionContext);
    }
}
