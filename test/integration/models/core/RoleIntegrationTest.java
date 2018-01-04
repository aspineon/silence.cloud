package integration.models.core;

import helpers.BeforeAndAfterTest;
import models.core.RoleModelCrud;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class RoleIntegrationTest extends BeforeAndAfterTest implements RoleModelCrud {

    private Long officeRoleId = 1L;
    private Long partnersRoleId = 2L;
    private Long counselRoleId = 3L;
    private Long practiseRoleId = 4L;
    private Long juniorAssociateRoleId = 5L;
    private Long associateRoleId = 6L;
    private Long seniorAssociateRoleId = 7L;
    private Long auditorsRoleId = 8L;
    private Long marketingRoleId = 9L;
    private Long salesRoleId = 10L;
    private Long technicalRoleId = 11L;
    private Long itRoleId = 12L;
    private Long hrRoleId = 13L;

    private String officeRoleName = "office";
    private String partnersRoleName = "partners";
    private String counselRoleName = "counsel";
    private String practiseRoleName = "practise";
    private String associateRoleName = "associate";
    private String auditorsRoleName = "auditors";
    private String marketingRoleName = "marketing";
    private String salesRoleName = "sales";
    private String technicalRoleName = "technical";
    private String itRoleName = "it";
    private String hrRoleName = "hr";

    @Test
    public void officeRoleTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(RoleModelCrud.super.findRoleById(officeRoleId));
            assertNotNull(RoleModelCrud.super.findRoleByName(officeRoleName));
        });
    }

    @Test
    public void partnersRoleTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(RoleModelCrud.super.findRoleById(partnersRoleId));
            assertNotNull(RoleModelCrud.super.findRoleByName(partnersRoleName));
        });
    }

    @Test
    public void counselRoleTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(RoleModelCrud.super.findRoleById(counselRoleId));
            assertNotNull(RoleModelCrud.super.findRoleByName(counselRoleName));
        });
    }

    @Test
    public void practiseRoleTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(RoleModelCrud.super.findRoleById(practiseRoleId));
            assertNotNull(RoleModelCrud.super.findRoleByName(practiseRoleName));
        });
    }

    @Test
    public void juniorAssociateRoleTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(RoleModelCrud.super.findRoleById(juniorAssociateRoleId));
        });
    }

    @Test
    public void associateRoleTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(RoleModelCrud.super.findRoleById(associateRoleId));
            assertNotNull(RoleModelCrud.super.findRoleByName(associateRoleName));
        });
    }

    @Test
    public void seniorAssociateRoleTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(RoleModelCrud.super.findRoleById(seniorAssociateRoleId));
        });
    }

    @Test
    public void auditorsRoleTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(RoleModelCrud.super.findRoleById(auditorsRoleId));
            assertNotNull(RoleModelCrud.super.findRoleByName(auditorsRoleName));
        });
    }

    @Test
    public void marketingRoleTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(RoleModelCrud.super.findRoleById(marketingRoleId));
            assertNotNull(RoleModelCrud.super.findRoleByName(marketingRoleName));
        });
    }

    @Test
    public void salesRoleTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(RoleModelCrud.super.findRoleById(salesRoleId));
            assertNotNull(RoleModelCrud.super.findRoleByName(salesRoleName));
        });
    }

    @Test
    public void technicalRoleTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(RoleModelCrud.super.findRoleById(technicalRoleId));
            assertNotNull(RoleModelCrud.super.findRoleByName(technicalRoleName));
        });
    }

    @Test
    public void itRoleTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(RoleModelCrud.super.findRoleById(itRoleId));
            assertNotNull(RoleModelCrud.super.findRoleByName(itRoleName));
        });
    }

    @Test
    public void hrRoleTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(RoleModelCrud.super.findRoleById(hrRoleId));
            assertNotNull(RoleModelCrud.super.findRoleByName(hrRoleName));
        });
    }
}
