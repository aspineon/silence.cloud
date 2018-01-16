package repositories.core.user;

import helpers.BeforeAndAfterTest;
import models.core.user.UserModel;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class FindAllUsersRepositoryTest extends BeforeAndAfterTest {

    private int expectedSize = 2;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void findAllUsers() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final FindAllUsersRepository findAllUsersRepository = app.injector().instanceOf(FindAllUsersRepository.class);
            final CompletionStage<List<UserModel>> stage = findAllUsersRepository.findAllUsers();

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(users -> {

                    return users.size() == expectedSize;
                });
            });
        });
    }
}
