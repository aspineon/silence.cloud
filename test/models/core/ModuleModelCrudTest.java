package models.core;

import helpers.BeforeAndAfterTest;
import org.junit.Test;

import static org.junit.Assert.*;

public class ModuleModelCrudTest extends BeforeAndAfterTest implements ModuleModelCrud {

    private int firstExpectedSize = 1;
    private int secondExpectedSize = 2;

    private String existsModuleName = "sampleModule";
    private String newModuleName = "newModule";
    private String updatedModuleName = "updatedModule";

    private Long existsStatusId = 1L;
    private Long newStatusId = 2L;

    private Long existsModuleId = 1L;

    @Test
    public void testModuleModelCrud() throws Exception {
        // test default size
        assertEquals(firstExpectedSize, ModuleModelCrud.super.findAllModules().size());

        // test default status size
        assertEquals(firstExpectedSize, ModuleModelCrud.super.findAllModulesByStatus(existsStatusId).size());

        // test find default module by id
        assertNotNull(ModuleModelCrud.super.findModuleById(existsModuleId));

        // test default module by name
        assertNotNull(ModuleModelCrud.super.findModuleByName(existsModuleName));

        // test create module
        assertNotNull(ModuleModelCrud.super.createModule(newModuleName, existsStatusId));

        // test create module with exists name
        assertNull(ModuleModelCrud.super.createModule(newModuleName, existsStatusId));

        // test sizes of modules when new module create
        assertEquals(secondExpectedSize, ModuleModelCrud.super.findAllModules().size());
        assertEquals(secondExpectedSize, ModuleModelCrud.super.findAllModulesByStatus(existsStatusId).size());
        assertNotNull(ModuleModelCrud.super.findModuleByName(newModuleName));

        // test update module name
        assertNotNull(ModuleModelCrud.super.updateModuleName(existsModuleId, updatedModuleName));
        assertNull(ModuleModelCrud.super.updateModuleName(existsModuleId, updatedModuleName));
        assertNull(ModuleModelCrud.super.updateModuleName(existsModuleId, newModuleName));
        assertNotNull(ModuleModelCrud.super.findModuleByName(updatedModuleName));

        // test update module status
        assertNotNull(ModuleModelCrud.super.updateModuleStatus(existsStatusId, newStatusId));
        assertEquals(firstExpectedSize, ModuleModelCrud.super.findAllModulesByStatus(existsStatusId).size());
        assertEquals(firstExpectedSize, ModuleModelCrud.super.findAllModulesByStatus(newStatusId).size());

        // test delete module
        assertNull(ModuleModelCrud.super.deleteModule(existsModuleId));
        assertEquals(firstExpectedSize, ModuleModelCrud.super.findAllModules().size());
    }

}