

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        integration.models.core.PageIntegrationTest.class,
        models.core.PageModelCrudTest.class,
        repositories.core.PageRepositoryTest.class
})
public class PageTestSuite {
}