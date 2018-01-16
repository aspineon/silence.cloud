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
 * Find user by email and password repository.
 */
public class FindUserByEmailAndPasswordRepository {

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
    public FindUserByEmailAndPasswordRepository (
            EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext,
            EbeanDynamicEvolutions ebeanDynamicEvolutions
    ) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
        this.ebeanDynamicEvolutions = ebeanDynamicEvolutions;
    }

    /**
     * @param email
     * @param password
     * @return UserModel userModel when success or null when failed
     */
    public CompletionStage<Optional<UserModel>> findUserByEmailAndPassword(String email, String password) {

        return supplyAsync(() -> {

            try {

                if((email == null) || (password == null)){

                    return Optional.empty();
                }

                return ebeanServer.find(UserModel.class).where().eq("email", email.toLowerCase())
                        .eq("shaPassword", UserModel.getSha512(password)).findOneOrEmpty();
            } catch (Exception e) {

                e.printStackTrace();
                return Optional.empty();
            }
        }, executionContext);
    }
}
