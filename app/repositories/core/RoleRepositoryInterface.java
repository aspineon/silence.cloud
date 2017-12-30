package repositories.core;

import models.core.RoleModel;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface RoleRepositoryInterface {

    CompletionStage<List<RoleModel>> findAllRoles();

    CompletionStage<List<RoleModel>> findAllRolesByStatus(Long statusId);

    CompletionStage<Optional<RoleModel>> findRoleById(Long id);

    CompletionStage<Optional<RoleModel>> findRoleByName(String name);

    CompletionStage<Optional<RoleModel>> createRole(String name, Long statusId);

    CompletionStage<Optional<RoleModel>> updateRoleName(Long roleId, String name);

    CompletionStage<Optional<RoleModel>> updateRoleStatus(Long roleId, Long statusId);

    CompletionStage<Optional<RoleModel>> deleteRole(Long roleId);

}
