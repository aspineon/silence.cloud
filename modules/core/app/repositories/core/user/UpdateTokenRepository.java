package repositories.core.user;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.Transaction;
import models.core.user.TokenModel;
import models.core.user.UserModel;
import play.Logger;
import play.db.ebean.EbeanConfig;
import play.db.ebean.EbeanDynamicEvolutions;
import repositories.core.DatabaseExecutionContext;

import javax.inject.Inject;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class UpdateTokenRepository {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;
    private final EbeanDynamicEvolutions ebeanDynamicEvolutions;

    @Inject
    public UpdateTokenRepository(
            EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext,
            EbeanDynamicEvolutions ebeanDynamicEvolutions
    ) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
        this.ebeanDynamicEvolutions = ebeanDynamicEvolutions;
    }

    public CompletionStage<Optional<UserModel>> updateToken(UserModel user){

        return supplyAsync(() -> {

            Transaction txn = ebeanServer.beginTransaction();
            Optional<UserModel> optionalUser = Optional.empty();

            try {

                tryToUpdateToken(user, txn);

                optionalUser = ebeanServer.find(UserModel.class).setId(user.id).findOneOrEmpty();
            } catch (Exception e) {

                Logger.error(e.getMessage(), e);
            } finally {

                txn.end();
                return optionalUser;
            }

        }, executionContext);
    }

    private void tryToUpdateToken(UserModel user, Transaction txn) {
        if(user.otpToken != null) {
            updateExistsToken(user, txn);
        } else {

            createNewToken(user, txn);
        }
    }

    private void updateExistsToken(UserModel user, Transaction txn) {
        user.otpToken.token = UUID.randomUUID().toString().replace("-", "")
                .substring(0, 9);
        user.otpToken.updatedAt = new Date();
        user.updatedAt = new Date();
        ebeanServer.update(user);
        txn.commit();
    }

    private void createNewToken(UserModel user, Transaction txn) {
        TokenModel tokenModel = new TokenModel();
        tokenModel.id = System.currentTimeMillis();
        tokenModel.createdAt = new Date();
        tokenModel.updatedAt = new Date();
        tokenModel.token = UUID.randomUUID().toString().replace("-", "")
                .substring(0, 9);
        tokenModel.user = user;
        ebeanServer.insert(tokenModel);
        txn.commit();
    }
}
