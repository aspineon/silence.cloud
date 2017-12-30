package models.core;

import java.util.Date;
import java.util.List;

public interface RoleModelCrud {

    default void createRole(String name, Long statusId){
        RoleModel roleModel = new RoleModel();
        roleModel.name = name;
        roleModel.status = StatusModel.FINDER.ref(statusId);
        roleModel.createdAt = new Date();
        roleModel.updateAt = new Date();
        roleModel.save();
    }

    default void updateRoleName(Long roleId, String name){
        RoleModel roleModel = RoleModel.FINDER.ref(roleId);
        roleModel.name = name;
        roleModel.updateAt = new Date();
        roleModel.update();
    }

    default void updateRoleStatus(Long roleId, Long statusId){
        RoleModel roleModel = RoleModel.FINDER.ref(roleId);
        roleModel.status = StatusModel.FINDER.ref(statusId);
        roleModel.updateAt = new Date();
        roleModel.save();
    }

    default void deleteRole(Long roleId){
        RoleModel roleModel = RoleModel.FINDER.ref(roleId);
        roleModel.delete();
    }

    default List<RoleModel> findAllRoles(){
        List<RoleModel> roles = RoleModel.FINDER.all();
        return roles;
    }

    default List<RoleModel> findAllRolesByStatus(Long statusId){
        StatusModel status = StatusModel.FINDER.ref(statusId);
        List<RoleModel> roles = RoleModel.FINDER.query().where().eq("status", status).findList();
        return roles;
    }

    default RoleModel findRoleById(Long roleId){
        RoleModel roleModel = RoleModel.FINDER.ref(roleId);
        return roleModel;
    }

    default RoleModel findRoleByName(String name){
        RoleModel roleModel = RoleModel.FINDER.query().where().eq("name", name).findOne();
        return roleModel;
    }

}
