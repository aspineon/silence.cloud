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

public class FindUserByEmailAndPasswordRepositoryTest extends BeforeAndAfterTest {

    private String existsEmail          = "john@doe.com";
    private String notExistsEmail       = "john1@doe.com";
    private String nullEmail            = null;

    private String existsPassword       = "R3v3l@t104LoA";
    private String notExistsPassword    = "R3vel@t104LoA";
    private String nullPassword         = null;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void findUSerByExistsEmailAndExistsPassword() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final FindUserByEmailAndPasswordRepository findUserByEmailAndPasswordRepository = app.injector()
                    .instanceOf(FindUserByEmailAndPasswordRepository.class);
            final CompletionStage<Optional<UserModel>> stage = findUserByEmailAndPasswordRepository
                    .findUserByEmailAndPassword(existsEmail, existsPassword);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                    return user.isPresent() && user.get() != null;
                });
            });
        });
    }

    @Test
    public void findUserByNotExistsEmailAndExistsPassword() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final FindUserByEmailAndPasswordRepository findUserByEmailAndPasswordRepository = app.injector()
                    .instanceOf(FindUserByEmailAndPasswordRepository.class);
            final CompletionStage<Optional<UserModel>> stage = findUserByEmailAndPasswordRepository
                    .findUserByEmailAndPassword(notExistsEmail, existsPassword);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                    return !user.isPresent();
                });
            });
        });
    }

    @Test
    public void findUserByNullEmailAndExistsPassword() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final FindUserByEmailAndPasswordRepository findUserByEmailAndPasswordRepository = app.injector()
                    .instanceOf(FindUserByEmailAndPasswordRepository.class);
            final CompletionStage<Optional<UserModel>> stage = findUserByEmailAndPasswordRepository
                    .findUserByEmailAndPassword(nullEmail, existsPassword);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                    return !user.isPresent();
                });
            });
        });
    }

    @Test
    public void findUserByExistsEmailAndNotExistsPassword() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final FindUserByEmailAndPasswordRepository findUserByEmailAndPasswordRepository = app.injector()
                    .instanceOf(FindUserByEmailAndPasswordRepository.class);
            final CompletionStage<Optional<UserModel>> stage = findUserByEmailAndPasswordRepository
                    .findUserByEmailAndPassword(existsEmail, notExistsPassword);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                    return !user.isPresent();
                });
            });
        });
    }

    @Test
    public void findUserByExistsEmailAndNullPassword() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final FindUserByEmailAndPasswordRepository findUserByEmailAndPasswordRepository = app.injector()
                    .instanceOf(FindUserByEmailAndPasswordRepository.class);
            final CompletionStage<Optional<UserModel>> stage = findUserByEmailAndPasswordRepository
                    .findUserByEmailAndPassword(existsEmail, nullPassword);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                    return !user.isPresent();
                });
            });
        });
    }
}
