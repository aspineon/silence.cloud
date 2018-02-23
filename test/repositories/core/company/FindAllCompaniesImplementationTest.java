package repositories.core.company;

import helpers.BeforeAndAfterTest;
import models.core.company.CompanyModel;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class FindAllCompaniesImplementationTest extends BeforeAndAfterTest implements FindAllCompaniesAbstraction {

    private int expectedSize = 2;

    @Test
    public void findAllCompaniesTest() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final CompletionStage<List<CompanyModel>> stage = findAllCompanies();
            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(listOfCompanies -> {

                    return listOfCompanies.size() == this.expectedSize;
                });
            });
        });
    }

    @Override
    public CompletionStage<List<CompanyModel>> findAllCompanies() {

        final FindAllCompaniesAbstraction findAllCompaniesAbstraction = app.injector()
                .instanceOf(FindAllCompaniesImplementation.class);
        return findAllCompaniesAbstraction.findAllCompanies();
    }
}
