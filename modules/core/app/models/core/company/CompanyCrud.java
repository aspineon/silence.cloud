package models.core.company;

import models.core.user.UserModel;

import java.util.List;

public interface CompanyCrud {

    default List<CompanyModel> findAllCompanies(){

        return new CompanyModel().FINDER.all();
    }

    default List<CompanyModel> findAllCompaniesWithUser(UserModel userModel){

        return new CompanyModel().FINDER.query().where().eq("user", userModel).findList();
    }

    default CompanyModel findCompanyById(Long id){

        return new CompanyModel().FINDER.byId(id);
    }

    default CompanyModel findCompanyByUserAndId(UserModel userModel, Long id){

        return new CompanyModel().FINDER.query().where().eq("user", userModel)
                .eq("id", id).findOne();
    }

    default CompanyModel findCompanyByName(String companyName){

        return new CompanyModel().FINDER.query().where().eq("companyName", companyName).findOne();
    }

    default CompanyModel findCompanyByUserAndName(UserModel userModel, String companyName){

        return new CompanyModel().FINDER.query().where().eq("user", userModel)
                .eq("companyName", companyName).findOne();
    }

    default CompanyModel findCompanyByTaxNumber(String taxNumber){

        return new CompanyModel().FINDER.query().where().eq("taxNumber", taxNumber).findOne();
    }

    default CompanyModel findCompanyByUserAndTaxNumber(UserModel userModel, String taxNumber){

        return new CompanyModel().FINDER.query().where().eq("user", userModel)
                .eq("taxNumber", taxNumber).findOne();
    }

    default CompanyModel findCompanyByEmail(String companyEmail){

        return new CompanyModel().FINDER.query().where().eq("companyEmail", companyEmail).findOne();
    }

    default CompanyModel findCompanyByUserAndEmail(UserModel userModel, String companyEmail){
        return new CompanyModel().FINDER.query().where().eq("user", userModel)
                .eq("companyEmail", companyEmail).findOne();
    }

    default CompanyModel findCompanyByPhone(String companyPhone){

        return new CompanyModel().FINDER.query().where().eq("companyPhone", companyPhone).findOne();
    }

    default CompanyModel findCompanyByUserAndPhone(UserModel userModel, String companyPhone){

        return new CompanyModel().FINDER.query().where().eq("user", userModel)
                .eq("companyPhone", companyPhone).findOne();
    }

    default List<CompanyModel> findPrimaryCompanies(){

        return new CompanyModel().FINDER.query().where().eq("isPrimary", true).findList();
    }

    default CompanyModel findPrimaryUserCompany(UserModel userModel){

        return new CompanyModel().FINDER.query().where().eq("user", userModel)
                .eq("isPrimary", true).findOne();
    }

    default CompanyModel createCompany(CompanyModel companyModel){

        try {
            companyModel.save();
            return getCompanyModel(companyModel);
        } catch (RuntimeException runtimeException){

            runtimeException.printStackTrace();
            return null;
        }
    }

    default CompanyModel updateCompany(CompanyModel companyModel){

        try {

            companyModel.update();
            return getCompanyModel(companyModel);
        } catch (RuntimeException runtimeException) {

            runtimeException.printStackTrace();
            return null;
        }
    }

    default CompanyModel deleteCompany(CompanyModel companyModel){

        try {

            companyModel.delete();
            if(getCompanyModel(companyModel) == null){
                return companyModel;
            }

            return null;
        } catch (RuntimeException runtimeException) {

            runtimeException.printStackTrace();
            return null;
        }
    }

    default CompanyModel getCompanyModel(CompanyModel companyModel) {
        return new CompanyModel().FINDER.query().where().eq("id", companyModel.id)
                .eq("user", companyModel.user).eq("companyName", companyModel.companyName)
                .eq("companyEmail", companyModel.companyEmail)
                .eq("companyPhone", companyModel.companyPhone)
                .eq("companyAddress", companyModel.companyAddress)
                .eq("companyCity", companyModel.companyCity)
                .eq("companyCountry", companyModel.companyCountry)
                .eq("companyPostalCode", companyModel.companyPostalCode)
                .eq("companyPostalCode", companyModel.companyPostalCode)
                .eq("taxNumber", companyModel.taxNumber).findOne();
    }
}
