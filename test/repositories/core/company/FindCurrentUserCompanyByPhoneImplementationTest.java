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

public class FindCurrentUserCompanyByPhoneImplementationTest extends BeforeAndAfterTest implements
        FindCurrentUserCompanyByPhoneAbstraction {

    private String      firstExistsCompanyPhone     = "1";
    private String      secondExistsCompanyPhone    = "2";
    private String      notExistsCompanyPhone       = "3";
    private String      nullCompanyPhone            = null;

    private Long        userId                      = 1L;

    private UserModel   nullUserModel               = null;

    @Test
    public void findCurrentUserCompanyByFirstPhone(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            findCurrentUserCompanyByExistsPhone(this.firstExistsCompanyPhone);
        });
    }

    @Test
    public void findCurrentUserCompanyBySecondPhone(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            findCurrentUserCompanyByExistsPhone(this.secondExistsCompanyPhone);
        });
    }

    private void findCurrentUserCompanyByExistsPhone(String companyPhone){

        UserModel userModel = UserModel.FINDER.byId(this.userId);
        assertNotNull(userModel);

        final CompletionStage<Optional<CompanyModel>> stage = findCurrentUserCompanyByPhone(userModel, companyPhone);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {

            assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(company -> {

                return company.isPresent() && company.get() != null;
            });
        });
    }

    @Test
    public void findCurrentUSerCompanyByNotExistsPhoneTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            findCurrentUserCompanyByNotExistsPhone(this.notExistsCompanyPhone);
        });
    }

    @Test
    public void findCurrentUSerCompanyByNullPhoneTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            findCurrentUserCompanyByNotExistsPhone(this.nullCompanyPhone);
        });
    }

    private void findCurrentUserCompanyByNotExistsPhone(String companyPhone){

        UserModel userModel = UserModel.FINDER.byId(this.userId);
        assertNotNull(userModel);

        final CompletionStage<Optional<CompanyModel>> stage = findCurrentUserCompanyByPhone(userModel, companyPhone);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {

            assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(company -> {

                return !company.isPresent();
            });
        });
    }

    @Test
    public void findCurrentUserCompanyByNullUserAndExistsPhoneTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final CompletionStage<Optional<CompanyModel>> stage =
                    findCurrentUserCompanyByPhone(this.nullUserModel, firstExistsCompanyPhone);
            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(company -> {

                    return !company.isPresent();
                });
            });
        });
    }

    @Override
    public CompletionStage<Optional<CompanyModel>> findCurrentUserCompanyByPhone(
            UserModel userModel, String companyPhone
    ) {

        final FindCurrentUserCompanyByPhoneImplementation findCurrentUserCompanyByPhoneImplementation = app.injector()
                .instanceOf(FindCurrentUserCompanyByPhoneImplementation.class);
        return findCurrentUserCompanyByPhoneImplementation.findCurrentUserCompanyByPhone(userModel, companyPhone);
    }
}
