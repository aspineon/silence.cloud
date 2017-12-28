package repositories.core;

import models.core.StatusModel;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class StatusRepositoryTest extends WithApplication implements StatusRepositoryInterface {

    private String active = "active";
    private String inactive = "inactive";

    private int expectedSize = 1;

    @Override
    protected Application provideApplication(){
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void statusRepositoryTest() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            CompletionStage<Optional<StatusModel>> insertStatusStage = insert(createObject(active));
            await().atMost(1, TimeUnit.SECONDS).until(() -> {
                assertThat(insertStatusStage.toCompletableFuture()).isCompletedWithValueMatching(status -> {
                    return status.isPresent() && status.get() != null;
                });
            });

            CompletionStage<Optional<StatusModel>> getStatusByNameStage = getStatusByName(active);
            await().atMost(1, TimeUnit.SECONDS).until(() -> {
                assertThat(getStatusByNameStage.toCompletableFuture()).isCompletedWithValueMatching(status -> {
                    return status.isPresent() && status.get() != null;
                });
            });

            StatusModel statusModel = StatusModel.findByName(active);

            CompletionStage<Optional<StatusModel>> lookupStatusStage = lookupStatus(statusModel.id);
            await().atMost(1, TimeUnit.SECONDS).until(() -> {
                assertThat(lookupStatusStage.toCompletableFuture()).isCompletedWithValueMatching(status -> {
                    return status.isPresent() && status.get() != null;
                });
            });

            CompletionStage<List<StatusModel>> listCompletionStage = getStatusesList();
            await().atMost(1, TimeUnit.SECONDS).until(() -> {
                assertThat(listCompletionStage.toCompletableFuture()).isCompletedWithValueMatching(list -> {
                    return list.size() == expectedSize;
                });
            });

            CompletionStage<Optional<StatusModel>> updateStatusStage = update(statusModel.id, createObject(inactive));
            await().atMost(1, TimeUnit.SECONDS).until(() -> {
                assertThat(updateStatusStage.toCompletableFuture()).isCompletedWithValueMatching(status -> {
                    return status.isPresent() && status.get() != null;
                });
            });

            CompletionStage<Optional<StatusModel>> deleteStatusStage = delete(statusModel.id);
            await().atMost(1, TimeUnit.SECONDS).until(() -> {
                assertThat(deleteStatusStage.toCompletableFuture()).isCompletedWithValueMatching(status -> {
                    return status.isPresent() && status.get() != null;
                });
            });
        });

    }

    @Override
    public CompletionStage<List<StatusModel>> getStatusesList() {
        final StatusRepository statusRepository = app.injector().instanceOf(StatusRepository.class);
        return statusRepository.getStatusesList();
    }

    @Override
    public CompletionStage<Optional<StatusModel>> lookupStatus(Long id) {
        final StatusRepository statusRepository = app.injector().instanceOf(StatusRepository.class);
        return statusRepository.lookupStatus(id);
    }

    @Override
    public CompletionStage<Optional<StatusModel>> getStatusByName(String name) {
        final StatusRepository statusRepository = app.injector().instanceOf(StatusRepository.class);
        return statusRepository.getStatusByName(name);
    }

    @Override
    public CompletionStage<Optional<StatusModel>> insert(StatusModel statusModel) {
        final StatusRepository statusRepository = app.injector().instanceOf(StatusRepository.class);
        return statusRepository.insert(statusModel);
    }

    @Override
    public CompletionStage<Optional<StatusModel>> update(Long id, StatusModel newStatusModelData) {
        final StatusRepository statusRepository = app.injector().instanceOf(StatusRepository.class);
        return statusRepository.update(id, newStatusModelData);
    }

    @Override
    public CompletionStage<Optional<StatusModel>> delete(Long id) {
        final StatusRepository statusRepository = app.injector().instanceOf(StatusRepository.class);
        return statusRepository.delete(id);
    }

    private StatusModel createObject(String name){
        StatusModel statusModel = new StatusModel();
        statusModel.name = name;
        statusModel.createdAt = new Date();
        statusModel.updatedAt = new Date();
        return statusModel;
    }
}