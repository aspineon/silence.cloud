package helpers;

import models.core.RoleModel;
import models.core.StatusModel;

import java.util.Date;
import java.util.List;

public class DefaultRoles {

    private Long activeStatusId = 1L;

    private Long officeRoleId = 1L;
    private Long partnersRoleId = 2L;
    private Long counselRoleId = 3L;
    private Long practiseRoleId = 4L;
    private Long juniorAssociateRoleId = 5L;
    private Long associateRoleId = 6L;
    private Long seniorAssociateRoleId = 7L;
    private Long auditorsRoleId = 8L;
    private Long marketingRoleId = 9L;
    private Long salesRoleId = 10L;
    private Long technicalRoleId = 11L;
    private Long itRoleId = 12L;
    private Long hrRoleId = 13L;

    private String officeRoleName = "office";
    private String partnersRoleName = "partners";
    private String counselRoleName = "counsel";
    private String practiseRoleName = "practise";
    private String juniorAssociateRoleName = "junior_associate";
    private String associateRoleName = "associate";
    private String seniorAssociateRoleName = "seniorAssociate";
    private String auditorsRoleName = "auditors";
    private String marketingRoleName = "marketing";
    private String salesRoleName = "sales";
    private String technicalRoleName = "technical";
    private String itRoleName = "it";
    private String hrRoleName = "hr";

    public void createRoles() {
        createRole(officeRoleId, officeRoleName, activeStatusId);
        createRole(partnersRoleId, partnersRoleName, activeStatusId);
        createRole(counselRoleId, counselRoleName, activeStatusId);
        createRole(practiseRoleId, practiseRoleName, activeStatusId);
        createRole(juniorAssociateRoleId, juniorAssociateRoleName, activeStatusId);
        createRole(associateRoleId, associateRoleName, activeStatusId);
        createRole(seniorAssociateRoleId, seniorAssociateRoleName, activeStatusId);
        createRole(auditorsRoleId, auditorsRoleName, activeStatusId);
        createRole(marketingRoleId, marketingRoleName, activeStatusId);
        createRole(salesRoleId, salesRoleName, activeStatusId);
        createRole(technicalRoleId, technicalRoleName, activeStatusId);
        createRole(itRoleId, itRoleName, activeStatusId);
        createRole(hrRoleId, hrRoleName, activeStatusId);
    }

    public void deleteRoles() {
        List<RoleModel> roles = RoleModel.FINDER.all();
        for (RoleModel role : roles) {
            role.delete();
        }
    }

    private void createRole(Long roleId, String roleName, Long statusId) {
        RoleModel roleModel = new RoleModel();
        roleModel.id = roleId;
        roleModel.name = roleName;
        roleModel.status = StatusModel.FINDER.ref(statusId);
        roleModel.createdAt = new Date();
        roleModel.updatedAt = new Date();
        roleModel.save();
    }
}
