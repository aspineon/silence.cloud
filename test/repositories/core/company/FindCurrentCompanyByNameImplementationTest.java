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

public class FindCurrentCompanyByNameImplementationTest extends BeforeAndAfterTest implements
        FindCurrentCompanyByNameAbstraction {

    private String  firstCompanyName        = "google";
    private String  secondCompanyName       = "facebook";
    private String  notExistsCompanyName    = "oracle";
    private String  nullCompanyName         = null;

    @Test
    public void findCurrentCompanyByFirstName(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            findCurrentExistsCompanyName(this.firstCompanyName);
        });
    }

    @Test
    public void findCurrentCompanyBySecondName(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            findCurrentExistsCompanyName(this.secondCompanyName);
        });
    }

    private void findCurrentExistsCompanyName(String companyName){

        final CompletionStage<Optional<CompanyModel>> stage = findCompanyByName(companyName);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {

            assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(company -> {

                return company.isPresent() && company.get() != null;
            });
        });
    }

    @Test
    public void findCurrentCompanyByNotExistsName(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            findNotExistsCompanyName(notExistsCompanyName);
        });
    }

    @Test
    public void findCurrentCompanyByNullName(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            findNotExistsCompanyName(nullCompanyName);
        });
    }

    private void findNotExistsCompanyName(String companyName){

        final CompletionStage<Optional<CompanyModel>> stage = findCompanyByName(companyName);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {

            assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(company -> {

                return !company.isPresent();
            });
        });
    }

    @Override
    public CompletionStage<Optional<CompanyModel>> findCompanyByName(String companyName) {

        final FindCurrentCompanyByNameImplementation findCurrentCompanyByNameImplementation = app.injector()
                .instanceOf(FindCurrentCompanyByNameImplementation.class);
        return findCurrentCompanyByNameImplementation.findCompanyByName(companyName);
    }
}
