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
 * Find user by phone.
 */
public class FindUserByPhoneRepository {

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
    public FindUserByPhoneRepository (
            EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext,
            EbeanDynamicEvolutions ebeanDynamicEvolutions
    ) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
        this.ebeanDynamicEvolutions = ebeanDynamicEvolutions;
    }

    /**
     * @param phone
     * @return UserModel userModel when success or null when failed
     */
    public CompletionStage<Optional<UserModel>> findUserByPhone(String phone) {

        return supplyAsync(() -> {
            try {

                if(phone == null){

                    return Optional.empty();
                }
                return ebeanServer.find(UserModel.class).where().eq("phone", phone).findOneOrEmpty();
            } catch (Exception e) {

                e.printStackTrace();
                return Optional.empty();
            }
        }, executionContext);
    }

}
