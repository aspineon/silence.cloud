package models.core;

import org.junit.Test;
import play.test.WithApplication;

import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class StatusModelTest extends WithApplication {

    private String active = "active";
    private String inactive = "inactive";

    private int firstExpectedSize = 1;
    private int secondExpectedSize = 0;

    @Test
    public void statusModelTest() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            StatusModel.createStatus(active);
            StatusModel activeStatus = StatusModel.findByName(active);
            assertNotNull(activeStatus);
            assertNotNull(StatusModel.findById(activeStatus.id));
            assertEquals(StatusModel.findAllStatuses().size(), firstExpectedSize);
            StatusModel.updateStatus(activeStatus.id, inactive);
            assertNotNull(StatusModel.findByName(inactive));
            StatusModel.deleteStatus(activeStatus.id);
            assertNull(StatusModel.findByName(inactive));
            assertEquals(StatusModel.findAllStatuses().size(), secondExpectedSize);
        });
    }

}