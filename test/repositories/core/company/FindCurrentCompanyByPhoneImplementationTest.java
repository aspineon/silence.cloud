package repositories.core.company;

import helpers.BeforeAndAfterTest;
import models.core.company.CompanyModel;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class FindCurrentCompanyByPhoneImplementationTest extends BeforeAndAfterTest implements
        FindCurrentCompanyByPhoneAbstraction {

    private String firstExistsCompanyPhone    = "1";
    private String secondExistsCompanyPhone   = "2";
    private String notExistsCompanyPhone      = "3";
    private String nullCompanyPhone           = null;

    @Test
    public void findCurrentCompanyByFirstExistsPhone() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            findCurrentCompanyByExistsPhone(this.firstExistsCompanyPhone);
        });
    }

    @Test
    public void findCurrentCompanyBySecondExistsPhone() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            findCurrentCompanyByExistsPhone(this.secondExistsCompanyPhone);
        });
    }

    private void findCurrentCompanyByExistsPhone(String companyPhone){

        CompletionStage<Optional<CompanyModel>> stage = findCurrentCompanyByPhone(companyPhone);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {

            assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(company -> {

                return company.isPresent() && company.get() != null;
            });
        });
    }

    @Test
    public void findCurrentCompanyByNotExistsPhoneTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            findCurrentCompanyByNotExistsPhone(this.notExistsCompanyPhone);
        });
    }

    @Test
    public void findCurrentCompanyByNullPhone(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            findCurrentCompanyByNotExistsPhone(this.nullCompanyPhone);
        });
    }

    private void findCurrentCompanyByNotExistsPhone(String companyPhone){

        CompletionStage<Optional<CompanyModel>> stage = findCurrentCompanyByPhone(companyPhone);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {

            assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(company -> {

                return !company.isPresent();
            });
        });
    }

    @Override
    public CompletionStage<Optional<CompanyModel>> findCurrentCompanyByPhone(String companyPhone) {

        final FindCurrentCompanyByPhoneImplementation findCurrentCompanyByPhoneImplementation = app.injector()
                .instanceOf(FindCurrentCompanyByPhoneImplementation.class);
        return findCurrentCompanyByPhoneImplementation.findCurrentCompanyByPhone(companyPhone);
    }
}
