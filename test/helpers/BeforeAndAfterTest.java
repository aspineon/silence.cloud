package helpers;

import com.google.common.collect.ImmutableMap;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.Evolutions;
import play.test.WithApplication;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class BeforeAndAfterTest extends WithApplication {

    private final static DefaultStatuses defaultStatuses = new DefaultStatuses();
    private final static DefaultRoles defaultRoles = new DefaultRoles();
    private final static DefaultModules defaultModules = new DefaultModules();

    static Database database;

    @BeforeClass
    public static void setUp() throws Exception {
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

        });
    }

    @AfterClass
    public static void tearDown() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            running(fakeApplication(inMemoryDatabase("test")), () -> {
                defaultModules.deleteModules();
                defaultRoles.deleteRoles();
                defaultStatuses.deleteDefaultStatuses();

                Evolutions.cleanupEvolutions(database);
                database.shutdown();
            });
        });
    }

}
