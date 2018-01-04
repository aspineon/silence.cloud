package models.core;

import helpers.BeforeAndAfterTest;
import org.junit.Test;

import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class ModuleModelCrudTest extends BeforeAndAfterTest implements ModuleModelCrud {

    private int expectedSize = 1;
    private int emptyListSize = 0;

    private String existsModuleName = "sampleModule";
    private String newModuleName = "newModule";
    private String updatedModuleName = "updatedModule";
    private String notExistsModuleName = "notExistsModule";

    private Long activeStatusId = 1L;
    private Long inactiveStatusId = 2L;


    private Long notExistsStatusId = 100L;

    private Long existsModuleId = 1L;
    private Long notExistsModuleId = 100L;

    @Test
    public void findAllModulesTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertEquals(expectedSize, ModuleModelCrud.super.findAllModules().size());
        });
    }

    @Test
    public void findAllModulesByNotExistsStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertEquals(emptyListSize, ModuleModelCrud.super.findAllModulesByStatus(notExistsStatusId).size());
        });
    }

    @Test
    public void findAllModulesByExistsStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertEquals(expectedSize, ModuleModelCrud.super.findAllModulesByStatus(activeStatusId).size());
        });
    }

    @Test
    public void findModuleByNotExistsId() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(ModuleModelCrud.super.findModuleById(notExistsModuleId));
        });
    }

    @Test
    public void findModuleByExistsId() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(ModuleModelCrud.super.findModuleById(existsModuleId));
        });
    }

    @Test
    public void findModuleByNotExistsName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(ModuleModelCrud.super.findModuleByName(notExistsModuleName));
        });
    }

    @Test
    public void findModuleByExistsName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(ModuleModelCrud.super.findModuleByName(existsModuleName));
        });
    }

    @Test
    public void createModuleWithExistsName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(ModuleModelCrud.super.createModule(existsModuleName, inactiveStatusId));
        });
    }

    @Test
    public void createModuleWithNotExistsStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(ModuleModelCrud.super.createModule(newModuleName, notExistsStatusId));
        });
    }

    @Test
    public void createNewModule() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(ModuleModelCrud.super.createModule(newModuleName, inactiveStatusId));
        });
    }

    @Test
    public void updateModuleNameWithExistsId() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(ModuleModelCrud.super.updateModuleName(notExistsModuleId, newModuleName));
        });
    }

    @Test
    public void updateModuleWithExistsName() throws Exception {

        running(fakeApplication(inMemoryDatabase("tets")), () -> {

            assertNull(ModuleModelCrud.super.updateModuleName(existsModuleId, existsModuleName));
        });
    }

    @Test
    public void updateModuleName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(ModuleModelCrud.super.updateModuleName(existsModuleId, updatedModuleName));
        });
    }

    @Test
    public void updateModuleStatusWithNotExistsId() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(ModuleModelCrud.super.updateModuleStatus(notExistsModuleId, inactiveStatusId));
        });
    }

    @Test
    public void updateModuleStatusWithNotExistsStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(ModuleModelCrud.super.updateModuleStatus(existsModuleId, notExistsStatusId));
        });
    }

    @Test
    public void updateModuleStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(ModuleModelCrud.super.updateModuleStatus(existsModuleId, inactiveStatusId));
        });
    }

    @Test
    public void deleteModuleWithNotExistsId() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(ModuleModelCrud.super.deleteModule(notExistsModuleId));
        });
    }

    @Test
    public void deleteModuleWithExistsId() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(ModuleModelCrud.super.deleteModule(existsModuleId));
        });
    }
}