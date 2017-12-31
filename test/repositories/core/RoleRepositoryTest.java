package repositories.core;

import com.google.common.collect.ImmutableMap;
import models.core.RoleModel;
import models.core.RoleModelCrud;
import models.core.StatusModel;
import org.junit.*;
import play.Application;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.Evolutions;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class RoleRepositoryTest extends WithApplication implements RoleModelCrud {

    private static Database database;

    private static final Long firstStatusId = 100L;
    private static final Long secondStatusId = 101L;

    private final String newRoleName = "newRole";
    private final String updatedRoleName = "updatedRole";

    private static final String firstStatusName = "firstStatus";
    private static final String secondStatusName = "secondStatus";

    private int expectedSize = 1;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @BeforeClass
    public static void setUp() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            database = Databases.inMemory(
                    "mydatabase",
                    ImmutableMap.of(
                            "MODE", "MYSQL"
                    ),
                    ImmutableMap.of(
                            "logStatements", true
                    )
            );
            Evolutions.applyEvolutions(database);

            StatusModel statusModel = new StatusModel();
            statusModel.id = firstStatusId;
            statusModel.name = firstStatusName;
            statusModel.createdAt = new Date();
            statusModel.updateAt = new Date();
            statusModel.save();

            StatusModel statusModel2 = new StatusModel();
            statusModel.id = secondStatusId;
            statusModel.name = secondStatusName;
            statusModel.createdAt = new Date();
            statusModel.updateAt = new Date();
            statusModel.save();
        });
    }

    @Test
    public void roleRepositoryTest() throws Exception {
        final RoleRepository roleRepository = app.injector().instanceOf(RoleRepository.class);

        final CompletionStage<Optional<RoleModel>> createStage = roleRepository.createRole(newRoleName, firstStatusId);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {
            assertThat(createStage.toCompletableFuture()).isCompletedWithValueMatching(role -> {
                return role.isPresent() && role.get() != null;
            });
        });

        final CompletionStage<List<RoleModel>> findAllRolesStage = roleRepository.findAllRoles();
        await().atMost(1, TimeUnit.SECONDS).until(() -> {
            assertThat(findAllRolesStage.toCompletableFuture()).isCompletedWithValueMatching(roles -> {
                return roles.size() == expectedSize;
            });
        });

        final CompletionStage<List<RoleModel>> findAllRolesByStatus = roleRepository.findAllRolesByStatus(firstStatusId);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {
            assertThat(findAllRolesByStatus.toCompletableFuture()).isCompletedWithValueMatching(roles -> {
                return roles.size() == expectedSize;
            });
        });

        final CompletionStage<Optional<RoleModel>> roleByNameStage = roleRepository.findRoleByName(newRoleName);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {
            assertThat(roleByNameStage.toCompletableFuture()).isCompletedWithValueMatching(role -> {
                return role.isPresent() && role.get() != null;
            });
        });

        final RoleModel existsRole = RoleModelCrud.super.findRoleByName(newRoleName);

        final CompletionStage<Optional<RoleModel>> roleByIdStage = roleRepository.findRoleById(existsRole.id);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {
            assertThat(roleByIdStage.toCompletableFuture()).isCompletedWithValueMatching(role -> {
                return role.isPresent() && role.get() != null;
            });
        });

        final CompletionStage<Optional<RoleModel>> updateRoleNameStage = roleRepository.updateRoleName(
                existsRole.id, updatedRoleName
        );
        await().atMost(1, TimeUnit.SECONDS).until(() -> {
            assertThat(updateRoleNameStage.toCompletableFuture()).isCompletedWithValueMatching(role -> {
                return role.isPresent() && role.get() != null;
            });
        });

        final CompletionStage<Optional<RoleModel>> updateRoleStatusStage = roleRepository.updateRoleStatus(
                existsRole.id, secondStatusId
        );
        await().atMost(1, TimeUnit.SECONDS).until(() -> {
            assertThat(updateRoleStatusStage.toCompletableFuture()).isCompletedWithValueMatching(role -> {
                return role.isPresent() && role.get() != null;
            });
        });

        final CompletionStage<Optional<RoleModel>> deleteRoleStage = roleRepository.deleteRole(existsRole.id);
        await().atMost(1, TimeUnit.SECONDS).until(() -> {
            assertThat(deleteRoleStage.toCompletableFuture()).isCompletedWithValueMatching(role -> {
                return !role.isPresent();
            });
        });
    }

    @AfterClass
    public static void tearDown() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            List<RoleModel> roles = RoleModel.FINDER.all();
            for(RoleModel role: roles){
                role.delete();
            }

            List<StatusModel> statuses  = StatusModel.FINDER.all();
            for (StatusModel status: statuses){
                status.delete();
            }

            Evolutions.cleanupEvolutions(database);
            database.shutdown();
        });
    }
}