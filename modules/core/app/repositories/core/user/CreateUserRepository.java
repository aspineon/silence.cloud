package repositories.core.user;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import models.core.user.UserModel;
import play.db.ebean.EbeanConfig;
import play.db.ebean.EbeanDynamicEvolutions;
import repositories.core.DatabaseExecutionContext;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Create user test.
 */
public class CreateUserRepository {

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
    public CreateUserRepository(
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
    public CompletionStage<Optional<UserModel>> createUser(UserModel userModel){

        return supplyAsync(() -> {

            try {

                ebeanServer.insert(userModel);

                return ebeanServer.find(UserModel.class).where().eq("email", userModel.email.toLowerCase())
                        .eq("phone", userModel.phone).findOneOrEmpty();
            } catch (Exception e) {

                e.printStackTrace();
                return Optional.empty();
            }
        }, executionContext);
    }
}
