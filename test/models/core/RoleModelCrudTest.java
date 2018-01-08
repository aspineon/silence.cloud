package models.core;

import helpers.BeforeAndAfterTest;
import org.junit.*;

import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class RoleModelCrudTest extends BeforeAndAfterTest implements RoleModelCrud {

    private Long activeStatusId = 1L;
    private Long inactiveStatusId = 2L;

    private Long officeRoleId = 1L;

    private Long notExistsRoleId = 100L;

    private Long notExistsStatusId = 100L;

    private String newRoleName = "newRole";
    private String updatedRole = "updatedRole";
    private String officeRoleName = "office";

    private String notExistsRoleName = "notExists";

    private int firstExpectedSize = 13;

    @Test
    public void findAllRolesTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertEquals(firstExpectedSize, RoleModelCrud.super.findAllRoles().size());
        });
    }

    @Test
    public void findRoleByExistsId() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(RoleModelCrud.super.findRoleById(activeStatusId));
        });
    }

    @Test
    public void findRoleByNotExistsId() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(RoleModelCrud.super.findRoleById(notExistsStatusId));
        });
    }

    @Test
    public void findRoleByExistsName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(RoleModelCrud.super.findRoleByName(officeRoleName));
        });
    }

    @Test
    public void findAllRolesByStatusTest() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            assertEquals(firstExpectedSize, RoleModelCrud.super.findAllRolesByStatus(activeStatusId).size());
        });
    }

    @Test
    public void findRoleByNotExistsName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(RoleModelCrud.super.findRoleByName(notExistsRoleName));
        });
    }

    @Test
    public void createRoleWithExistsNameTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(RoleModelCrud.super.createRole(officeRoleName, activeStatusId));
        });
    }

    @Test
    public void createRoleWithNotExistsStatus() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(RoleModelCrud.super.createRole(newRoleName, notExistsStatusId));
        });
    }

    @Test
    public void createNewRole() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(RoleModelCrud.super.createRole(newRoleName, activeStatusId));
        });
    }

    @Test
    public void updateNotExistsRole() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(RoleModelCrud.super.updateRoleName(notExistsRoleId, updatedRole));
        });
    }

    @Test
    public void updateRoleWithExistsName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(RoleModelCrud.super.updateRoleName(officeRoleId, officeRoleName));
        });
    }

    @Test
    public void updateRoleByNotExistsName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(RoleModelCrud.super.updateRoleName(officeRoleId, updatedRole));
        });
    }

    @Test
    public void updateNotExistsRoleWithStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(RoleModelCrud.super.updateRoleStatus(notExistsRoleId, activeStatusId));
        });
    }

    @Test
    public void updateRoleWithNotExistsStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(RoleModelCrud.super.updateRoleStatus(officeRoleId, notExistsStatusId));
        });
    }

    @Test
    public void updateRoleStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(RoleModelCrud.super.updateRoleStatus(officeRoleId, inactiveStatusId));
        });
    }

    @Test
    public void deleteRoleWithNotExistsId() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(RoleModelCrud.super.deleteRole(notExistsRoleId));
        });
    }

    @Test
    public void deleteRoleWithExistsId() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(RoleModelCrud.super.deleteRole(officeRoleId));
        });
    }
}