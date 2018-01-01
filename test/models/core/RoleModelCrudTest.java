package models.core;

import helpers.BeforeAndAfterTest;
import org.junit.*;

import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class RoleModelCrudTest extends BeforeAndAfterTest implements RoleModelCrud {

    private Long firstSampleId = 1L;
    private Long secondSampleId = 2L;

    private String newRoleName = "newRole";
    private String updatedRole = "updatedRole";
    private String sampleRoleName = "office";

    private int firstExpectedSize = 13;
    private int secondExpectedSize = 14;
    private int changeStatusSize = 1;

    @Test
    public void testFindAllRoles() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            assertEquals(firstExpectedSize, RoleModelCrud.super.findAllRoles().size());
        });
    }

    @Test
    public void testFindRoleById() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            assertNotNull(RoleModelCrud.super.findRoleById(firstSampleId));
        });
    }

    @Test
    public void testFindRoleByName() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            assertNotNull(RoleModelCrud.super.findRoleByName(sampleRoleName));
        });
    }

    @Test
    public void testFindAllRolesByStatus() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            assertEquals(firstExpectedSize, RoleModelCrud.super.findAllRolesByStatus(firstSampleId).size());
        });
    }

    @Test
    public void testUserModelCrud() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            // create new role
            assertNotNull(RoleModelCrud.super.createRole(newRoleName, firstSampleId));
            assertNull(RoleModelCrud.super.createRole(newRoleName, firstSampleId));
            assertNull(RoleModelCrud.super.createRole(sampleRoleName, firstSampleId));
            assertNotNull(RoleModelCrud.super.findRoleByName(newRoleName));
            assertNotNull(RoleModelCrud.super.findRoleById(RoleModelCrud.super.findRoleByName(newRoleName).id));
            assertEquals(secondExpectedSize, RoleModelCrud.super.findAllRoles().size());

            // update role name
            assertNull(
                    RoleModelCrud.super.updateRoleName(RoleModelCrud.super.findRoleByName(newRoleName).id, sampleRoleName)
            );
            assertNotNull(
                    RoleModelCrud.super.updateRoleName(RoleModelCrud.super.findRoleByName(newRoleName).id, updatedRole)
            );
            assertNotNull(RoleModelCrud.super.findRoleByName(updatedRole));

            // update status test
            assertNotNull(
                    RoleModelCrud.super.updateRoleStatus(
                            RoleModelCrud.super.findRoleByName(updatedRole).id, secondSampleId
                    )
            );
            assertEquals(changeStatusSize, RoleModelCrud.super.findAllRolesByStatus(secondSampleId).size());

            // delete roles
            RoleModelCrud.super.deleteRole(RoleModelCrud.super.findRoleByName(updatedRole).id);
            assertEquals(firstExpectedSize, RoleModelCrud.super.findAllRoles().size());
        });
    }
}