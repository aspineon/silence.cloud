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

public class FindCurrentCompanyByEmailImplementationTest extends BeforeAndAfterTest implements
        FindCurrentCompanyByEmailAbstraction {

    private String  firstCompanyEmail       = "first@example.com";
    private String  secondCompanyEmail      = "second@example.com";
    private String  notExistsCompanyEmail   = "third@example.com";
    private String  nullCompanyEmail        = null;

    @Test
    public void findCurrentCompanyByFirstExistsEmail(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            findCurrentCompanyByExistsEmail(this.firstCompanyEmail);
        });
    }

    @Test
    public void findCurrentCompanyBySecondExistsEmail(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            findCurrentCompanyByExistsEmail(this.secondCompanyEmail);
        });
    }

    private void findCurrentCompanyByExistsEmail(String companyEmail){

        CompletionStage<Optional<CompanyModel>> stage = findCurrentCompanyByEmail(companyEmail);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {

            assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(company -> {

                return company.isPresent() && company.get() != null;
            });
        });
    }

    @Test
    public void findCurrentCompanyByNotExistsEmailTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            findCurrentCompanyByNotExistsEmail(this.notExistsCompanyEmail);
        });
    }

    @Test
    public void findCurrentCompanyByNullEmailTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            findCurrentCompanyByNotExistsEmail(this.nullCompanyEmail);
        });
    }

    private void findCurrentCompanyByNotExistsEmail(String companyEmail){

        CompletionStage<Optional<CompanyModel>> stage = findCurrentCompanyByEmail(companyEmail);

    }

    @Override
    public CompletionStage<Optional<CompanyModel>> findCurrentCompanyByEmail(String companyEmail) {

        final FindCurrentCompanyByEmailImplementation findCurrentCompanyByEmailImplementation = app.injector()
                .instanceOf(FindCurrentCompanyByEmailImplementation.class);
        return findCurrentCompanyByEmailImplementation.findCurrentCompanyByEmail(companyEmail);
    }
}
