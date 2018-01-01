package repositories.core;

import helpers.BeforeAndAfterTest;
import models.core.ModuleModel;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;

public class ModuleRepositoryTest extends BeforeAndAfterTest {

    private int firstExpectedSize = 1;
    private int secondExpectedSize = 2;

    private String existsModuleName = "sampleModule";
    private String newModuleName = "newModule";
    private String updatedModuleName = "updatedModule";

    private Long existsStatusId = 1L;
    private Long newStatusId = 2L;

    private Long existsModuleId = 1L;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void moduleRepository() throws Exception {
        final ModuleRepository moduleRepository = app.injector().instanceOf(ModuleRepository.class);

        final CompletionStage<List<ModuleModel>> findFirstAllModulesStage = moduleRepository.findAllModules();
        await().atMost(1, TimeUnit.SECONDS).until(() -> {
            assertThat(findFirstAllModulesStage.toCompletableFuture()).isCompletedWithValueMatching(list -> {
                return list.size() == firstExpectedSize;
            });
        });

        final CompletionStage<List<ModuleModel>> findFirstAllModulesByStatusStage = moduleRepository
                .findAllModulesByStatus(existsStatusId);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {
            assertThat(findFirstAllModulesByStatusStage.toCompletableFuture()).isCompletedWithValueMatching(list -> {
                return list.size() == firstExpectedSize;
            });
        });

        final CompletionStage<Optional<ModuleModel>> firstFindModuleByIdStage = moduleRepository
                .findModuleById(existsModuleId);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {
            assertThat(firstFindModuleByIdStage.toCompletableFuture()).isCompletedWithValueMatching(module -> {
                return module.isPresent() && module.get() != null;
            });
        });

        final CompletionStage<Optional<ModuleModel>> firstFindModuleByNameStage = moduleRepository
                .findModuleByName(existsModuleName);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {
            assertThat(firstFindModuleByNameStage.toCompletableFuture()).isCompletedWithValueMatching(module -> {
                return module.isPresent() && module.get() != null;
            });
        });

        final CompletionStage<Optional<ModuleModel>> createModuleStage = moduleRepository
                .createModule(newModuleName, existsStatusId);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {
            assertThat(createModuleStage.toCompletableFuture()).isCompletedWithValueMatching(module -> {
                return module.isPresent() && module.get() != null;
            });
        });

        final CompletionStage<List<ModuleModel>> secondFindAllModulesStage = moduleRepository.findAllModules();
        await().atMost(1, TimeUnit.SECONDS).until(() -> {
            assertThat(secondFindAllModulesStage.toCompletableFuture()).isCompletedWithValueMatching(list -> {
                return list.size() == secondExpectedSize;
            });
        });

        final CompletionStage<List<ModuleModel>> secondFindAllModulesByStatus = moduleRepository
                .findAllModulesByStatus(existsStatusId);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {
            assertThat(secondFindAllModulesByStatus.toCompletableFuture()).isCompletedWithValueMatching(list -> {
                return list.size() == 2;
            });
        });

        final CompletionStage<Optional<ModuleModel>> secondFindModuleByNameStage = moduleRepository
                .findModuleByName(newModuleName);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {
            assertThat(secondFindModuleByNameStage.toCompletableFuture()).isCompletedWithValueMatching(module -> {
                return module.isPresent() && module.get() != null;
            });
        });

        final CompletionStage<Optional<ModuleModel>> updateModuleNameStage = moduleRepository.updateModuleName(
                existsModuleId, updatedModuleName
        );
        await().atMost(1, TimeUnit.SECONDS).until(() -> {
            assertThat(updateModuleNameStage.toCompletableFuture()).isCompletedWithValueMatching(module -> {
                return module.isPresent() && module.get() != null;
            });
        });

        final CompletionStage<Optional<ModuleModel>> thirdFindModuleByNameStage = moduleRepository
                .findModuleByName(updatedModuleName);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {
            assertThat(thirdFindModuleByNameStage.toCompletableFuture()).isCompletedWithValueMatching(module -> {
                return module.isPresent() && module.get() != null;
            });
        });

        final CompletionStage<Optional<ModuleModel>> updateModuleStatusStage = moduleRepository
                .updateModuleStatus(existsModuleId, newStatusId);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {
            assertThat(updateModuleStatusStage.toCompletableFuture()).isCompletedWithValueMatching(module -> {
                return module.isPresent() && module.get() != null;
            });
        });

        final CompletionStage<List<ModuleModel>> thirdFindModuleByStatusStage = moduleRepository
                .findAllModulesByStatus(newStatusId);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {
            assertThat(thirdFindModuleByStatusStage.toCompletableFuture()).isCompletedWithValueMatching(list -> {
                return list.size() == 1;
            });
        });

        final CompletionStage<List<ModuleModel>> fourthFindModuleByStatusStage = moduleRepository
                .findAllModulesByStatus(existsStatusId);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {
            assertThat(thirdFindModuleByStatusStage.toCompletableFuture()).isCompletedWithValueMatching(list -> {
                return list.size() == 1;
            });
        });

        final CompletionStage<Optional<ModuleModel>> deleteModuleStage = moduleRepository.deleteModule(existsModuleId);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {
            assertThat(deleteModuleStage.toCompletableFuture()).isCompletedWithValueMatching(module -> {
                return module.isPresent() && module.get() != null;
            });
        });

        final CompletionStage<List<ModuleModel>> fourthfindAllModulesStage = moduleRepository.findAllModules();
        await().atMost(1, TimeUnit.SECONDS).until(() -> {
            assertThat(fourthfindAllModulesStage.toCompletableFuture()).isCompletedWithValueMatching(list -> {
                return list.size() == firstExpectedSize;
            });
        });
    }

}