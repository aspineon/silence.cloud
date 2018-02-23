package repositories.core.company;

import helpers.BeforeAndAfterTest;
import models.core.company.CompanyModel;
import models.core.user.UserModel;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertNotNull;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class FindCurrentUserCompanyByIdImplementationTest extends BeforeAndAfterTest implements
        FindCurrentUserCompanyByIdAbstraction {

    private Long        userId              = 1L;

    private Long        firstCompanyId      = 1L;
    private Long        secondCompanyId     = 2L;
    private Long        notExistsCompanyId  = 3L;

    private UserModel   nullUser            = null;

    @Test
    public void findFirstUserCompanyById() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            checkExistsCompany(this.firstCompanyId);
        });
    }

    @Test
    public void findSecondUserCompanyById() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            checkExistsCompany(this.secondCompanyId);
        });
    }

    private void checkExistsCompany(Long companyId) {
        UserModel userModel = UserModel.FINDER.byId(userId);
        assertNotNull(userModel);

        final CompletionStage<Optional<CompanyModel>> stage = findCurrentUserCompanyById(userModel, companyId);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {

            assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(company -> {

                return company.isPresent() && company.get() != null;
            });
        });
    }

    @Test
    public void findUserCompanyByNotExistsId() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            UserModel userModel = UserModel.FINDER.byId(this.userId);
            assertNotNull(userModel);

            final CompletionStage<Optional<CompanyModel>> stage =
                    findCurrentUserCompanyById(userModel, this.notExistsCompanyId);
            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(company -> {

                    return !company.isPresent();
                });
            });
        });
    }

    @Test
    public void findUserCompanyByNullUserAndExistsId() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final CompletionStage<Optional<CompanyModel>> stage =
                    findCurrentUserCompanyById(this.nullUser, this.notExistsCompanyId);
            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(company -> {

                    return !company.isPresent();
                });
            });
        });
    }

    @Override
    public CompletionStage<Optional<CompanyModel>> findCurrentUserCompanyById(UserModel userModel, Long companyId) {

        final FindCurrentUserCompanyByIdImplementation findCurrentUserCompanyByIdImplementation = app.injector()
                .instanceOf(FindCurrentUserCompanyByIdImplementation.class);
        return findCurrentUserCompanyByIdImplementation.findCurrentUserCompanyById(userModel, companyId);
    }
}
