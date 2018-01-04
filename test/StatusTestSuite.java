import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        integration.models.core.StatusIntegrationTest.class,
        models.core.StatusModelCrudTest.class,
        repositories.core.StatusRepositoryTest.class
})
public class StatusTestSuite {
}
