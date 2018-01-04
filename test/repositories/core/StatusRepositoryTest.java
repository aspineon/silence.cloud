package repositories.core;

import helpers.BeforeAndAfterTest;
import models.core.StatusModel;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class StatusRepositoryTest extends BeforeAndAfterTest {

    private final Long statusId = 1L;
    private final String statusName = "active";

    private final Long notExistsStatusId = 99L;

    private final String newStatus = "newStatus";
    private final String updatedStatus = "updatedStatus";

    private String notExistsStatusName = "notExists";

    private int expectedSize = 7;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void findAllStatusesTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final StatusRepository statusRepository = app.injector().instanceOf(StatusRepository.class);
            final CompletionStage<List<StatusModel>> stage = statusRepository.findAllStatuses();

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(list -> {

                    return list.size() == expectedSize;
                });
            });
        });
    }

    @Test
    public void findStatusByNotExistsId() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final StatusRepository statusRepository = app.injector().instanceOf(StatusRepository.class);
            final CompletionStage<Optional<StatusModel>> stage = statusRepository.findStatusById(notExistsStatusId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(status -> {

                    return !status.isPresent();
                });
            });
        });
    }

    @Test
    public void findStatusByExistsIdTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final StatusRepository statusRepository = app.injector().instanceOf(StatusRepository.class);
            final CompletionStage<Optional<StatusModel>> stage = statusRepository.findStatusById(statusId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(status -> {

                    return status.isPresent() && status.get() != null;
                });
            });
        });
    }

    @Test
    public void findStatusByNotExistsNameTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final StatusRepository statusRepository = app.injector().instanceOf(StatusRepository.class);
            final CompletionStage<Optional<StatusModel>> stage = statusRepository.findStatusByName(notExistsStatusName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(status -> {

                    return !status.isPresent();
                });
            });
        });
    }

    @Test
    public void findStatusByExistsNameTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final StatusRepository statusRepository = app.injector().instanceOf(StatusRepository.class);
            final CompletionStage<Optional<StatusModel>> stage = statusRepository.findStatusByName(statusName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(status -> {

                    return status.isPresent() && status.get() != null;
                });
            });
        });
    }

    @Test
    public void createNewStatusWithExistsStatusName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final StatusRepository statusRepository = app.injector().instanceOf(StatusRepository.class);
            final CompletionStage<Optional<StatusModel>> stage = statusRepository.createStatusModel(statusName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(status -> {

                    return !status.isPresent();
                });
            });
        });
    }

    @Test
    public void createStatusWithNewName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final StatusRepository statusRepository = app.injector().instanceOf(StatusRepository.class);
            final CompletionStage<Optional<StatusModel>> stage = statusRepository.createStatusModel(newStatus);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(status -> {

                    return status.isPresent() && status.get() != null;
                });
            });
        });
    }

    @Test
    public void updateStatusWithNotExistsId() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final StatusRepository statusRepository = app.injector().instanceOf(StatusRepository.class);

            final CompletionStage<Optional<StatusModel>> createStage = statusRepository.createStatusModel(newStatus);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(createStage.toCompletableFuture()).isCompletedWithValueMatching(status -> {
                    return status.isPresent() && status.get() != null;
                });
            });

            final CompletionStage<Optional<StatusModel>> updateStage = statusRepository.updateStatusModel(
                    notExistsStatusId, updatedStatus
            );

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(updateStage.toCompletableFuture()).isCompletedWithValueMatching(status -> {

                    return !status.isPresent();
                });
            });
        });
    }


    @Test
    public void updateStatusWithExistsStatusName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final StatusRepository statusRepository = app.injector().instanceOf(StatusRepository.class);

            final CompletionStage<Optional<StatusModel>> createStage = statusRepository.createStatusModel(newStatus);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(createStage.toCompletableFuture()).isCompletedWithValueMatching(status -> {
                    return status.isPresent() && status.get() != null;
                });
            });

            final CompletionStage<Optional<StatusModel>> updateStage = statusRepository
                    .updateStatusModel(statusId, statusName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(updateStage.toCompletableFuture()).isCompletedWithValueMatching(status -> {

                    return !status.isPresent();
                });
            });
        });
    }

    @Test
    public void updateStatusTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final StatusRepository statusRepository = app.injector().instanceOf(StatusRepository.class);

            final CompletionStage<Optional<StatusModel>> createStage = statusRepository.createStatusModel(newStatus);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(createStage.toCompletableFuture()).isCompletedWithValueMatching(status -> {
                    return status.isPresent() && status.get() != null;
                });
            });

            final CompletionStage<Optional<StatusModel>> updateStage = statusRepository
                    .updateStatusModel(statusId, updatedStatus);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(updateStage.toCompletableFuture()).isCompletedWithValueMatching(status -> {

                    return status.isPresent() && status.get() != null;
                });
            });
        });
    }

    @Test
    public void deleteStatusWithNotExistsId() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final StatusRepository statusRepository = app.injector().instanceOf(StatusRepository.class);
            final CompletionStage<Optional<StatusModel>> stage = statusRepository.deleteStatusModel(notExistsStatusId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(status -> {

                    return status.isPresent() && status.get() != null;
                });
            });
        });
    }

    @Test
    public void deleteStatusWithExistsId() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final StatusRepository statusRepository = app.injector().instanceOf(StatusRepository.class);
            final CompletionStage<Optional<StatusModel>> stage = statusRepository.deleteStatusModel(statusId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(status -> {

                    return !status.isPresent();
                });
            });
        });
    }
}