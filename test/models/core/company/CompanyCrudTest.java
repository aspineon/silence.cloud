package models.core.company;

import helpers.BeforeAndAfterTest;
import models.core.user.UserModel;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class CompanyCrudTest extends BeforeAndAfterTest implements CompanyCrud {

    private int     expectedResult          = 2;
    private int     expectedPrimaryResult   = 1;

    private Long    userId                  = 1L;

    private Long    firstCompanyId          = 1L;
    private Long    secondCompanyId         = 2L;
    private Long    notExistsCompanyId      = 3L;

    private String  firstCompanyName        = "google";
    private String  secondCompanyName       = "facebook";
    private String  notExistsCompanyName    = "onet";

    private String  firstCompanyTaxNumber   = "0";
    private String  secondCompanyTaxNumber  = "1";
    private String  notExistsTaxNumber      = "99999999";

    private String  firstCompanyEmail       = "first@example.com";
    private String  secondCompanyEmail      = "second@example.com";
    private String  notExistsCompanyEmail   = "third@example.com";

    private String  firstCompanyPhone       = "1";
    private String  secondCompanyPhone      = "2";
    private String  notExistsCompanyPhone   = "3";

    private Long    newCompanyId            = 3L;
    private String  newCompanyName          = "oracle";
    private String  newCompanyEmail         = "third@example.com";
    private String  newCompanyPhone         = "3";
    private String  newTaxNumber            = "3";
    private String  companyAddress          = "Address";
    private String  companyCity             = "city";
    private String  companyPostalCode       = "11-1111";
    private String  companyCountry          = "country";

    @Test
    public void findAllCompaniesTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            List<CompanyModel> companies = CompanyCrud.super.findAllCompanies();
            assertEquals(expectedResult, companies.size());
        });
    }

    @Test
    public void findAllCompaniesWithUserTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            UserModel userModel = UserModel.FINDER.byId(userId);
            assertNotNull(userModel);

            List<CompanyModel> companies = CompanyCrud.super.findAllCompaniesWithUser(userModel);
            assertEquals(expectedResult, companies.size());
        });
    }

    @Test
    public void findCompanyByIdTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(CompanyCrud.super.findCompanyById(this.firstCompanyId));
            assertNotNull(CompanyCrud.super.findCompanyById(this.secondCompanyId));
            assertNull(CompanyCrud.super.findCompanyById(this.notExistsCompanyId));
        });
    }

    @Test
    public void findCompanyByUserAndIdTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            UserModel userModel = UserModel.FINDER.byId(this.userId);
            assertNotNull(userModel);

            assertNotNull(CompanyCrud.super.findCompanyByUserAndId(userModel, this.firstCompanyId));
            assertNotNull(CompanyCrud.super.findCompanyByUserAndId(userModel, this.secondCompanyId));
            assertNull(CompanyCrud.super.findCompanyByUserAndId(userModel, this.notExistsCompanyId));
        });
    }

    @Test
    public void findCompanyByNameTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(CompanyCrud.super.findCompanyByName(this.firstCompanyName));
            assertNotNull(CompanyCrud.super.findCompanyByName(this.secondCompanyName));
            assertNull(CompanyCrud.super.findCompanyByName(this.notExistsCompanyName));
        });
    }

    @Test
    public void findCompanyByUserAndNameTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            UserModel userModel = UserModel.FINDER.byId(userId);
            assertNotNull(userModel);

            assertNotNull(CompanyCrud.super.findCompanyByUserAndName(userModel, this.firstCompanyName));
            assertNotNull(CompanyCrud.super.findCompanyByUserAndName(userModel, this.secondCompanyName));
            assertNull(CompanyCrud.super.findCompanyByUserAndName(userModel, this.notExistsCompanyName));
        });
    }

    @Test
    public void findCompanyByTaxNumber() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(CompanyCrud.super.findCompanyByTaxNumber(this.firstCompanyTaxNumber));
            assertNotNull(CompanyCrud.super.findCompanyByTaxNumber(this.secondCompanyTaxNumber));
            assertNull(CompanyCrud.super.findCompanyByTaxNumber(this.notExistsTaxNumber));
        });
    }

    @Test
    public void findCompanyByEmail() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(CompanyCrud.super.findCompanyByEmail(firstCompanyEmail));
            assertNotNull(CompanyCrud.super.findCompanyByEmail(secondCompanyEmail));
            assertNull(CompanyCrud.super.findCompanyByEmail(notExistsCompanyEmail));
        });
    }

    @Test
    public void findCompanyByUserAndEmail() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            UserModel userModel = UserModel.FINDER.byId(userId);
            assertNotNull(userModel);

            assertNotNull(CompanyCrud.super.findCompanyByUserAndEmail(userModel, firstCompanyEmail));
            assertNotNull(CompanyCrud.super.findCompanyByUserAndEmail(userModel, secondCompanyEmail));
            assertNull(CompanyCrud.super.findCompanyByUserAndEmail(userModel, notExistsCompanyEmail));
        });
    }

    @Test
    public void findCompanyByUserAndTaxNumberTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            UserModel userModel = UserModel.FINDER.byId(userId);
            assertNotNull(userModel);

            assertNotNull(CompanyCrud.super.findCompanyByUserAndTaxNumber(userModel, this.firstCompanyTaxNumber));
            assertNotNull(CompanyCrud.super.findCompanyByUserAndTaxNumber(userModel, this.secondCompanyTaxNumber));
            assertNull(CompanyCrud.super.findCompanyByUserAndTaxNumber(userModel, this.notExistsTaxNumber));
        });
    }

    @Test
    public void findCompanyByPhoneTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(CompanyCrud.super.findCompanyByPhone(this.firstCompanyPhone));
            assertNotNull(CompanyCrud.super.findCompanyByPhone(this.secondCompanyPhone));
            assertNull(CompanyCrud.super.findCompanyByPhone(this.notExistsCompanyPhone));
        });
    }

    @Test
    public void findCompanyByUserAndPhoneTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            UserModel userModel = UserModel.FINDER.byId(userId);
            assertNotNull(userModel);

            assertNotNull(CompanyCrud.super.findCompanyByUserAndPhone(userModel, firstCompanyPhone));
            assertNotNull(CompanyCrud.super.findCompanyByUserAndPhone(userModel, secondCompanyPhone));
            assertNull(CompanyCrud.super.findCompanyByUserAndPhone(userModel, notExistsCompanyPhone));
        });
    }

    @Test
    public void findPrimaryCompaniesTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertEquals(expectedPrimaryResult, CompanyCrud.super.findPrimaryCompanies().size());
        });
    }

    @Test
    public void findPrimaryUserCompany() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            UserModel userModel = UserModel.FINDER.byId(userId);
            assertNotNull(userModel);

            assertNotNull(CompanyCrud.super.findPrimaryUserCompany(userModel));
        });
    }

    @Test
    public void createNewCompanyTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            UserModel userModel = UserModel.FINDER.byId(userId);
            assertNotNull(userModel);

            assertNotNull(
                    CompanyCrud.super.createCompany(
                            createNewCompany(
                                    newCompanyName, newCompanyEmail, newCompanyPhone, newTaxNumber, false
                            )
                    )
            );
        });
    }

    @Test
    public void createCompanyWithExistsNameTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            UserModel userModel = UserModel.FINDER.byId(userId);
            assertNotNull(userModel);

            assertNull(
                    CompanyCrud.super.createCompany(
                            createNewCompany(firstCompanyName, newCompanyEmail, newCompanyPhone, newTaxNumber, false)
                    )
            );
        });
    }

    @Test
    public void createNewCompanyWithExistsEmailTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            UserModel userModel = UserModel.FINDER.byId(userId);
            assertNotNull(userModel);

            assertNull(
                    CompanyCrud.super.createCompany(
                            createNewCompany(newCompanyName, firstCompanyEmail, newCompanyPhone, newTaxNumber, false)
                    )
            );
        });
    }

    @Test
    public void createNewCompanyWithExistsPhoneTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            UserModel userModel = UserModel.FINDER.byId(userId);
            assertNotNull(userModel);

            assertNull(
                    CompanyCrud.super.createCompany(
                            createNewCompany(newCompanyName, newCompanyEmail, firstCompanyPhone, newTaxNumber, false)
                    )
            );
        });
    }

    @Test
    public void createNewCompanyWithExistsTaxNumberTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            UserModel userModel = UserModel.FINDER.byId(userId);
            assertNotNull(userModel);

            assertNull(
                    CompanyCrud.super.createCompany(
                            createNewCompany(newCompanyName, newCompanyEmail, newCompanyPhone, firstCompanyTaxNumber, false)
                    )
            );
        });
    }

    @Test
    public void createEmptyCompany() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(CompanyCrud.super.createCompany(null));
        });
    }

    @Test
    public void updateCompanyTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            CompanyModel companyModel = CompanyCrud.super.findCompanyById(firstCompanyId);
            assertNotNull(companyModel);

            companyModel.companyName = newCompanyName;
            companyModel.companyPhone = newCompanyPhone;
            companyModel.companyEmail = newCompanyEmail;
            companyModel.taxNumber = newTaxNumber;

            assertNotNull(CompanyCrud.super.updateCompany(companyModel));

        });
    }

    @Test
    public void updateCompanyModelWithExistsName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            CompanyModel companyModel = CompanyCrud.super.findCompanyById(firstCompanyId);
            assertNotNull(companyModel);

            companyModel.companyName = secondCompanyName;
            assertNull(CompanyCrud.super.updateCompany(companyModel));
        });
    }

    @Test
    public void updateCompanyModelWithExistsPhone() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            CompanyModel companyModel = CompanyCrud.super.findCompanyById(firstCompanyId);
            assertNotNull(companyModel);

            companyModel.companyPhone = secondCompanyPhone;
            assertNull(CompanyCrud.super.updateCompany(companyModel));
        });
    }

    @Test
    public void updateCompanyModelWithExistsEmail() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            CompanyModel companyModel = CompanyCrud.super.findCompanyById(firstCompanyId);
            assertNotNull(companyModel);

            companyModel.companyEmail = secondCompanyEmail;
            assertNull(CompanyCrud.super.updateCompany(companyModel));
        });
    }

    @Test
    public void updateCompanyModelWithExistsTaxNumber() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            CompanyModel companyModel = CompanyCrud.super.findCompanyById(firstCompanyId);
            assertNotNull(companyModel);

            companyModel.taxNumber = secondCompanyTaxNumber;
            assertNull(CompanyCrud.super.updateCompany(companyModel));
        });
    }

    @Test
    public void updateEmptyCompanyModel() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(CompanyCrud.super.updateCompany(null));
        });
    }

    @Test
    public void deleteCompanyModelTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            CompanyModel companyModel = CompanyCrud.super.findCompanyById(firstCompanyId);
            assertNotNull(companyModel);

            assertNotNull(CompanyCrud.super.deleteCompany(companyModel));
        });
    }

    @Test
    public void deleteEmptyCompanyModelTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(CompanyCrud.super.deleteCompany(null));
        });
    }

    private CompanyModel createNewCompany(
            String companyName, String companyEmail, String companyPhone, String taxNumber, boolean isPrimary
    ){

        CompanyModel companyModel       = new CompanyModel();
        companyModel.id                 = this.newCompanyId;
        companyModel.user               = UserModel.FINDER.byId(this.userId);
        companyModel.companyName        = companyName;
        companyModel.companyEmail       = companyEmail;
        companyModel.companyPhone       = companyPhone;
        companyModel.companyAddress     = this.companyAddress;
        companyModel.companyCity        = this.companyCity;
        companyModel.companyCountry     = this.companyCountry;
        companyModel.companyPostalCode  = this.companyPostalCode;
        companyModel.taxNumber          = taxNumber;
        companyModel.isPrimary          = isPrimary;
        companyModel.createdAt          = new Date();
        companyModel.updatedAt           = new Date();

        return companyModel;
    }
}
