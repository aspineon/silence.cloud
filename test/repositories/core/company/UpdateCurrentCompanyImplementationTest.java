package repositories.core.company;

import helpers.BeforeAndAfterTest;
import models.core.company.CompanyModel;
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

public class UpdateCurrentCompanyImplementationTest extends BeforeAndAfterTest implements
        UpdateCurrentCompanyAbstraction {

    private Long    currentCompanyId        = 1L;

    private String  existsCompanyEmail      = "second@example.com";
    private String  existsCompanyPhone      = "2";
    private String  existsCompanyName       = "facebook";
    private String  existsCompanyTaxNumber  = "1";

    private String  newCompanyName          = "yourtube";
    private String  newCompanyEmail         = "example@comapny.com";
    private String  newCompanyPhone         = "000-000-000";
    private String  newCompanyTaxNumber     = "0123456789";


    @Test
    public void updateCompanyTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            CompanyModel companyModel = new CompanyModel().FINDER.byId(currentCompanyId);
            assertNotNull(companyModel);

            companyModel.companyName    = newCompanyName;
            companyModel.companyEmail   = newCompanyEmail;
            companyModel.companyPhone   = newCompanyPhone;
            companyModel.taxNumber      = newCompanyTaxNumber;

            final CompletionStage<Optional<CompanyModel>> stage = updateCompany(companyModel);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(company -> {

                    return company.isPresent() && company.get() != null;
                });
            });
        });
    }

    @Test
    public void updateCompanyWithExistsName() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            CompanyModel companyModel = new CompanyModel().FINDER.byId(currentCompanyId);
            assertNotNull(companyModel);

            companyModel.companyName = existsCompanyName;

            testBadUpdateCompanyResult(companyModel);
        });
    }

    @Test
    public void updateCompanyWithExistsEmail() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            CompanyModel companyModel = new CompanyModel().FINDER.byId(currentCompanyId);
            assertNotNull(companyModel);

            companyModel.companyEmail = existsCompanyEmail;

            testBadUpdateCompanyResult(companyModel);
        });
    }

    @Test
    public void updateCompanyWithExistsPhone() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            CompanyModel companyModel = new CompanyModel().FINDER.byId(currentCompanyId);
            assertNotNull(companyModel);

            companyModel.companyPhone = existsCompanyPhone;

            testBadUpdateCompanyResult(companyModel);
        });
    }

    @Test
    public void updateCompanyWithExistsTaxNumber() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            CompanyModel companyModel = new CompanyModel().FINDER.byId(currentCompanyId);
            assertNotNull(companyModel);

            companyModel.taxNumber = existsCompanyTaxNumber;

            testBadUpdateCompanyResult(companyModel);
        });
    }

    @Test
    public void updateNullCompany() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            testBadUpdateCompanyResult(null);
        });
    }

    private void testBadUpdateCompanyResult(CompanyModel companyModel) {

        final CompletionStage<Optional<CompanyModel>> stage = updateCompany(companyModel);

        await().atMost(1, TimeUnit.SECONDS).until(() -> {

            assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(company -> {

                return !company.isPresent();
            });
        });
    }

    @Override
    public CompletionStage<Optional<CompanyModel>> updateCompany(CompanyModel companyModel) {

        final UpdateCurrentCompanyImplementation updateCurrentCompanyImplementation = app.injector()
                .instanceOf(UpdateCurrentCompanyImplementation.class);
        return updateCurrentCompanyImplementation.updateCompany(companyModel);
    }
}
