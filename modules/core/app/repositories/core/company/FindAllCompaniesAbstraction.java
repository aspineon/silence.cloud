package repositories.core.company;

import models.core.company.CompanyModel;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface FindAllCompaniesAbstraction {

    CompletionStage<List<CompanyModel>> findAllCompanies();
}
