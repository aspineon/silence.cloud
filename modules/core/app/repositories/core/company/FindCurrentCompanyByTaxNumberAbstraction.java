package repositories.core.company;

import models.core.company.CompanyModel;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface FindCurrentCompanyByTaxNumberAbstraction {

    CompletionStage<Optional<CompanyModel>> findCurrentCompanyByTaxNumber(String taxNumber);
}
