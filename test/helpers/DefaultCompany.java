package helpers;

import models.core.company.CompanyModel;
import models.core.user.UserModel;

import java.util.Date;

public class DefaultCompany {

    private Long userId = 1L;

    private Long    firstCompanyId          = 1L;
    private String  firstCompanyEmail       = "first@example.com";
    private String  firstCompanyPhone       = "1";
    private String  firstCompanyName        = "google";
    private String  firstCompanyTaxNumber   = "0";
    private boolean primary                 = true;

    private Long    secondCompanyId         = 2L;
    private String  secondCompanyEmail      = "second@example.com";
    private String  secondCompanyPhone      = "2";
    private String  secondCompanyName       = "facebook";
    private String  secondCompanyTaxNumber  = "1";
    private boolean secondary               = false;

    private String  companyAddress          = "Address";
    private String  companyCity             = "city";
    private String  companyPostalCode       = "11-1111";
    private String  companyCountry          = "country";

    public void createCompanies(){

        createCompany(
                this.firstCompanyId, this.firstCompanyName, this.firstCompanyEmail, this.firstCompanyPhone,
                this.firstCompanyTaxNumber, this.primary
        );
        createCompany(
                this.secondCompanyId, this.secondCompanyName, this.secondCompanyEmail, this.secondCompanyPhone,
                this.secondCompanyTaxNumber, this.secondary
        );
    }

    private void createCompany(
            Long id, String companyName, String email, String phone, String companyTaxNumber, boolean isPrimary
    ){

        CompanyModel companyModel       = new CompanyModel();
        companyModel.id                 = id;
        companyModel.user               = UserModel.FINDER.byId(this.userId);
        companyModel.companyName        = companyName;
        companyModel.companyPhone       = phone;
        companyModel.companyEmail       = email;
        companyModel.companyAddress     = this.companyAddress;
        companyModel.companyCity        = this.companyCity;
        companyModel.companyCountry     = this.companyCountry;
        companyModel.companyPostalCode  = this.companyPostalCode;
        companyModel.taxNumber          = companyTaxNumber;
        companyModel.isPrimary          = isPrimary;
        companyModel.createdAt          = new Date();
        companyModel.updatedAt           = new Date();

        companyModel.save();
    }

    public void deleteCompanies(){

        for(CompanyModel companyModel: new CompanyModel().FINDER.all()){

            companyModel.delete();
        }
    }

}
