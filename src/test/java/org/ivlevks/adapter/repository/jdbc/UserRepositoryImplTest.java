package org.ivlevks.adapter.repository.jdbc;

import org.ivlevks.configuration.ConnectionManager;
import org.ivlevks.configuration.migration.MigrationHelper;
import org.ivlevks.domain.entity.User;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

// тесты все вместе так и не завелись корректно (поодиночке работают),
// скорее всего дропать нужно через liqubase
class UserRepositoryImplTest {
    private static final String DROP_TABLE_USERS = "DROP TABLE monitoring.users";
    private static final String DROP_TABLE_INDICATIONS = "DROP TABLE monitoring.indications";
    private static final String DROP_TABLE_COUNTERS = "DROP TABLE monitoring.counters";
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine");
    private User user;
    private ConnectionManager connectionManager;
    private UserRepositoryImpl userRepositoryImpl;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
//        MigrationHelper.migrate();
    }

    @AfterEach
    void tearDown() {
        Connection connectionDropIndications = connectionManager.getConnection();
        try (PreparedStatement dropTableIndicationsStatement = connectionDropIndications.prepareStatement(DROP_TABLE_INDICATIONS)) {
            dropTableIndicationsStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Connection connectionDropUsers = connectionManager.getConnection();
        try (PreparedStatement dropTableUsersStatement = connectionDropUsers.prepareStatement(DROP_TABLE_USERS)) {
            dropTableUsersStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Connection connectionDropCounters = connectionManager.getConnection();
        try (PreparedStatement dropTableCountersStatement = connectionDropCounters.prepareStatement(DROP_TABLE_COUNTERS)) {
            dropTableCountersStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void addUser() {
        user = new User("julia", "julia@yandex.ru" , "12345", false);
        userRepositoryImpl.addUser(user);

        List<User> users = userRepositoryImpl.getAllUsers();
        Assertions.assertEquals(3, users.size());
    }

    @Test
    void getUser() {
        Optional<User> user = userRepositoryImpl.getUserByEmail("admin@yandex.ru");
        Assertions.assertEquals("12345", user.get().getPassword());
    }

    @Test
    void getAllUsers() {
        List<User> users = userRepositoryImpl.getAllUsers();
        Assertions.assertEquals(2, users.size());
    }

    @Test
    void updateUser() {
        user = new User("admin", "admin@yandex.ru" , "12345", false);
        userRepositoryImpl.updateUser(user);

        Optional<User> updatedUser = userRepositoryImpl.getUserByEmail("admin@yandex.ru");
        Assertions.assertEquals(false, updatedUser.get().isUserAdmin());
    }
}