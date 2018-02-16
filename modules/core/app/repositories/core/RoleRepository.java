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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * repositories.core.RoleRepository.java
 * <p>
 * Implements async crud operation of role model.
 */
public class RoleRepository implements RoleRepositoryInterface {

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
    public RoleRepository(
            EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext,
            EbeanDynamicEvolutions ebeanDynamicEvolutions
    ) {

        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
        this.ebeanDynamicEvolutions = ebeanDynamicEvolutions;
    }

    /**
     * Find all roles.
     *
     * @return all roles list
     */
    @Override
    public CompletionStage<List<RoleModel>> findAllRoles() {

        return supplyAsync(() -> {

            List roles = new LinkedList();
            roles = ebeanServer.find(RoleModel.class).findList();

            return roles;
        }, executionContext);
    }

    /**
     * Find all roles by status.
     *
     * @param statusId
     * @return list of all roles find by status or empty list when status not found
     */
    @Override
    public CompletionStage<List<RoleModel>> findAllRolesByStatus(Long statusId) {

        return supplyAsync(() -> {

            List<RoleModel> roles = new LinkedList<>();
            StatusModel status = ebeanServer.find(StatusModel.class).setId(statusId).findOne();
            if (status != null) {

                roles = ebeanServer.find(RoleModel.class).where().eq("status", status).findList();
            }

            return roles;
        }, executionContext);
    }

    /**
     * Find role by id.
     *
     * @param id
     * @return role when success or null when failed
     */
    @Override
    public CompletionStage<Optional<RoleModel>> findRoleById(Long id) {

        return supplyAsync(() -> {

            return Optional.ofNullable(ebeanServer.find(RoleModel.class).setId(id).findOne());
        }, executionContext);
    }

    /**
     * Find role by name.
     *
     * @param name
     * @return role when success or null when failed
     */
    @Override
    public CompletionStage<Optional<RoleModel>> findRoleByName(String name) {

        return supplyAsync(() -> {

            return Optional.ofNullable(
                    ebeanServer.find(RoleModel.class).where().eq("name", name).findOne()
            );
        }, executionContext);
    }

    /**
     * Create role.
     *
     * @param name
     * @param statusId
     * @return new role when success or null when failed
     */
    @Override
    public CompletionStage<Optional<RoleModel>> createRole(String name, Long statusId) {

        return supplyAsync(() -> {

            StatusModel status = ebeanServer.find(StatusModel.class).setId(statusId).findOne();
            RoleModel existRoleModel = ebeanServer.find(RoleModel.class).where().eq("name", name)
                    .findOne();

            if ((status != null) && (existRoleModel == null)) {

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
            }

            return Optional.empty();
        }, executionContext);
    }

    /**
     * Update role name.
     *
     * @param roleId
     * @param name
     * @return updated role when success or null when failed
     */
    @Override
    public CompletionStage<Optional<RoleModel>> updateRoleName(Long roleId, String name) {

        return supplyAsync(() -> {

            Transaction txn = ebeanServer.beginTransaction();
            Optional<RoleModel> updatedRole = Optional.empty();

            try {

                RoleModel currentRole = ebeanServer.find(RoleModel.class).setId(roleId).findOne();
                RoleModel existsRole = ebeanServer.find(RoleModel.class).where().eq("name", name)
                        .findOne();

                if ((currentRole != null) && (existsRole == null)) {

                    currentRole.name = name;
                    currentRole.updateAt = new Date();
                    ebeanServer.update(currentRole);
                    txn.commit();
                } else {

                    return updatedRole;
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

    /**
     * Update role status.
     *
     * @param roleId
     * @param statusId
     * @return role when success or null when failed
     */
    @Override
    public CompletionStage<Optional<RoleModel>> updateRoleStatus(Long roleId, Long statusId) {

        return supplyAsync(() -> {

            Transaction txn = ebeanServer.beginTransaction();
            Optional<RoleModel> updatedRole = Optional.empty();

            try {

                StatusModel status = ebeanServer.find(StatusModel.class).setId(statusId).findOne();
                RoleModel currentRole = ebeanServer.find(RoleModel.class).setId(roleId).findOne();

                if ((currentRole != null) && (status != null)) {

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

    /**
     * Delete role.
     *
     * @param roleId
     * @return null when success or role when failed
     */
    @Override
    public CompletionStage<Optional<RoleModel>> deleteRole(Long roleId) {

        return supplyAsync(() -> {

            RoleModel role = ebeanServer.find(RoleModel.class).setId(roleId).findOne();
            Optional<RoleModel> roleModelOptional = Optional.empty();

            if(role != null) {

                ebeanServer.delete(role);
            } else {

                return Optional.of(new RoleModel());
            }

            if (ebeanServer.find(RoleModel.class).setId(role.id).findOne() != null) {

                roleModelOptional = Optional.of(role);
                return roleModelOptional;
            }

            return Optional.ofNullable(ebeanServer.find(RoleModel.class).setId(role.id).findOne());
        }, executionContext);
    }
}
