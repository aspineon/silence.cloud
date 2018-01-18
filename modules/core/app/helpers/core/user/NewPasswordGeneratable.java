package helpers.core.user;

import java.util.UUID;

public interface NewPasswordGeneratable {

    default String generateRandomPassword(){

        return UUID.randomUUID().toString();
    }
}
