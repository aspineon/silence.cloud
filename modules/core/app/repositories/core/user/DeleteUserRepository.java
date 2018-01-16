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
 * Delete current user.
 */
public class DeleteUserRepository {

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
    public DeleteUserRepository(
            EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext,
            EbeanDynamicEvolutions ebeanDynamicEvolutions
    ) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
        this.ebeanDynamicEvolutions = ebeanDynamicEvolutions;
    }

    /**
     * @param id
     * @return null when success or UserModel userModel when failed
     */
    public CompletionStage<Optional<UserModel>> deleteUser(Long id){

        return supplyAsync(() -> {

            try {

                UserModel userModel = ebeanServer.find(UserModel.class).setId(id).findOne();
                if(userModel == null){

                    return Optional.of(new UserModel());
                }
                ebeanServer.delete(userModel);

                return ebeanServer.find(UserModel.class).setId(id).findOneOrEmpty();
            } catch (Exception e) {

                e.printStackTrace();
                return Optional.of(new UserModel());
            }
        }, executionContext);
    }
}
