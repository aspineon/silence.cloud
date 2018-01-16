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

public class FindUserByPhoneRepositoryTest extends BeforeAndAfterTest {

    private String  existsPhone     = "000000000";
    private String  notExistsPhone  = "000000001";
    private String  nullPhone        = null;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void findUserByExistsPhone () {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final FindUserByPhoneRepository findUserByPhoneRepository = app.injector()
                    .instanceOf(FindUserByPhoneRepository.class);
            final CompletionStage<Optional<UserModel>> stage = findUserByPhoneRepository.findUserByPhone(existsPhone);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                    return user.isPresent() && user.get() != null;
                });
            });
        });
    }

    @Test
    public void findUserByNotExistsPhone () {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final FindUserByPhoneRepository findUserByPhoneRepository = app.injector()
                    .instanceOf(FindUserByPhoneRepository.class);
            final CompletionStage<Optional<UserModel>> stage = findUserByPhoneRepository
                    .findUserByPhone(notExistsPhone);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                    return !user.isPresent();
                });
            });
        });
    }

    @Test
    public void findUserByNullPhone () {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final FindUserByPhoneRepository findUserByPhoneRepository = app.injector()
                    .instanceOf(FindUserByPhoneRepository.class);
            final CompletionStage<Optional<UserModel>> stage = findUserByPhoneRepository.findUserByPhone(nullPhone);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                    return !user.isPresent();
                });
            });
        });
    }
}
