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

public class FindCurrentUserCompanyByEmailImplementationTest extends BeforeAndAfterTest implements
        FindCurrentUserCompanyByEmailAbstraction {

    private String      firstCompanyEmail       = "first@example.com";
    private String      secondCompanyEmail      = "second@example.com";
    private String      notExistsCompanyEmail   = "third@example.com";
    private String      nullCompanyEmail        = null;

    private Long        userId                  = 1L;

    private UserModel   nullUser                = null;

    @Test
    public void findCurrentUserCompanyByFirstExistsEmail() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            findCurrentUserCompanyByExistsEmail(this.firstCompanyEmail);
        });
    }

    @Test
    public void findCurrentUserCompanyBySecondExistsEmail() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            findCurrentUserCompanyByExistsEmail(this.secondCompanyEmail);
        });
    }

    private void findCurrentUserCompanyByExistsEmail(String companyEmail){

        UserModel userModel = UserModel.FINDER.byId(userId);
        assertNotNull(userModel);

        final CompletionStage<Optional<CompanyModel>> stage = findCurrentUserCompanyByEmail(userModel, companyEmail);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {

            assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(company -> {

                return company.isPresent() && company.get() != null;
            });
        });
    }

    @Test
    public void findCurrentCompanyByNotExistsEmailTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            findCurrentUserCompanyByNotExistsEmail(this.notExistsCompanyEmail);
        });
    }

    @Test
    public void findCurrentCompanyByNullEmailTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            findCurrentUserCompanyByNotExistsEmail(this.nullCompanyEmail);
        });
    }

    private void findCurrentUserCompanyByNotExistsEmail(String companyEmail){

        UserModel userModel = UserModel.FINDER.byId(userId);
        assertNotNull(userModel);

        final CompletionStage<Optional<CompanyModel>> stage = findCurrentUserCompanyByEmail(userModel, companyEmail);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {

            assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(company -> {

                return !company.isPresent();
            });
        });
    }

    @Test
    public void findCurrentUserCompanyByNullUserAndExistsEmailTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            CompletionStage<Optional<CompanyModel>> stage = findCurrentUserCompanyByEmail(
                    this.nullUser, firstCompanyEmail
            );
            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(company -> {

                    return !company.isPresent();
                });
            });
        });
    }

    @Override
    public CompletionStage<Optional<CompanyModel>> findCurrentUserCompanyByEmail(
            UserModel userModel, String companyEmail
    ) {

        final FindCurrentUserCompanyByEmailImplementation findCurrentUserCompanyByEmailImplementation = app.injector()
                .instanceOf(FindCurrentUserCompanyByEmailImplementation.class);
        return findCurrentUserCompanyByEmailImplementation.findCurrentUserCompanyByEmail(userModel, companyEmail);
    }
}
