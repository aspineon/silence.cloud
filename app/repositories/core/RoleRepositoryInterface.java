package repositories.core;

import models.core.RoleModel;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

/**
 * repositories.core.RoleRepositoryInterface.java
 * <p>
 * Abstract methods of RoleRepository.
 */
public interface RoleRepositoryInterface {

    /**
     * Find all roles.
     *
     * @return all roles list
     */
    CompletionStage<List<RoleModel>> findAllRoles();

    /**
     * Find all roles by status.
     *
     * @param statusId
     * @return list of all roles find by status
     */
    CompletionStage<List<RoleModel>> findAllRolesByStatus(Long statusId);

    /**
     * Find role by id.
     *
     * @param id
     * @return role when success or null when failed
     */
    CompletionStage<Optional<RoleModel>> findRoleById(Long id);

    /**
     * Find role by name.
     *
     * @param name
     * @return role when success or null when failed
     */
    CompletionStage<Optional<RoleModel>> findRoleByName(String name);

    /**
     * Create new role.
     *
     * @param name
     * @param statusId
     * @return role when success or null when failed
     */
    CompletionStage<Optional<RoleModel>> createRole(String name, Long statusId);

    /**
     * Update role name.
     *
     * @param roleId
     * @param name
     * @return role when success or null when failed
     */
    CompletionStage<Optional<RoleModel>> updateRoleName(Long roleId, String name);

    /**
     * Update role status.
     *
     * @param roleId
     * @param statusId
     * @return role when success or null when failed
     */
    CompletionStage<Optional<RoleModel>> updateRoleStatus(Long roleId, Long statusId);

    /**
     * Delete role by id.
     *
     * @param roleId
     * @return delete role when success or null when failed
     */
    CompletionStage<Optional<RoleModel>> deleteRole(Long roleId);

}
