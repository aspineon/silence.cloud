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

    private final Long notExistsStatusId = 99L;

    private final String newStatus = "newStatus";
    private final String updatedStatus = "updatedStatus";

    private String notExistsStatusName = "notExists";

    private int firstExceptedSize = 7;

    @Test
    public void findAllStatusesTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () ->{

            assertEquals(firstExceptedSize, StatusModelCrud.super.findAllStatuses().size());
        });
    }

    @Test(expected=NullPointerException.class)
    public void findStatusByNotExistsIdTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(StatusModelCrud.super.findStatusById(notExistsStatusId));
        });
    }

    @Test
    public void findStatusByExistsIdTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            try {
                assertNotNull(StatusModelCrud.super.findStatusById(statusId));

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test(expected=NullPointerException.class)
    public void findStatusByNotExistsNameTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(StatusModelCrud.super.findStatusByName(notExistsStatusName));
        });
    }

    @Test
    public void findStatusByExistsNameTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            try {

                assertNotNull(StatusModelCrud.super.findStatusByName(statusName));
            } catch (Exception e) {

                e.printStackTrace();
                assertNotNull(null);
            }
        });
    }

    @Test
    public void deleteStatusByNotExistsIdTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(StatusModelCrud.super.deleteStatus(notExistsStatusId));
        });
    }

    @Test
    public void deleteStatusByExistsIdTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(StatusModelCrud.super.deleteStatus(statusId));
        });
    }

    @Test
    public void createStatusByExistsNameTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(StatusModelCrud.super.createStatus(statusName));
        });
    }

    @Test
    public void createStatusByNotExistsNameTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(StatusModelCrud.super.createStatus(newStatus));
        });
    }

    @Test
    public void updateStatusWithNotExistsIdTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            StatusModelCrud.super.createStatus(newStatus);
            assertNull(StatusModelCrud.super.updateStatus(notExistsStatusId, updatedStatus));
        });
    }

    @Test
    public void updateStatusWithExistsNameTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            StatusModelCrud.super.createStatus(newStatus);
            assertNull(StatusModelCrud.super.updateStatus(statusId, newStatus));
        });
    }

    @Test
    public void updateStatusTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            StatusModelCrud.super.createStatus(newStatus);
            assertNotNull(StatusModelCrud.super.updateStatus(statusId, updatedStatus));
        });
    }
}