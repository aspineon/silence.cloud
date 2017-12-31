package models.core;

import com.google.common.collect.ImmutableMap;
import helpers.DefaultStatuses;
import org.junit.*;
import org.junit.runners.MethodSorters;
import play.db.evolutions.Evolutions;
import play.db.Database;
import play.db.Databases;
import play.test.WithApplication;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StatusModelCrudTest extends WithApplication implements StatusModelCrud {

    static Database database;

    private static final DefaultStatuses defaultStatuses = new DefaultStatuses();

    private final Long statusId = 1L;
    private final String statusName = "active";

    private final String newStatus = "newStatus";
    private final String updatedStatus = "updatedStatus";

    private int firstExceptedSize = 7;
    private int secondExceptedSize = 8;

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
        });
    }

    @AfterClass
    public static void tearDown() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            defaultStatuses.deleteDefaultStatuses();

            Evolutions.cleanupEvolutions(database);
            database.shutdown();
        });
    }

    @Test
    public void findAllStatusesTest() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () ->{
            assertEquals(firstExceptedSize, StatusModelCrud.super.findAllStatuses().size());
        });
    }

    @Test
    public void findStatusByIdTest() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            assertNotNull(StatusModelCrud.super.findStatusById(statusId));
        });
    }

    @Test
    public void findStatusByNameTest() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            assertNotNull(StatusModelCrud.super.findStatusByName(statusName));
        });
    }

    @Test
    public void statusModelTest() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            StatusModelCrud.super.createStatus(newStatus);
            assertNotNull(StatusModelCrud.super.findStatusByName(newStatus));
            assertEquals(secondExceptedSize, StatusModelCrud.super.findAllStatuses().size());
            StatusModelCrud.super.updateStatus(
                    StatusModelCrud.super.findStatusByName(newStatus).id, updatedStatus
            );
            assertNotNull(StatusModelCrud.super.findStatusByName(updatedStatus));
            StatusModelCrud.super.deleteStatus(StatusModelCrud.super.findStatusByName(updatedStatus).id);
            assertNull(StatusModelCrud.super.findStatusByName(updatedStatus));
            assertEquals(firstExceptedSize, StatusModelCrud.super.findAllStatuses().size());
        });
    }
}