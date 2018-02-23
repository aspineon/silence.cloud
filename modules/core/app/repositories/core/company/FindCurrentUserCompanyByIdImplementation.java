package repositories.core.company;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import models.core.company.CompanyModel;
import models.core.user.UserModel;
import play.db.ebean.EbeanConfig;
import play.db.ebean.EbeanDynamicEvolutions;
import repositories.core.DatabaseExecutionContext;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class FindCurrentUserCompanyByIdImplementation implements FindCurrentUserCompanyByIdAbstraction {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;
    private final EbeanDynamicEvolutions ebeanDynamicEvolutions;

    @Inject
    public FindCurrentUserCompanyByIdImplementation(
            EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext,
            EbeanDynamicEvolutions ebeanDynamicEvolutions
    ) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
        this.ebeanDynamicEvolutions = ebeanDynamicEvolutions;
    }

    @Override
    public CompletionStage<Optional<CompanyModel>> findCurrentUserCompanyById(UserModel userModel, Long companyId) {

        return supplyAsync(
                () -> {
                    try {
                        return ebeanServer.find(CompanyModel.class).where().eq("user", userModel)
                                .eq("id", companyId).findOneOrEmpty();
                    } catch (Exception exception){

                        exception.printStackTrace();
                        return Optional.empty();
                    }
                }, executionContext
        );
    }
}
