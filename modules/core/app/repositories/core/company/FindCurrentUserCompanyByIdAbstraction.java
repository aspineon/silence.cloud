package repositories.core.company;

import models.core.company.CompanyModel;
import models.core.user.UserModel;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface FindCurrentUserCompanyByIdAbstraction {

    CompletionStage<Optional<CompanyModel>> findCurrentUserCompanyById(UserModel userModel, Long companyId);
}
