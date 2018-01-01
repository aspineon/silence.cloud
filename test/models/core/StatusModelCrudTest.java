package models.core;

import helpers.BeforeAndAfterTest;
import org.junit.*;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StatusModelCrudTest extends BeforeAndAfterTest implements StatusModelCrud {

    private final Long statusId = 1L;
    private final String statusName = "active";

    private final String newStatus = "newStatus";
    private final String updatedStatus = "updatedStatus";

    private int firstExceptedSize = 7;
    private int secondExceptedSize = 8;

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
            assertNotNull(StatusModelCrud.super.createStatus(newStatus));
            assertNotNull(StatusModelCrud.super.findStatusByName(newStatus));
            assertEquals(secondExceptedSize, StatusModelCrud.super.findAllStatuses().size());
            assertNotNull(
                    StatusModelCrud.super.updateStatus(StatusModelCrud.super
                            .findStatusByName(newStatus).id, updatedStatus)
            );
            assertNotNull(StatusModelCrud.super.findStatusByName(updatedStatus));
            assertNotNull(StatusModelCrud.super.deleteStatus(StatusModelCrud.super.findStatusByName(updatedStatus).id));
            assertNull(StatusModelCrud.super.findStatusByName(updatedStatus));
            assertEquals(firstExceptedSize, StatusModelCrud.super.findAllStatuses().size());
        });
    }
}