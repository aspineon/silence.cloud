package repositories.core.user;

import helpers.BeforeAndAfterTest;
import models.core.user.UserModel;
import org.junit.Test;
import org.mockito.internal.matchers.Find;
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

public class FindUserByPhoneAndPasswordRepositoryTest extends BeforeAndAfterTest {

    private String  existsPhone         = "000000000";
    private String  notExistsPhone      = "000000001";
    private String  nullPhone           = null;

    private String  existsPassword      = "R3v3l@t104LoA";
    private String  notExistsPassword   = "R3vel@t104LoA";
    private String  nullPassword        = null;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void findUserByExistsPhoneAndExistsPasswordTest() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final FindUserByPhoneAndPasswordRepository findUserByPhoneAndPasswordRepository = app.injector()
                    .instanceOf(FindUserByPhoneAndPasswordRepository.class);
            final CompletionStage<Optional<UserModel>> stage = findUserByPhoneAndPasswordRepository
                    .findUserByPhoneAndPassword(existsPhone, existsPassword);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                    return user.isPresent() && user.get() != null;
                });
            });
        });
    }

    @Test
    public void findUserByNotExistsPhoneAndExistsPasswordTest() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final FindUserByPhoneAndPasswordRepository findUserByPhoneAndPasswordRepository = app.injector()
                    .instanceOf(FindUserByPhoneAndPasswordRepository.class);
            final CompletionStage<Optional<UserModel>> stage = findUserByPhoneAndPasswordRepository
                    .findUserByPhoneAndPassword(notExistsPhone, existsPassword);

            await().atMost(1, TimeUnit.SECONDS). until(() ->{

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                    return !user.isPresent();
                });
            });
        });
    }

    @Test
    public void findUserByNullPhoneAndExistsPasswordTest() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final FindUserByPhoneAndPasswordRepository findUserByPhoneAndPasswordRepository = app.injector()
                    .instanceOf(FindUserByPhoneAndPasswordRepository.class);
            final CompletionStage<Optional<UserModel>> stage = findUserByPhoneAndPasswordRepository
                    .findUserByPhoneAndPassword(nullPhone, existsPassword);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                    return !user.isPresent();
                });
            });
        });
    }

    @Test
    public void findUserByExistsPhoneAndNotExistsPassword() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final FindUserByPhoneAndPasswordRepository findUserByPhoneAndPasswordRepository = app.injector()
                    .instanceOf(FindUserByPhoneAndPasswordRepository.class);
            final CompletionStage<Optional<UserModel>> stage = findUserByPhoneAndPasswordRepository
                    .findUserByPhoneAndPassword(existsPhone, notExistsPassword);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                    return !user.isPresent();
                });
            });
        });
    }

    @Test
    public void findUserByExistsPhoneAndNullPassword() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final FindUserByPhoneAndPasswordRepository findUserByPhoneAndPasswordRepository = app.injector()
                    .instanceOf(FindUserByPhoneAndPasswordRepository.class);
            final CompletionStage<Optional<UserModel>> stage = findUserByPhoneAndPasswordRepository
                    .findUserByPhoneAndPassword(existsPhone, nullPassword);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                    return !user.isPresent();
                });
            });
        });
    }
}
