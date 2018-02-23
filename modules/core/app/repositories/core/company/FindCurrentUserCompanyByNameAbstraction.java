package repositories.core.company;

import models.core.company.CompanyModel;
import models.core.user.UserModel;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface FindCurrentUserCompanyByNameAbstraction {

    CompletionStage<Optional<CompanyModel>> findCurrentUserCompanyByName(UserModel userModel, String companyName);
}
