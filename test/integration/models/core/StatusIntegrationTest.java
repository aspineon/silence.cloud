package integration.models.core;

import helpers.BeforeAndAfterTest;
import models.core.StatusModelCrud;
import org.junit.Test;

import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class StatusIntegrationTest extends BeforeAndAfterTest implements StatusModelCrud {
    
    private Long activeStatusId = 1L;
    private Long inactiveStatusId = 2L;
    private Long frozenStatusId = 3L;
    private Long bannedStatusId = 4L;
    private Long blockedStatusId = 5L;
    private Long onlineStatusId = 6L;
    private Long offlineStatusId = 7L;

    private String activeStatusName = "active";
    private String inactiveStatusName = "inactive";
    private String frozenStatusName = "frozen";
    private String bannedStatusName = "banned";
    private String blockedStatusName = "blocked";
    private String onlineStatusName = "online";
    private String offlineStatusName = "offline";

    @Test
    public void checkActiveStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(StatusModelCrud.super.findStatusById(activeStatusId));
            assertNotNull(StatusModelCrud.super.findStatusByName(activeStatusName));
        });
    }

    @Test
    public void checkInactiveStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(StatusModelCrud.super.findStatusById(inactiveStatusId));
            assertNotNull(StatusModelCrud.super.findStatusByName(inactiveStatusName));
        });
    }

    @Test
    public void checkFrozenStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(StatusModelCrud.super.findStatusById(frozenStatusId));
            assertNotNull(StatusModelCrud.super.findStatusByName(frozenStatusName));
        });
    }

    @Test
    public void checkBlockedStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(StatusModelCrud.super.findStatusById(blockedStatusId));
            assertNotNull(StatusModelCrud.super.findStatusByName(blockedStatusName));
        });
    }

    @Test
    public void checkBannedStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(StatusModelCrud.super.findStatusById(bannedStatusId));
            assertNotNull(StatusModelCrud.super.findStatusByName(bannedStatusName));
        });
    }

    @Test
    public void checkOnlineStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(StatusModelCrud.super.findStatusById(onlineStatusId));
            assertNotNull(StatusModelCrud.super.findStatusByName(onlineStatusName));
        });
    }

    @Test
    public void checkOfflineStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(StatusModelCrud.super.findStatusById(offlineStatusId));
            assertNotNull(StatusModelCrud.super.findStatusByName(offlineStatusName));
        });
    }
}
