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
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class ModuleRepositoryTest extends BeforeAndAfterTest {

    private int expectedSize = 1;
    private int emptyListSize = 0;

    private String existsModuleName = "sampleModule";
    private String newModuleName = "newModule";
    private String updatedModuleName = "updatedModule";
    private String notExistsModuleName = "notExistsModule";

    private Long activeStatusId = 1L;
    private Long inactiveStatusId = 2L;


    private Long notExistsStatusId = 100L;

    private Long existsModuleId = 1L;
    private Long notExistsModuleId = 100L;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void findAllModules() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final ModuleRepository moduleRepository = app.injector().instanceOf(ModuleRepository.class);
            final CompletionStage<List<ModuleModel>> stage = moduleRepository.findAllModules();

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(list -> {

                    return list.size() == expectedSize;
                });
            });
        });
    }

    @Test
    public void findAllModulesByNotExistsStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final ModuleRepository moduleRepository = app.injector().instanceOf(ModuleRepository.class);
            final CompletionStage<List<ModuleModel>> stage = moduleRepository.findAllModulesByStatus(notExistsStatusId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(list -> {

                    return list.size() == emptyListSize;
                });
            });
        });
    }

    @Test
    public void findAllModulesByExistsStatusId() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final ModuleRepository moduleRepository = app.injector().instanceOf(ModuleRepository.class);
            final CompletionStage<List<ModuleModel>> stage = moduleRepository.findAllModulesByStatus(existsModuleId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(list -> {

                    return list.size() == expectedSize;
                });
            });
        });
    }

    @Test
    public void findModuleWithNotExitsStatusId() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final ModuleRepository moduleRepository = app.injector().instanceOf(ModuleRepository.class);
            final CompletionStage<Optional<ModuleModel>> stage = moduleRepository.findModuleById(notExistsModuleId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(module -> {

                    return !module.isPresent();
                });
            });
        });
    }

    @Test
    public void findModuleByExistsStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final ModuleRepository moduleRepository = app.injector().instanceOf(ModuleRepository.class);
            final CompletionStage<Optional<ModuleModel>> stage = moduleRepository.findModuleById(existsModuleId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(module -> {

                    return module.isPresent() && module.get() != null;
                });
            });
        });
    }

    @Test
    public void findModuleByNotExistsName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final ModuleRepository moduleRepository = app.injector().instanceOf(ModuleRepository.class);
            final CompletionStage<Optional<ModuleModel>> stage = moduleRepository.findModuleByName(notExistsModuleName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(module -> {

                    return !module.isPresent();
                });
            });
        });
    }

    @Test
    public void findModuleByExistsName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final ModuleRepository moduleRepository = app.injector().instanceOf(ModuleRepository.class);
            final CompletionStage<Optional<ModuleModel>> stage = moduleRepository.findModuleByName(existsModuleName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(module -> {

                    return module.isPresent() && module.get() != null;
                });
            });
        });
    }


    @Test
    public void createModuleWithExistsName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final ModuleRepository moduleRepository = app.injector().instanceOf(ModuleRepository.class);
            final CompletionStage<Optional<ModuleModel>> stage = moduleRepository
                    .createModule(existsModuleName, inactiveStatusId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(module -> {

                    return !module.isPresent();
                });
            });
        });
    }

    @Test
    public void createModuleWithNotExistsStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final ModuleRepository moduleRepository = app.injector().instanceOf(ModuleRepository.class);
            final CompletionStage<Optional<ModuleModel>> stage = moduleRepository
                    .createModule(newModuleName, notExistsStatusId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(module -> {

                    return !module.isPresent();
                });
            });
        });

    }

    @Test
    public void createNewModule() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final ModuleRepository moduleRepository = app.injector().instanceOf(ModuleRepository.class);
            final CompletionStage<Optional<ModuleModel>> stage = moduleRepository
                    .createModule(newModuleName, inactiveStatusId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(module -> {

                    return module.isPresent() && module.get() != null;
                });
            });
        });
    }

    @Test
    public void updateModuleNameNotExistsModuleId() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final ModuleRepository moduleRepository = app.injector().instanceOf(ModuleRepository.class);
            final CompletionStage<Optional<ModuleModel>> stage = moduleRepository
                    .updateModuleName(notExistsModuleId, updatedModuleName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(module -> {

                    return !module.isPresent();
                });
            });
        });
    }

    @Test
    public void updateModuleNameNotExistsName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final ModuleRepository moduleRepository = app.injector().instanceOf(ModuleRepository.class);
            final CompletionStage<Optional<ModuleModel>> stage = moduleRepository
                    .updateModuleName(existsModuleId, existsModuleName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(module -> {

                    return !module.isPresent();
                });
            });
        });
    }

    @Test
    public void updateModuleName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final ModuleRepository moduleRepository = app.injector().instanceOf(ModuleRepository.class);
            final CompletionStage<Optional<ModuleModel>> stage = moduleRepository
                    .updateModuleName(existsModuleId, updatedModuleName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(module -> {

                    return module.isPresent() && module.get() != null;
                });
            });
        });
    }

    @Test
    public void updateModuleStatusNotExistsModuleId() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final ModuleRepository moduleRepository = app.injector().instanceOf(ModuleRepository.class);
            final CompletionStage<Optional<ModuleModel>> stage = moduleRepository
                    .updateModuleStatus(notExistsModuleId, inactiveStatusId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(module -> {

                    return !module.isPresent();
                });
            });
        });
    }

    @Test
    public void updateModuleStatusNotExistsStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final ModuleRepository moduleRepository = app.injector().instanceOf(ModuleRepository.class);
            final CompletionStage<Optional<ModuleModel>> stage = moduleRepository
                    .updateModuleStatus(existsModuleId, notExistsStatusId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(module -> {

                    return !module.isPresent();
                });
            });
        });
    }

    @Test
    public void updateModuleStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final ModuleRepository moduleRepository = app.injector().instanceOf(ModuleRepository.class);
            final CompletionStage<Optional<ModuleModel>> stage = moduleRepository
                    .updateModuleStatus(existsModuleId, inactiveStatusId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(module -> {

                    return module.isPresent() && module.get() != null;
                });
            });
        });
    }

    @Test
    public void deleteModuleWithNotExistsModuleId() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final ModuleRepository moduleRepository = app.injector().instanceOf(ModuleRepository.class);
            final CompletionStage<Optional<ModuleModel>> stage = moduleRepository.deleteModule(notExistsModuleId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(module -> {

                    return module.isPresent() && module.get() != null;
                });
            });
        });
    }

    @Test
    public void deleteModuleWithExistsModuleId() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final ModuleRepository moduleRepository = app.injector().instanceOf(ModuleRepository.class);
            final CompletionStage<Optional<ModuleModel>> stage = moduleRepository.deleteModule(existsModuleId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(module -> {

                    return !module.isPresent();
                });
            });
        });
    }
}