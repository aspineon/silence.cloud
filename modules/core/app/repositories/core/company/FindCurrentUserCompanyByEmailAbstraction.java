package repositories.core.company;

import models.core.company.CompanyModel;
import models.core.user.UserModel;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface FindCurrentUserCompanyByEmailAbstraction {

    CompletionStage<Optional<CompanyModel>> findCurrentUserCompanyByEmail(UserModel userModel, String companyEmail);
}
