package models.core;

import com.google.common.collect.ImmutableMap;
import helpers.DefaultRoles;
import helpers.DefaultStatuses;
import org.junit.*;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.Evolutions;
import play.test.WithApplication;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class RoleModelCrudTest extends WithApplication implements RoleModelCrud {

    static Database database;

    private static DefaultStatuses defaultStatuses = new DefaultStatuses();
    private static DefaultRoles defaultRoles = new DefaultRoles();

    private Long firstSampleId = 1L;
    private Long secondSampleId = 2L;

    private String newRoleName = "newRole";
    private String updatedRole = "updatedRole";
    private String sampleRoleName = "office";

    private int firstExpectedSize = 13;
    private int secondExpectedSize = 14;
    private int changeStatusSize = 1;

    @BeforeClass
    public static void setUp() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            database = Databases.inMemory(
                    "mydatabase",
                    ImmutableMap.of(
                            "MODE", "MYSQL"
                    ),
                    ImmutableMap.of(
                            "logStatements", true
                    )
            );
            Evolutions.applyEvolutions(database);

            defaultStatuses.createDefaultStatuses();
            defaultRoles.createRoles();

        });
    }

    @AfterClass
    public static void tearDown() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            running(fakeApplication(inMemoryDatabase("test")), () -> {
                defaultRoles.deleteRoles();
                defaultStatuses.deleteDefaultStatuses();

                Evolutions.cleanupEvolutions(database);
                database.shutdown();
            });
        });
    }

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
            RoleModelCrud.super.createRole(newRoleName, firstSampleId);
            assertNotNull(RoleModelCrud.super.findRoleByName(newRoleName));
            assertNotNull(RoleModelCrud.super.findRoleById(RoleModelCrud.super.findRoleByName(newRoleName).id));
            assertEquals(secondExpectedSize, RoleModelCrud.super.findAllRoles().size());
            RoleModelCrud.super.updateRoleName(RoleModelCrud.super.findRoleByName(newRoleName).id, updatedRole);
            assertNotNull(RoleModelCrud.super.findRoleByName(updatedRole));
            RoleModelCrud.super.updateRoleStatus(RoleModelCrud.super.findRoleByName(updatedRole).id, secondSampleId);
            assertEquals(changeStatusSize, RoleModelCrud.super.findAllRolesByStatus(secondSampleId).size());
            RoleModelCrud.super.deleteRole(RoleModelCrud.super.findRoleByName(updatedRole).id);
            assertEquals(firstExpectedSize, RoleModelCrud.super.findAllRoles().size());
        });
    }
}