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

public class FindCurrentUserCompanyByNameImplementationTest extends BeforeAndAfterTest implements
        FindCurrentUserCompanyByNameAbstraction {

    private String      firstCompanyName        = "google";
    private String      secondCompanyName       = "facebook";
    private String      notExistsCompanyName    = "oracle";
    private String      nullCompanyName         = null;

    private Long        userId                  = 1L;
    private UserModel   nullUser                = null;

    @Test
    public void findCurrentUSerCompanyByFirstCompanyName() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            checkExistsCompanyName(this.firstCompanyName);
        });
    }

    @Test
    public void findCurrentUSerCompanyBySecondCompanyName() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            checkExistsCompanyName(this.secondCompanyName);
        });
    }

    private void checkExistsCompanyName(String companyName){

        UserModel userModel = UserModel.FINDER.byId(userId);
        assertNotNull(userModel);

        CompletionStage<Optional<CompanyModel>> stage = findCurrentUserCompanyByName(userModel, companyName);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {

            assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(company -> {

                return company.isPresent() && company.get() != null;
            });
        });
    }

    @Test
    public void findCurrentUserCompanyByNotExistsName() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            checkNotExistsCompanyName(this.notExistsCompanyName);
        });
    }

    @Test
    public void findCurrentUserCompanyByNullsName() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            checkNotExistsCompanyName(this.nullCompanyName);
        });
    }

    private void checkNotExistsCompanyName(String companyName){

        UserModel userModel = UserModel.FINDER.byId(userId);
        assertNotNull(userModel);

        CompletionStage<Optional<CompanyModel>> stage = findCurrentUserCompanyByName(userModel, companyName);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {

            assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(company -> {

                return !company.isPresent();
            });
        });
    }

    @Test
    public void findCurrentUserCompanyByNameForNotExistsUser(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            CompletionStage<Optional<CompanyModel>> stage =
                    findCurrentUserCompanyByName(this.nullUser, this.firstCompanyName);
            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(company -> {

                    return !company.isPresent();
                });
            });
        });
    }

    @Override
    public CompletionStage<Optional<CompanyModel>> findCurrentUserCompanyByName(UserModel userModel, String companyName) {

        FindCurrentUserCompanyByNameAbstraction findCurrentUserCompanyByNameAbstraction = app.injector()
                .instanceOf(FindCurrentUserCompanyByNameImplementation.class);
        return findCurrentUserCompanyByNameAbstraction.findCurrentUserCompanyByName(userModel, companyName);
    }
}
