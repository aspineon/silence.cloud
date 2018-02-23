package repositories.core.company;

import models.core.company.CompanyModel;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface FindCurrentCompanyByPhoneAbstraction {

    CompletionStage<Optional<CompanyModel>> findCurrentCompanyByPhone(String companyPhone);
}
