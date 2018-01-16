package repositories.core.user;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.Transaction;
import models.core.user.UserModel;
import play.db.ebean.EbeanConfig;
import play.db.ebean.EbeanDynamicEvolutions;
import repositories.core.DatabaseExecutionContext;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Update current user
 */
public class UpdateUserRepository {

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
    public UpdateUserRepository(
            EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext,
            EbeanDynamicEvolutions ebeanDynamicEvolutions
    ) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
        this.ebeanDynamicEvolutions = ebeanDynamicEvolutions;
    }

    /**
     * @param userModel
     * @return UserModel userModel when success or null when failed
     */
    public CompletionStage<Optional<UserModel>> updateUser(UserModel userModel){

        return supplyAsync(() -> {

            Transaction txn = ebeanServer.beginTransaction();
            Optional<UserModel> optionalUser = Optional.empty();

            try {

                ebeanServer.update(userModel);
                txn.commit();

                optionalUser = ebeanServer.find(UserModel.class).where()
                        .eq("email", userModel.email.toLowerCase())
                        .eq("phone", userModel.phone).findOneOrEmpty();
            } catch (Exception e) {

                e.printStackTrace();
            } finally {

                txn.end();
            }

            return optionalUser;

        }, executionContext);
    }
}
