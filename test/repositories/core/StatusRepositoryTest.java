package repositories.core;

import com.google.common.collect.ImmutableMap;
import helpers.DefaultStatuses;
import models.core.StatusModel;
import models.core.StatusModelCrud;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import play.Application;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.Evolutions;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class StatusRepositoryTest extends WithApplication implements StatusModelCrud {

    private static Database database;

    private static final DefaultStatuses defaultStatuses = new DefaultStatuses();

    private final String newStatus = "newStatus";
    private final String updatedStatus = "updateStatus";

    private int expectedSize = 8;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

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
        });
    }

    @AfterClass
    public static void tearDown() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            defaultStatuses.deleteDefaultStatuses();
            Evolutions.cleanupEvolutions(database);
            database.shutdown();
        });
    }

    @Test
    public void testStatusRepository() throws Exception {
        final StatusRepository statusRepository = app.injector().instanceOf(StatusRepository.class);

        final CompletionStage<Optional<StatusModel>> createStage = statusRepository.createStatusModel(newStatus);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {
            assertThat(createStage.toCompletableFuture()).isCompletedWithValueMatching(status -> {
                return status.isPresent() && status.get() != null;
            });
        });

        final CompletionStage<List<StatusModel>> findAllStatusesStage = statusRepository.findAllStatuses();
        await().atMost(1, TimeUnit.SECONDS).until(() -> {
            assertThat(findAllStatusesStage.toCompletableFuture()).isCompletedWithValueMatching(list -> {
                return list.size() == expectedSize;
            });
        });

        StatusModel statusModel = StatusModelCrud.super.findStatusByName(newStatus);
        final CompletionStage<Optional<StatusModel>> statusByIdStage = statusRepository.findStatusById(statusModel.id);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {
            assertThat(statusByIdStage.toCompletableFuture()).isCompletedWithValueMatching(status -> {
                return status.isPresent() && status.get() != null;
            });
        });

        final CompletionStage<Optional<StatusModel>> statusByName = statusRepository.findStatusByName(newStatus);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {
            assertThat(statusByIdStage.toCompletableFuture()).isCompletedWithValueMatching(status -> {
                return status.isPresent() && status.get() != null;
            });
        });

        final CompletionStage<Optional<StatusModel>> updateStatus = statusRepository.updateStatusModel(
                statusModel.id, updatedStatus
        );
        await().atMost(1, TimeUnit.SECONDS).until(() -> {
            assertThat(updateStatus.toCompletableFuture()).isCompletedWithValueMatching(status -> {
                return status.isPresent() && status.get() != null;
            });
        });

        final CompletionStage<Optional<StatusModel>> deleteStatus = statusRepository.deleteStatusModel(statusModel.id);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {
            assertThat(deleteStatus.toCompletableFuture()).isCompletedWithValueMatching(status -> {
                return !status.isPresent();
            });
        });
    }
}