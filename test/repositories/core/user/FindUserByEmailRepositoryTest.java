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

public class FindUserByEmailRepositoryTest extends BeforeAndAfterTest {

    private String existsEmail      = "john@doe.com";
    private String notExistsEmail   = "john1@doe.com";
    private String nullEmail        = null;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void findUserByExistsEmail() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final FindUserByEmailRepository findUserByEmailRepository = app.injector().instanceOf(
                    FindUserByEmailRepository.class
            );
            final CompletionStage<Optional<UserModel>> stage = findUserByEmailRepository.findUserByEmail(existsEmail);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                    return user.isPresent() && user.get() != null;
                });
            });
        });
    }

    @Test(expected = RuntimeException.class)
    public void findUserByNotExistsEmail() {

        final FindUserByEmailRepository findUserByEmailRepository = app.injector().instanceOf(
                FindUserByEmailRepository.class
        );
        final CompletionStage<Optional<UserModel>> stage = findUserByEmailRepository.findUserByEmail(notExistsEmail);

        await().atMost(1, TimeUnit.SECONDS).until(() -> {

            assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                return !user.isPresent();
            });
        });
    }

    @Test
    public void findUserByNullEmail() {

        final FindUserByEmailRepository findUserByEmailRepository = app.injector().instanceOf(
                FindUserByEmailRepository.class
        );
        final CompletionStage<Optional<UserModel>> stage = findUserByEmailRepository.findUserByEmail(nullEmail);

        await().atMost(1, TimeUnit.SECONDS).until(() -> {

            assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                return !user.isPresent();
            });
        });
    }
}
