package repositories.core.user;

import helpers.BeforeAndAfterTest;
import models.core.user.UserByIdFindable;
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

public class UpdateUserRepositoryTest extends BeforeAndAfterTest implements UserByIdFindable {

    private Long    existsId    = 1L;
    private String  newEmail    = "john1@doe.com";
    private String  newPhone    = "000000001";
    private String  existsEmail = "john@smith.com";
    private String  existsPhone = "1111111111";

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void updateUser() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final UserModel userModel = UserByIdFindable.super.findUserById(existsId).get();
            userModel.setEmail(newEmail);
            userModel.phone = newPhone;

            final UpdateUserRepository updateUserRepository = app.injector().instanceOf(UpdateUserRepository.class);
            final CompletionStage<Optional<UserModel>> stage = updateUserRepository.updateUser(userModel);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                    return user.isPresent() && user.get() != null;
                });
            });
        });
    }

    @Test
    public void updateUserWithExistsEmail() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final UserModel userModel = UserByIdFindable.super.findUserById(existsId).get();
            userModel.setEmail(existsEmail);
            userModel.phone = newPhone;

            final UpdateUserRepository updateUserRepository = app.injector().instanceOf(UpdateUserRepository.class);
            final CompletionStage<Optional<UserModel>> stage = updateUserRepository.updateUser(userModel);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                    return !user.isPresent();
                });
            });
        });
    }

    @Test
    public void updateUserWithExistsPhone() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final UserModel userModel = UserByIdFindable.super.findUserById(existsId).get();
            userModel.setEmail(newEmail);
            userModel.phone = existsPhone;

            final UpdateUserRepository updateUserRepository = app.injector().instanceOf(UpdateUserRepository.class);
            final CompletionStage<Optional<UserModel>> stage = updateUserRepository.updateUser(userModel);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                    return !user.isPresent();
                });
            });
        });
    }
}