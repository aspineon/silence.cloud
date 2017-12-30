package models.core;

import com.google.common.collect.ImmutableMap;
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

    private static Long sampleId = 100L;
    private static Long secondSampleId = 101L;

    private static String firstStatusName = "active";
    private static String secondStatusName = "inactive";
    private static String firstNewRole = "firstNewRole";
    private String secondRoleName = "secondNewRole";
    private String updatedRole = "updatedRole";

    private int firstExpectedSize = 1;
    private int secondExpectedSize = 2;

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

            StatusModel statusModel = new StatusModel();
            statusModel.id = secondSampleId;
            statusModel.name = firstStatusName;
            statusModel.createdAt = new Date();
            statusModel.updateAt = new Date();
            statusModel.save();

            StatusModel statusModel2 = new StatusModel();
            statusModel2.id = sampleId;
            statusModel2.name = secondStatusName;
            statusModel2.createdAt = new Date();
            statusModel2.updateAt = new Date();
            statusModel2.save();

            RoleModel roleModel = new RoleModel();
            roleModel.id = sampleId;
            roleModel.name = firstNewRole;
            roleModel.status = StatusModel.FINDER.ref(sampleId);
            roleModel.createdAt = new Date();
            roleModel.updateAt = new Date();
            roleModel.save();

        });
    }

    @AfterClass
    public static void tearDown() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            running(fakeApplication(inMemoryDatabase("test")), () -> {
                List<RoleModel> roles = RoleModel.FINDER.all();
                for(RoleModel role: roles){
                    role.delete();
                }

                List<StatusModel> statuses  = StatusModel.FINDER.all();
                for (StatusModel status: statuses){
                    status.delete();
                }

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
            assertNotNull(RoleModelCrud.super.findRoleById(sampleId));
        });
    }

    @Test
    public void testFindRoleByName() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            assertNotNull(RoleModelCrud.super.findRoleByName(firstNewRole));
        });
    }

    @Test
    public void testFindAllRolesByStatus() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            assertEquals(firstExpectedSize, RoleModelCrud.super.findAllRolesByStatus(sampleId).size());
        });
    }

    @Test
    public void testUserModelCrud() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            RoleModelCrud.super.createRole(secondRoleName, sampleId);
            assertNotNull(RoleModelCrud.super.findRoleByName(secondRoleName));
            assertNotNull(RoleModelCrud.super.findRoleById(RoleModelCrud.super.findRoleByName(secondRoleName).id));
            assertEquals(secondExpectedSize, RoleModelCrud.super.findAllRoles().size());
            RoleModelCrud.super.updateRoleName(sampleId, updatedRole);
            assertNotNull(RoleModelCrud.super.findRoleByName(updatedRole));
            RoleModelCrud.super.updateRoleStatus(sampleId, secondSampleId);
            assertEquals(firstExpectedSize, RoleModelCrud.super.findAllRolesByStatus(secondSampleId).size());
            RoleModelCrud.super.deleteRole(sampleId);
            assertEquals(firstExpectedSize, RoleModelCrud.super.findAllRoles().size());
        });
    }
}