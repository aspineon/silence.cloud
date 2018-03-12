package repositories.core.token;

import helpers.BeforeAndAfterTest;
import models.core.user.UserModel;
import org.junit.Test;
import repositories.core.user.CreateTokenRepository;
import repositories.core.user.FindUserByIdRepository;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class CreateTokenRepositoryTest extends BeforeAndAfterTest {

    private Long existsUserId = 1L;

    private UserModel nullUser = null;

    @Test
    public void nullUserTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final CreateTokenRepository createTokenRepository = app.injector().instanceOf(CreateTokenRepository.class);
            final CompletionStage<Optional<UserModel>> stage = createTokenRepository.createToken(nullUser);
            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                    return !user.isPresent();
                });
            });
        });
    }

    @Test
    public void createTokenTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final FindUserByIdRepository findUserByIdRepository = app.injector().instanceOf(FindUserByIdRepository.class);
            final CompletionStage<Optional<UserModel>> userStage = findUserByIdRepository.findUserById(existsUserId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(userStage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                    return user.isPresent() && user.get() != null;
                });
            });

            UserModel userModel = UserModel.FINDER.byId(existsUserId);

            final CreateTokenRepository createTokenRepository = app.injector().instanceOf(CreateTokenRepository.class);
            final CompletionStage<Optional<UserModel>> tokenStage = createTokenRepository.createToken(userModel);
            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(tokenStage.toCompletableFuture()).isCompletedWithValueMatching(currentUser -> {

                    return currentUser.isPresent() && currentUser.get() != null;
                });
            });
        });
    }
}
