package repositories.core;

import models.core.StatusModel;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface StatusRepositoryInterface {

    CompletionStage<Optional<StatusModel>> createStatusModel(String name);

    CompletionStage<Optional<StatusModel>> updateStatusModel(Long id, String name);

    CompletionStage<Optional<StatusModel>> deleteStatusModel(Long id);

    CompletionStage<List<StatusModel>> findAllStatuses();

    CompletionStage<Optional<StatusModel>> findStatusById(Long id);

    CompletionStage<Optional<StatusModel>> findStatusByName(String name);

}
