package repositories.core;

import models.core.StatusModel;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface StatusRepositoryInterface {

    CompletionStage<List<StatusModel>> getStatusesList();

    CompletionStage<Optional<StatusModel>> lookupStatus(Long id);

    CompletionStage<Optional<StatusModel>> getStatusByName(String name);

    CompletionStage<Optional<StatusModel>> insert(StatusModel statusModel);

    CompletionStage<Optional<StatusModel>> update(Long id, StatusModel newStatusModelData);

    CompletionStage<Optional<StatusModel>> delete(Long id);

}
