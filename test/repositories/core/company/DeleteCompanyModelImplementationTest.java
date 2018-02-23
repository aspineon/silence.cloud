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

public class DeleteCompanyModelImplementationTest extends BeforeAndAfterTest implements DeleteCompanyAbstraction {

    private Long companyId = 1L;

    @Test
    public void deleteCompanyTest() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            CompanyModel companyModel = new CompanyModel().FINDER.byId(companyId);
            assertNotNull(companyModel);
            final CompletionStage<Optional<CompanyModel>> stage = deleteCompany(companyModel);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(company -> {

                    return company.isPresent() && company.get() != null;
                });
            });
        });
    }

    @Test
    public void deleteNotExistsCompany(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final CompletionStage<Optional<CompanyModel>> stage = deleteCompany(null);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(company -> {

                    return !company.isPresent();
                });
            });
        });
    }

    @Override
    public CompletionStage<Optional<CompanyModel>> deleteCompany(CompanyModel companyModel) {

        final DeleteCompanyImplementation deleteCompanyImplementation = app.injector()
                .instanceOf(DeleteCompanyImplementation.class);
        return deleteCompanyImplementation.deleteCompany(companyModel);
    }
}
