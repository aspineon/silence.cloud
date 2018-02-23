package repositories.core.company;

import models.core.company.CompanyModel;
import models.core.user.UserModel;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface FindAllCompaniesByUserAbstraction {

    CompletionStage<List<CompanyModel>> findAllCompaniesByUser(UserModel userModel);
}
