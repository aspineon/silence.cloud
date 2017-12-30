package models.core;

import com.google.common.collect.ImmutableMap;
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

    private final static Long statusId = System.currentTimeMillis();

    private final static String firstNewStatus = "newStatus";
    private final String  secondNewStatus = "secondNewStatus";
    private final String updatedStatus = "updatedStatus";

    private int firstExceptedSize = 1;
    private int secondExceptedSize = 2;

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
            statusModel.id = statusId;
            statusModel.name = firstNewStatus;
            statusModel.createdAt = new Date();
            statusModel.updateAt = new Date();
            statusModel.save();
        });
    }

    @AfterClass
    public static void tearDown() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            List<StatusModel> statuses  = StatusModel.FINDER.all();
            for (StatusModel status: statuses){
                status.delete();
            }

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
            assertNotNull(StatusModelCrud.super.findStatusByName(firstNewStatus));
        });
    }

    @Test
    public void statusModelTest() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            StatusModelCrud.super.createStatus(secondNewStatus);
            assertNotNull(StatusModelCrud.super.findStatusByName(secondNewStatus));
            assertEquals(secondExceptedSize, StatusModelCrud.super.findAllStatuses().size());
            StatusModelCrud.super.updateStatus(
                    StatusModelCrud.super.findStatusByName(secondNewStatus).id, updatedStatus
            );
            assertNotNull(StatusModelCrud.super.findStatusByName(updatedStatus));
            StatusModelCrud.super.deleteStatus(StatusModelCrud.super.findStatusByName(updatedStatus).id);
            assertNull(StatusModelCrud.super.findStatusByName(updatedStatus));
            assertEquals(firstExceptedSize, StatusModelCrud.super.findAllStatuses().size());
        });
    }
}