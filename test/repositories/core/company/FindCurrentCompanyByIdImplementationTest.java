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

public class FindCurrentCompanyByIdImplementationTest extends BeforeAndAfterTest implements
        FindCurrentCompanyByIdAbstraction {

    private Long firstCompanyId     = 1L;
    private Long secondCompanyId    = 2L;

    private Long notExistsCompanyId = 3L;

    @Test
    public void findFirstCompanyByIdTest() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            checkNotEmptyCompany(findCurrentCompanyById(this.firstCompanyId));
        });
    }

    @Test
    public void findSecondCompanyByIdTest() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            checkNotEmptyCompany(findCurrentCompanyById(this.secondCompanyId));
        });
    }

    private void checkNotEmptyCompany(CompletionStage<Optional<CompanyModel>> currentCompanyById) {
        final CompletionStage<Optional<CompanyModel>> stage = currentCompanyById;

        await().atMost(1, TimeUnit.SECONDS).until(() -> {

            assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(secondCompany -> {
                return secondCompany.isPresent() && secondCompany.get() != null;
            });
        });
    }

    @Test
    public void findNotExistsCompanyTest() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final CompletionStage<Optional<CompanyModel>> stage = findCurrentCompanyById(this.notExistsCompanyId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(company -> {

                    return !company.isPresent();
                });
            });
        });
    }

    @Override
    public CompletionStage<Optional<CompanyModel>> findCurrentCompanyById(Long id) {

        final FindCurrentCompanyByIdAbstraction findCurrentCompanyByIdAbstraction = app.injector()
                .instanceOf(FindCurrentCompanyByIdImplementation.class);
        return findCurrentCompanyByIdAbstraction.findCurrentCompanyById(id);
    }
}
