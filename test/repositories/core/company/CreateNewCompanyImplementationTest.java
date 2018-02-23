package repositories.core.company;

import helpers.BeforeAndAfterTest;
import models.core.company.CompanyModel;
import models.core.user.UserModel;
import org.junit.Test;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class CreateNewCompanyImplementationTest extends BeforeAndAfterTest implements CreateNewCompanyAbstraction {

    private Long    userId                  = 1L;

    private String  existsCompanyEmail      = "first@example.com";
    private String  existsCompanyPhone      = "1";
    private String  existsCompanyName       = "google";
    private String  existsCompanyTaxNumber  = "0";
    private boolean primary                 = true;

    private String  newCompanyName          = "yourtube";
    private String  newCompanyEmail         = "example@comapny.com";
    private String  newCompanyPhone         = "000-000-000";
    private String  newCompanyTaxNumber     = "0123456789";

    private Long    newCompanyId            = 3L;
    private String  companyAddress          = "Address";
    private String  companyCity             = "city";
    private String  companyPostalCode       = "11-1111";
    private String  companyCountry          = "country";

    @Test
    public void createNewCompanyTest() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            CompletionStage<Optional<CompanyModel>> stage = createNewCompany(
                    createNewCompanyModel(newCompanyName, newCompanyEmail, newCompanyPhone, newCompanyTaxNumber)
            );

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(company -> {

                    return company.isPresent() && company.get() != null;
                });
            });
        });
    }

    @Test
    public void createNewCompanyWithExistsNameTest() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            CompletionStage<Optional<CompanyModel>> stage = createNewCompany(
                    createNewCompanyModel(existsCompanyName, newCompanyEmail, newCompanyPhone, newCompanyTaxNumber)
            );

            testBadResultOfCreateNewCompany(stage);
        });
    }

    @Test
    public void createNewCompanyWithExistsEmailTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            CompletionStage<Optional<CompanyModel>> stage = createNewCompany(
                    createNewCompanyModel(newCompanyName, existsCompanyEmail, newCompanyPhone, newCompanyTaxNumber)
            );

            testBadResultOfCreateNewCompany(stage);
        });
    }

    @Test
    public void createNewCompanyWithExistsPhone(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            CompletionStage<Optional<CompanyModel>> stage = createNewCompany(
                    createNewCompanyModel(newCompanyName, newCompanyEmail, existsCompanyPhone, newCompanyTaxNumber)
            );

            testBadResultOfCreateNewCompany(stage);
        });
    }

    @Test
    public void createNewCompanyWithExistsTaxNumber(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            CompletionStage<Optional<CompanyModel>> stage = createNewCompany(
                    createNewCompanyModel(newCompanyName, newCompanyEmail, newCompanyPhone, existsCompanyTaxNumber)
            );
        });
    }

    private void testBadResultOfCreateNewCompany(CompletionStage<Optional<CompanyModel>> stage) {
        await().atMost(1, TimeUnit.SECONDS).until(() -> {

            assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(company -> {

                return !company.isPresent();
            });
        });
    }

    @Override
    public CompletionStage<Optional<CompanyModel>> createNewCompany(CompanyModel companyModel) {

        final CreateNewCompanyImplementation createNewCompanyImplementation = app.injector()
                .instanceOf(CreateNewCompanyImplementation.class);
        return createNewCompanyImplementation.createNewCompany(companyModel);
    }

    private CompanyModel createNewCompanyModel(
            String companyName, String companyEmail, String companyPhone, String companyTaxNumber
    ){

        CompanyModel companyModel       = new CompanyModel();
        companyModel.id                 = this.newCompanyId;
        companyModel.createdAt          = new Date();
        companyModel.updateAt           = new Date();
        companyModel.user               = UserModel.FINDER.byId(this.userId);
        companyModel.companyName        = companyName;
        companyModel.companyEmail       = companyEmail;
        companyModel.companyPhone       = companyPhone;
        companyModel.taxNumber          = companyTaxNumber;
        companyModel.companyAddress     = this.companyAddress;
        companyModel.companyCity        = this.companyCity;
        companyModel.companyCountry     = this.companyCountry;
        companyModel .companyPostalCode = this.companyPostalCode;
        companyModel.isPrimary          = this.primary;
        return companyModel;
    }
}
