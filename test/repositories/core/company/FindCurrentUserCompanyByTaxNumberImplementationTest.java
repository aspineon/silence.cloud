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

public class FindCurrentUserCompanyByTaxNumberImplementationTest extends BeforeAndAfterTest implements
        FindCurrentUserCompanyByTaxNumberAbstraction {

    private String      firstCompanyTaxNumber   = "0";
    private String      secondCompanyTaxNumber  = "1";
    private String      notExistsTaxNumber      = "99999999";
    private String      nullTaxNumber           = null;

    private Long        userId                  = 1L;

    private UserModel   nullUserModel           = null;

    @Test
    public void findCurrentUserCompanyByFirstExistsTaxNumberTest() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            findCurrentUserCompanyByExistsTaxNumber(this.firstCompanyTaxNumber);
        });
    }

    @Test
    public void findCurrentUserCompanyBySecondExistsTaxNumberTest() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            findCurrentUserCompanyByExistsTaxNumber(this.secondCompanyTaxNumber);
        });
    }

    private void findCurrentUserCompanyByExistsTaxNumber(String taxNumber){

        UserModel userModel = UserModel.FINDER.byId(userId);
        assertNotNull(userModel);

        final CompletionStage<Optional<CompanyModel>> stage = findCurrentUserCompanyByTaxNumber(userModel, taxNumber);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {

            assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(company -> {

                return company.isPresent() && company.get() != null;
            });
        });
    }

    @Test
    public void findCurrentUserCompanyByNotExistsTaxNumberTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            findCurrentUserCompanyByNotExistsTaxNumber(this.notExistsTaxNumber);
        });
    }

    @Test
    public void findCurrentUserCompanyByNullTaxNumberTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            findCurrentUserCompanyByNotExistsTaxNumber(this.nullTaxNumber);
        });
    }

    private void findCurrentUserCompanyByNotExistsTaxNumber(String taxNumber){

        UserModel userModel = UserModel.FINDER.byId(userId);
        assertNotNull(userModel);

        final CompletionStage<Optional<CompanyModel>> stage = findCurrentUserCompanyByTaxNumber(userModel, taxNumber);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {

            assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(company -> {

                return !company.isPresent();
            });
        });
    }

    @Test
    public void findCurrentUserCompanyByTaxNumberWithExistsTaxNumberAndNotExistsUser(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final CompletionStage<Optional<CompanyModel>> stage = findCurrentUserCompanyByTaxNumber(
                    this.nullUserModel, firstCompanyTaxNumber
            );
            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(company -> {

                    return !company.isPresent();
                });
            });
        });
    }

    @Override
    public CompletionStage<Optional<CompanyModel>> findCurrentUserCompanyByTaxNumber(
            UserModel userModel, String taxNumber
    ) {

        final FindCurrentUserCompanyByTaxNumberImplementation findCurrentUserCompanyByTaxNumberImplementation = app
                .injector().instanceOf(FindCurrentUserCompanyByTaxNumberImplementation.class);
        return findCurrentUserCompanyByTaxNumberImplementation.findCurrentUserCompanyByTaxNumber(userModel, taxNumber);
    }
}
