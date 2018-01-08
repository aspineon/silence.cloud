package helpers;

import com.google.common.collect.ImmutableMap;
import org.junit.After;
import org.junit.Before;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.Evolutions;
import play.test.WithApplication;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class BeforeAndAfterTest extends WithApplication {

    private final DefaultStatuses defaultStatuses = new DefaultStatuses();
    private final DefaultRoles defaultRoles = new DefaultRoles();
    private final DefaultModules defaultModules = new DefaultModules();
    private final DefaultPages defaultPages = new DefaultPages();

    Database database;

    @Before
    public void setUp() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            database = Databases.inMemory(
                    "mydatabase",
                    ImmutableMap.of(
                            "MODE", "MYSQL"
                    ),
                    ImmutableMap.of(
                            "logStatements", true
                    )
            );
            Evolutions.applyEvolutions(database);

            defaultStatuses.createDefaultStatuses();
            defaultRoles.createRoles();
            defaultModules.createModules();
            defaultPages.createPages();

        });
    }

    @After
    public void tearDown() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            running(fakeApplication(inMemoryDatabase("test")), () -> {
                defaultModules.deleteModules();
                defaultRoles.deleteRoles();
                defaultStatuses.deleteDefaultStatuses();
                defaultPages.deletePages();

                Evolutions.cleanupEvolutions(database);
                database.shutdown();
            });
        });
    }

}
