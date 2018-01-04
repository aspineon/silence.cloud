import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        models.core.ModuleModelCrudTest.class,
        repositories.core.ModuleRepositoryTest.class
})
public class ModuleTestSuite {
}