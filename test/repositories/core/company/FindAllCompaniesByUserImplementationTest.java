package repositories.core.company;

import helpers.BeforeAndAfterTest;
import models.core.company.CompanyModel;
import models.core.user.UserModel;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertNotNull;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class FindAllCompaniesByUserImplementationTest extends BeforeAndAfterTest implements
        FindAllCompaniesByUserAbstraction {

    private Long    userId          = 1L;

    private int     expectedSize    = 2;

    @Test
    public void findAllCompaniesByUserTest() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            UserModel userModel = UserModel.FINDER.byId(this.userId);
            assertNotNull(userModel);

            final CompletionStage<List<CompanyModel>> stage = findAllCompaniesByUser(userModel);
            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(list -> {

                    return list.size() == this.expectedSize;
                });
            });
        });
    }

    @Override
    public CompletionStage<List<CompanyModel>> findAllCompaniesByUser(UserModel userModel) {

        final FindAllCompaniesByUserAbstraction findAllCompaniesByUserAbstraction = app.injector()
                .instanceOf(FindAllCompaniesByUserImplementation.class);
        return findAllCompaniesByUserAbstraction.findAllCompaniesByUser(userModel);
    }
}
