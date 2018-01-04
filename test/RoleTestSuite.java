import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        integration.models.core.RoleIntegrationTest.class,
        models.core.RoleModelCrudTest.class,
        repositories.core.RoleRepositoryTest.class
})
public class RoleTestSuite {
}
