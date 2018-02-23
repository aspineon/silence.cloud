package repositories.core.company;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import models.core.company.CompanyModel;
import play.db.ebean.EbeanConfig;
import play.db.ebean.EbeanDynamicEvolutions;
import repositories.core.DatabaseExecutionContext;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class CreateNewCompanyImplementation implements CreateNewCompanyAbstraction {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;
    private final EbeanDynamicEvolutions ebeanDynamicEvolutions;

    /**
     * Constructor.
     *
     * @param ebeanConfig
     * @param executionContext
     * @param ebeanDynamicEvolutions
     */
    @Inject
    public CreateNewCompanyImplementation(
            EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext,
            EbeanDynamicEvolutions ebeanDynamicEvolutions
    ) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
        this.ebeanDynamicEvolutions = ebeanDynamicEvolutions;
    }

    @Override
    public CompletionStage<Optional<CompanyModel>> createNewCompany(CompanyModel companyModel) {

        return supplyAsync(
                ()-> {

                    try {

                        companyModel.save();
                        return ebeanServer.find(CompanyModel.class).where()
                                .eq("companyName", companyModel.companyName)
                                .eq("companyEmail", companyModel.companyEmail)
                                .eq("companyPhone", companyModel.companyPhone)
                                .eq("taxNumber", companyModel.taxNumber).findOneOrEmpty();
                    } catch (Exception exception) {

                        exception.printStackTrace();
                        return Optional.empty();
                    }
                }, this.executionContext
        );
    }
}
