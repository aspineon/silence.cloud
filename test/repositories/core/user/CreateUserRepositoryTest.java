package repositories.core.user;

import helpers.BeforeAndAfterTest;
import models.core.user.UserModel;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class CreateUserRepositoryTest extends BeforeAndAfterTest {

    private String  username        = "user name";
    private String  newEmail        = "john1@doe.com";
    private String  newPhone        = "000000001";

    private String  existsEmail     = "john@doe.com";
    private String  existsPhone     = "000000000";

    private String  password        = "R3v3l@t104LoA";

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void createNewUser() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final UserModel userModel = createNewUser(newEmail, newPhone);

            final CreateUserRepository createUserRepository = app.injector().instanceOf(CreateUserRepository.class);
            final CompletionStage<Optional<UserModel>> stage = createUserRepository.createUser(userModel);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                    return user.isPresent() && user.get() != null;
                });
            });
        });
    }

    @Test
    public void createUserByExistsEmail() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final UserModel userModel = createNewUser(existsEmail, newPhone);

            createUser(userModel);
        });
    }

    @Test
    public void createUserByExistsPhone() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final UserModel userModel = createNewUser(newEmail, existsPhone);

            createUser(userModel);
        });
    }

    private void createUser(UserModel userModel) {
        final CreateUserRepository createUserRepository = app.injector().instanceOf(CreateUserRepository.class);
        final CompletionStage<Optional<UserModel>> stage = createUserRepository.createUser(userModel);

        await().atMost(1, TimeUnit.SECONDS).until(() -> {

            assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(user -> {

                return !user.isPresent();
            });
        });
    }

    private UserModel createNewUser(String email, String phone) {

        UserModel userModel = new UserModel();

        userModel.id = System.currentTimeMillis();
        userModel.createdAt = new Date();
        userModel.updateAt = new Date();
        userModel.username = username;
        userModel.setEmail(email);
        userModel.setPassword(password);
        userModel.phone = phone;
        userModel.isAdmin = true;
        userModel.setToken();
        userModel.setUuid();

        return userModel;
    }
}
