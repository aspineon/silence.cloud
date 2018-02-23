package repositories.core.company;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.Transaction;
import models.core.company.CompanyModel;
import play.db.ebean.EbeanConfig;
import play.db.ebean.EbeanDynamicEvolutions;
import repositories.core.DatabaseExecutionContext;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class UpdateCurrentCompanyImplementation implements UpdateCurrentCompanyAbstraction {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;
    private final EbeanDynamicEvolutions ebeanDynamicEvolutions;

    @Inject
    public UpdateCurrentCompanyImplementation(
            EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext,
            EbeanDynamicEvolutions ebeanDynamicEvolutions
    ) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
        this.ebeanDynamicEvolutions = ebeanDynamicEvolutions;
    }

    @Override
    public CompletionStage<Optional<CompanyModel>> updateCompany(CompanyModel companyModel) {

        return supplyAsync(() -> {

            Transaction txn = ebeanServer.beginTransaction();
            Optional<CompanyModel> optionalCompany = Optional.empty();

            try {

                ebeanServer.update(companyModel);
                txn.commit();

                optionalCompany = ebeanServer.find(CompanyModel.class).where()
                        .eq("companyName", companyModel.companyName)
                        .eq("companyEmail", companyModel.companyEmail)
                        .eq("companyPhone", companyModel.companyPhone)
                        .eq("taxNumber", companyModel.taxNumber).findOneOrEmpty();
            } catch (Exception e) {

                e.printStackTrace();
            } finally {

                txn.end();
            }

            return optionalCompany;

        }, this.executionContext);
    }
}
