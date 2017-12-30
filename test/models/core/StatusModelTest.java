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
public class StatusModelTest extends WithApplication implements StatusModelInterface {

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
            Evolutions.cleanupEvolutions(database);
            database.shutdown();
        });
    }

    @Test
    public void findAllStatusesTest() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () ->{
            assertEquals(firstExceptedSize, StatusModelInterface.super.findAllStatuses().size());
        });
    }

    @Test
    public void findStatusByIdTest() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            assertNotNull(StatusModelInterface.super.findStatusById(statusId));
        });
    }

    @Test
    public void findStatusByNameTest() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            assertNotNull(StatusModelInterface.super.findStatusByName(firstNewStatus));
        });
    }

    @Test
    public void statusModelTest() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            StatusModelInterface.super.createStatus(secondNewStatus);
            assertNotNull(StatusModelInterface.super.findStatusByName(secondNewStatus));
            assertEquals(secondExceptedSize, StatusModelInterface.super.findAllStatuses().size());
            StatusModelInterface.super.updateStatus(
                    StatusModelInterface.super.findStatusByName(secondNewStatus).id, updatedStatus
            );
            assertNotNull(StatusModelInterface.super.findStatusByName(updatedStatus));
            StatusModelInterface.super.deleteStatus(StatusModelInterface.super.findStatusByName(updatedStatus).id);
            assertNull(StatusModelInterface.super.findStatusByName(updatedStatus));
            assertEquals(firstExceptedSize, StatusModelInterface.super.findAllStatuses().size());
        });
    }
}