package repositories.core;

import helpers.BeforeAndAfterTest;
import models.core.StatusModel;
import models.core.StatusModelCrud;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;

public class StatusRepositoryTest extends BeforeAndAfterTest implements StatusModelCrud {

    private final String newStatus = "newStatus";
    private final String updatedStatus = "updateStatus";

    private int expectedSize = 8;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
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