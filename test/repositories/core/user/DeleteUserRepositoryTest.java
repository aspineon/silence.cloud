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

public class DeleteUserRepositoryTest extends BeforeAndAfterTest {

    private Long    existsId    = 1L;
    private Long    notExistsId = 99L;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void deleteUserWithExistsId() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final DeleteUserRepository deleteUserRepository = app.injector().instanceOf(DeleteUserRepository.class);
            final CompletionStage<Optional<UserModel>> stage = deleteUserRepository.deleteUser(existsId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                    return !user.isPresent();
                });
            });
        });
    }

    @Test
    public void deleteUserWithNotExistsId() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final DeleteUserRepository deleteUserRepository = app.injector().instanceOf(DeleteUserRepository.class);
            final CompletionStage<Optional<UserModel>> stage = deleteUserRepository.deleteUser(notExistsId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                    return user.isPresent() && user.get() != null;
                });
            });
        });
    }
}
