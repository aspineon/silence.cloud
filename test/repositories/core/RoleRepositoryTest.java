package repositories.core;

import helpers.BeforeAndAfterTest;
import models.core.RoleModel;
import org.junit.*;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class RoleRepositoryTest extends BeforeAndAfterTest {

    private Long activeStatusId = 1L;
    private Long inactiveStatusId = 2L;

    private Long officeRoleId = 1L;

    private Long notExistsRoleId = 100L;

    private Long notExistsStatusId = 100L;

    private String newRoleName = "newRole";
    private String updatedRole = "updatedRole";
    private String officeRoleName = "office";

    private String notExistsRoleName = "notExists";

    private int expectedSize = 13;
    private int emptyListSize = 0;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }


    @Test
    public void findAllRoles() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final RoleRepository roleRepository = app.injector().instanceOf(RoleRepository.class);
            CompletionStage<List<RoleModel>> stage = roleRepository.findAllRoles();

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(list -> {

                    return list.size() == expectedSize;
                });
            });
        });
    }

    @Test
    public void findAllRolesByNotExistsStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {
            final RoleRepository roleRepository = app.injector().instanceOf(RoleRepository.class);
            CompletionStage<List<RoleModel>> stage = roleRepository.findAllRolesByStatus(notExistsStatusId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(list -> {
                    return list.size() == emptyListSize;
                });
            });
        });
    }

    @Test
    public void findAllRolesByExistsStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final RoleRepository roleRepository = app.injector().instanceOf(RoleRepository.class);
            CompletionStage<List<RoleModel>> stage = roleRepository.findAllRolesByStatus(activeStatusId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(list -> {
                    return list.size() == expectedSize;
                });
            });
        });
    }

    @Test
    public void findRoleByNotExistsId() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final RoleRepository roleRepository = app.injector().instanceOf(RoleRepository.class);
            final CompletionStage<Optional<RoleModel>> stage = roleRepository.findRoleById(notExistsRoleId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(role -> {

                    return !role.isPresent();
                });
            });
        });
    }

    @Test
    public void findRoleByExistsId() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final RoleRepository roleRepository = app.injector().instanceOf(RoleRepository.class);
            final CompletionStage<Optional<RoleModel>> stage = roleRepository.findRoleById(officeRoleId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(role -> {

                    return role.isPresent() && role.get() != null;
                });
            });
        });
    }

    @Test
    public void findRoleByNotExistsName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final RoleRepository roleRepository = app.injector().instanceOf(RoleRepository.class);
            final CompletionStage<Optional<RoleModel>> stage = roleRepository.findRoleByName(notExistsRoleName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(role -> {

                    return !role.isPresent();
                });
            });
        });
    }

    @Test
    public void findRoleByExistsName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final RoleRepository roleRepository = app.injector().instanceOf(RoleRepository.class);
            final CompletionStage<Optional<RoleModel>> stage = roleRepository.findRoleByName(officeRoleName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(role -> {

                    return role.isPresent() && role.get() != null;
                });
            });
        });
    }

    @Test
    public void createRoleWithExistsName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final RoleRepository roleRepository = app.injector().instanceOf(RoleRepository.class);
            CompletionStage<Optional<RoleModel>> stage = roleRepository.createRole(officeRoleName, activeStatusId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(role -> {
                    return !role.isPresent();
                });
            });
        });
    }

    @Test
    public void createRoleWithNotExistsStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final RoleRepository roleRepository = app.injector().instanceOf(RoleRepository.class);
            final CompletionStage<Optional<RoleModel>> stage = roleRepository.createRole(newRoleName, notExistsStatusId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(role -> {

                    return !role.isPresent();
                });
            });
        });
    }

    @Test
    public void createNewRole() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final RoleRepository roleRepository = app.injector().instanceOf(RoleRepository.class);
            final CompletionStage<Optional<RoleModel>> stage = roleRepository.createRole(newRoleName, activeStatusId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(role -> {

                    return role.isPresent() && role.get() != null;
                });
            });
        });
    }

    @Test
    public void updateNotExistsRoleName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final RoleRepository roleRepository = app.injector().instanceOf(RoleRepository.class);
            final CompletionStage<Optional<RoleModel>> stage = roleRepository.updateRoleName(notExistsRoleId, officeRoleName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(role -> {

                    return !role.isPresent();
                });
            });
        });
    }

    @Test
    public void updateRoleWithExistsName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final RoleRepository roleRepository = app.injector().instanceOf(RoleRepository.class);
            final CompletionStage<Optional<RoleModel>> stage = roleRepository.updateRoleName(officeRoleId, officeRoleName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(role -> {

                    return !role.isPresent();
                });
            });
        });
    }

    @Test
    public void updateRoleName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final RoleRepository roleRepository = app.injector().instanceOf(RoleRepository.class);
            final CompletionStage<Optional<RoleModel>> stage = roleRepository.updateRoleName(officeRoleId, updatedRole);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(role -> {
                    return role.isPresent() && role.get() != null;
                });
            });
        });
    }

    @Test
    public void updateNotExistsRoleStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final RoleRepository roleRepository = app.injector().instanceOf(RoleRepository.class);
            final CompletionStage<Optional<RoleModel>> stage = roleRepository.updateRoleStatus(
                    notExistsRoleId, inactiveStatusId
            );

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(role -> {

                    return !role.isPresent();
                });
            });
        });
    }

    @Test
    public void updateRoleWithNotExistsStatus(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final RoleRepository roleRepository = app.injector().instanceOf(RoleRepository.class);
            final CompletionStage<Optional<RoleModel>> stage = roleRepository.updateRoleStatus(
                    officeRoleId, notExistsStatusId
            );

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(role -> {

                    return !role.isPresent();
                });
            });
        });
    }

    @Test
    public void updateRoleStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final RoleRepository roleRepository = app.injector().instanceOf(RoleRepository.class);
            final CompletionStage<Optional<RoleModel>> stage = roleRepository.updateRoleStatus(
                    officeRoleId, inactiveStatusId
            );

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(role -> {

                    return role.isPresent() && role.get() != null;
                });
            });
        });
    }

    @Test
    public void deleteRoleWithNotExistsId() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final RoleRepository roleRepository = app.injector().instanceOf(RoleRepository.class);
            final CompletionStage<Optional<RoleModel>> stage = roleRepository.deleteRole(notExistsRoleId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(role -> {

                    return role.isPresent() && role.get() != null;
                });
            });
        });
    }

    @Test
    public void deleteRole() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final RoleRepository roleRepository = app.injector().instanceOf(RoleRepository.class);
            final CompletionStage<Optional<RoleModel>> stage = roleRepository.deleteRole(officeRoleId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(role -> {

                    return !role.isPresent();
                });
            });
        });
    }
}