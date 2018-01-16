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
 * Find user by email.
 */
public class FindUserByEmailRepository {

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
    public FindUserByEmailRepository (
            EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext,
            EbeanDynamicEvolutions ebeanDynamicEvolutions
    ) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
        this.ebeanDynamicEvolutions = ebeanDynamicEvolutions;
    }

    /**
     * @param email
     * @return UserModel userModel when success or null when failed
     */
    public CompletionStage<Optional<UserModel>> findUserByEmail(String email){

        return supplyAsync(() -> {

            Optional<UserModel> optionalUser = Optional.empty();

            if(email != null){

                optionalUser = ebeanServer.find(UserModel.class).where().eq("email", email.toLowerCase())
                        .findOneOrEmpty();
            }

            if(!optionalUser.isPresent() || optionalUser.get().id == null){

                optionalUser = Optional.empty();
            }

            return optionalUser;
        }, executionContext);
    }
}
