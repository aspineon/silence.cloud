package models.core;

import io.ebean.Ebean;

import java.util.Date;
import java.util.List;

/**
 * models.core.RoleModelCrud.java
 * <p>
 * Crud operations of RoleModel.
 */
public interface RoleModelCrud {

    /**
     * Find all roles.
     *
     * @return list of all roles
     */
    default List<RoleModel> findAllRoles(){

        List<RoleModel> roles = RoleModel.FINDER.all();
        return roles;
    }

    /**
     * Find all roles by status.
     *
     * @param statusId
     * @return list of all roles find by status
     */
    default List<RoleModel> findAllRolesByStatus(Long statusId){

        StatusModel status = StatusModel.FINDER.ref(statusId);

        List<RoleModel> roles = RoleModel.FINDER.query().where().eq("status", status).findList();
        return roles;
    }

    /**
     * Find role by id.
     *
     * @param roleId
     * @return role when success or null when failed
     */
    default RoleModel findRoleById(Long roleId) {

        try {

            return RoleModel.FINDER.byId(roleId);
        } catch (NullPointerException e) {

            return null;
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    /**
     * Find role by name.
     *
     * @param name
     * @return role when success or null when failed
     */
    default RoleModel findRoleByName(String name) {

        try {

            RoleModel roleModel = RoleModel.FINDER.query().where().eq("name", name).findOne();

            if(roleModel.id != null){

                return roleModel;
            } else {

                throw new NullPointerException();
            }
        } catch (NullPointerException e) {

            return null;
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    /**
     * Create new role.
     *
     * @param name
     * @param statusId
     * @return new role model when success and null when failed
     */
    default RoleModel createRole(String name, Long statusId) {

        RoleModel existsRoleModel = RoleModel.FINDER.query().where().eq("name", name).findOne();
        StatusModel statusModel = StatusModel.FINDER.byId(statusId);

        if ((statusModel != null) && (existsRoleModel == null)) {

            RoleModel roleModel = new RoleModel();
            roleModel.name = name;
            roleModel.status = statusModel;
            roleModel.createdAt = new Date();
            roleModel.updateAt = new Date();
            roleModel.save();

            return RoleModel.FINDER.query().where().eq("name", name)
                    .eq("status", statusModel).findOne();
        }

        return null;
    }

    /**
     * Update role name.
     *
     * @param roleId
     * @param name
     * @return role when success or null when failed
     */
    default RoleModel updateRoleName(Long roleId, String name) {

        RoleModel roleModel = RoleModel.FINDER.byId(roleId);
        RoleModel existsRoleModel = RoleModel.FINDER.query().where().eq("name", name).findOne();

        if ((roleModel != null) && (existsRoleModel == null)) {

            Ebean.beginTransaction();

            try {

                roleModel.name = name;
                roleModel.updateAt = new Date();
                roleModel.update();

                Ebean.commitTransaction();
            } finally {

                Ebean.endTransaction();
            }

            return RoleModel.FINDER.query().where().eq("id", roleId).eq("name", name)
                    .findOne();
        }

        return null;
    }

    /**
     * Update role status.
     *
     * @param roleId
     * @param statusId
     * @return role when success or null when failed
     */
    default RoleModel updateRoleStatus(Long roleId, Long statusId) {

        RoleModel roleModel = RoleModel.FINDER.byId(roleId);
        StatusModel statusModel = StatusModel.FINDER.byId(statusId);

        if ((roleModel != null) && (statusModel != null)) {

            Ebean.beginTransaction();
            try {

                roleModel.status = statusModel;
                roleModel.updateAt = new Date();
                roleModel.update();

                Ebean.commitTransaction();
            } finally {

                Ebean.endTransaction();
            }

            return RoleModel.FINDER.query().where().eq("id", roleId).eq("status", statusModel)
                    .findOne();
        }

        return null;
    }

    /**
     * Delete role.
     *
     * @param roleId
     * @return role when success or null when failed
     */
    default RoleModel deleteRole(Long roleId) {

        RoleModel roleModel = RoleModel.FINDER.byId(roleId);

        if (roleModel != null) {

            roleModel.delete();

            return RoleModel.FINDER.byId(roleId);
        } else {

            return new RoleModel();
        }
    }

}
