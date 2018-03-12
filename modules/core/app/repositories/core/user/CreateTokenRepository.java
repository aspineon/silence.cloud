package repositories.core.user;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
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

public class CreateTokenRepository {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;
    private final EbeanDynamicEvolutions ebeanDynamicEvolutions;

    @Inject
    public CreateTokenRepository(
            EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext,
            EbeanDynamicEvolutions ebeanDynamicEvolutions
    ) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
        this.ebeanDynamicEvolutions = ebeanDynamicEvolutions;
    }

    public CompletionStage<Optional<UserModel>> createToken(UserModel user){

        return supplyAsync(() -> {

            try {

                return tryToCreateModel(user);
            } catch (Exception e){

                Logger.error(e.getMessage(), e);
                return Optional.empty();
            }
        }, executionContext);
    }

    private Optional<UserModel> tryToCreateModel(UserModel user) {

        TokenModel tokenModel = createNewToken(user);
        ebeanServer.insert(tokenModel);

        Optional<TokenModel> optionalToken = ebeanServer.find(TokenModel.class).where()
                .eq("token", tokenModel.token).findOneOrEmpty();
        if(optionalToken.isPresent() && (optionalToken.get() != null)){

            return ebeanServer.find(UserModel.class).setId(user.id).findOneOrEmpty();
        }

        return Optional.empty();
    }

    private TokenModel createNewToken(UserModel user) {
        TokenModel tokenModel = new TokenModel();
        tokenModel.id = System.currentTimeMillis();
        tokenModel.createdAt = new Date();
        tokenModel.updatedAt = new Date();
        tokenModel.user = user;
        tokenModel.token = UUID.randomUUID().toString().replace("-", "")
                .substring(0, 9);
        return tokenModel;
    }
}
