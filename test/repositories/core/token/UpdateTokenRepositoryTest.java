package repositories.core.token;

import helpers.BeforeAndAfterTest;
import models.core.user.UserModel;
import org.junit.Test;
import repositories.core.user.CreateTokenRepository;
import repositories.core.user.UpdateTokenRepository;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class UpdateTokenRepositoryTest extends BeforeAndAfterTest {

    private Long existsId = 1L;

    private UserModel nullUserModel = null;

    @Test
    public void nullUserTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final UpdateTokenRepository updateTokenRepository = app.injector().instanceOf(UpdateTokenRepository.class);
            final CompletionStage<Optional<UserModel>> stage = updateTokenRepository.updateToken(nullUserModel);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                    return !user.isPresent();
                });
            });
        });
    }

    @Test
    public void updateTokenWhenTokenNotExistsTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            UserModel currentUser = UserModel.FINDER.byId(existsId);

            final UpdateTokenRepository updateTokenRepository = app.injector().instanceOf(UpdateTokenRepository.class);
            final CompletionStage<Optional<UserModel>> updateStage = updateTokenRepository.updateToken(currentUser);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(updateStage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                    return user.isPresent() && user.get() != null;
                });
            });
        });
    }

    @Test
    public void updateTokenWhenTokenExists(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final UserModel userModel = UserModel.FINDER.byId(existsId);

            final CreateTokenRepository createTokenRepository = app.injector().instanceOf(CreateTokenRepository.class);
            final CompletionStage<Optional<UserModel>> createStage = createTokenRepository.createToken(userModel);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(createStage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                    return user.isPresent() && user.get() != null;
                });
            });

            final UserModel currentUser = UserModel.FINDER.byId(existsId);

            final UpdateTokenRepository updateTokenRepository = app.injector().instanceOf(UpdateTokenRepository.class);
            final CompletionStage<Optional<UserModel>> updateStage = updateTokenRepository.updateToken(currentUser);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(updateStage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                    return user.isPresent() && user.get() != null;
                });
            });
        });
    }
}
