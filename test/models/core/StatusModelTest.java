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

    private static Long statusId = System.currentTimeMillis();

    private static String active = "active";
    private String inactive = "inactive";
    private String frozen = "frozen";

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
            statusModel.name = active;
            statusModel.createdAt = new Date();
            statusModel.updateAt = new Date();
            statusModel.save();
        });

    }

    @Test
    public void findAllStatusesTest() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () ->{
            assertEquals(firstExceptedSize, findAllStatuses().size());
        });
    }

    @Test
    public void findStatusByIdTest() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            assertNotNull(findStatusById(statusId));
        });
    }

    @Test
    public void findStatusByNameTest() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            assertNotNull(findStatusByName(active));
        });
    }

    @Test
    public void statusModelTest() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            createStatus(inactive);
            assertNotNull(findStatusByName(inactive));
            assertEquals(secondExceptedSize, findAllStatuses().size());
            updateStatus(findStatusByName(inactive).id, frozen);
            assertNotNull(findStatusByName(frozen));
            deleteStatus(findStatusByName(frozen).id);
            assertNull(findStatusByName(frozen));
            assertEquals(firstExceptedSize, findAllStatuses().size());
        });
    }

    @AfterClass
    public static void tearDown() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            Evolutions.cleanupEvolutions(database);
            database.shutdown();
        });
    }

    @Override
    public void createStatus(String name) {
        StatusModel statusModel = new StatusModel();
        statusModel.createStatus(name);
    }

    @Override
    public void updateStatus(Long id, String name) {
        StatusModel statusModel = new StatusModel();
        statusModel.updateStatus(id, name);
    }

    @Override
    public void deleteStatus(Long id) {
        StatusModel statusModel = new StatusModel();
        statusModel.deleteStatus(id);
    }

    @Override
    public List<StatusModel> findAllStatuses() {
        StatusModel statusModel = new StatusModel();
        return statusModel.findAllStatuses();
    }

    @Override
    public StatusModel findStatusById(Long id) {
        StatusModel statusModel = new StatusModel();
        return statusModel.findStatusById(id);
    }

    @Override
    public StatusModel findStatusByName(String name) {
        StatusModel statusModel = new StatusModel();
        return statusModel.findStatusByName(name);
    }
}