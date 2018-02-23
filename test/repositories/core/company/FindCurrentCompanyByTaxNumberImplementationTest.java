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

public class FindCurrentCompanyByTaxNumberImplementationTest extends BeforeAndAfterTest implements
        FindCurrentCompanyByTaxNumberAbstraction {

    private String  firstCompanyTaxNumber   = "0";
    private String  secondCompanyTaxNumber  = "1";
    private String  notExistsTaxNumber      = "99999999";
    private String  nullTaxNumber           = null;

    @Test
    public void findCurrentCompanyByFirstExistsTaxNumberTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            findCurrentCompanyByExistsTaxNumber(this.firstCompanyTaxNumber);
        });
    }

    @Test
    public void findCurrentCompanyBySecondExistsTaxNumberTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            findCurrentCompanyByExistsTaxNumber(this.secondCompanyTaxNumber);
        });
    }

    private void findCurrentCompanyByExistsTaxNumber(String taxNumber) {

        final CompletionStage<Optional<CompanyModel>> stage = findCurrentCompanyByTaxNumber(taxNumber);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {

            assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(company -> {

                return company.isPresent() && company.get() != null;
            });
        });
    }

    @Test
    public void findCurrentCompanyByNotExistsTaxNumberTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            findCurrentCompanyByNotExistsTaxNumber(this.notExistsTaxNumber);
        });
    }

    @Test
    public void findCurrentCompanyByNullTaxNumberTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            findCurrentCompanyByNotExistsTaxNumber(nullTaxNumber);
        });
    }

    private void findCurrentCompanyByNotExistsTaxNumber(String taxNumber){

        final CompletionStage<Optional<CompanyModel>> stage = findCurrentCompanyByTaxNumber(taxNumber);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {

            assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(company -> {

                return !company.isPresent();
            });
        });
    }

    @Override
    public CompletionStage<Optional<CompanyModel>> findCurrentCompanyByTaxNumber(String taxNumber) {

        final FindCurrentCompanyByTaxNumberImplementation findCurrentCompanyByTaxNumberImplementation = app.injector()
                .instanceOf(FindCurrentCompanyByTaxNumberImplementation.class);
        return findCurrentCompanyByTaxNumberImplementation.findCurrentCompanyByTaxNumber(taxNumber);
    }
}
