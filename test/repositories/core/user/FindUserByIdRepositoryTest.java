package repositories.core.user;

import helpers.BeforeAndAfterTest;
import models.core.user.UserModel;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class FindUserByIdRepositoryTest extends BeforeAndAfterTest {

    private Long firstExistsId = 1L;
    private Long secondExistsId = 2L;
    private Long notExistsId = 99L;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void findUserByFirstExistsId() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final FindUserByIdRepository findUserByIdRepository = app.injector().instanceOf(
                    FindUserByIdRepository.class
            );
            final CompletionStage<Optional<UserModel>> stage = findUserByIdRepository.findUserById(firstExistsId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                    return user.isPresent() && user.get() != null;
                });
            });
        });
    }

    @Test
    public void findUserBySecondExistsId() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final FindUserByIdRepository findUserByIdRepository = app.injector().instanceOf(
                    FindUserByIdRepository.class
            );
            final CompletionStage<Optional<UserModel>> stage = findUserByIdRepository.findUserById(secondExistsId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                    return user.isPresent() && user.get() != null;
                });
            });
        });
    }

    @Test
    public void findUserByNotExistsId() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final FindUserByIdRepository findUserByIdRepository = app.injector().instanceOf(
                    FindUserByIdRepository.class
            );
            final CompletionStage<Optional<UserModel>> stage = findUserByIdRepository.findUserById(notExistsId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                    return !user.isPresent();
                });
            });
        });
    }
}
