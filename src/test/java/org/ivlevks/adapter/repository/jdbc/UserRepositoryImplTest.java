package org.ivlevks.adapter.repository.jdbc;

import org.ivlevks.domain.entity.User;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

class UserRepositoryImplTest {
    private static final String CREATE_SCHEMA = "CREATE SCHEMA IF NOT EXISTS monitoring";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS monitoring.users (" +
            "id serial primary key," +
            "username varchar not null," +
            "email varchar not null," +
            "password varchar not null," +
            "admin boolean)";
    private static final String POPULATE_TABLE = "INSERT INTO monitoring.users (" +
            "username, email, password, admin) VALUES('admin', 'admin@yandex.ru', '12345', 'true')";
    private static final String DROP_TABLE = "DROP TABLE monitoring.users";
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine");
    private User user;
    private Connection connection;
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
        try {
            connection = DriverManager.getConnection(
                    postgres.getJdbcUrl(),
                    postgres.getUsername(),
                    postgres.getPassword());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        createPopulateUsersTable(connection);
        userRepositoryImpl = new UserRepositoryImpl();
    }

    public static void createPopulateUsersTable(Connection connection) {
        try (PreparedStatement createSchemaStatement = connection.prepareStatement(CREATE_SCHEMA)) {
            createSchemaStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (PreparedStatement createTableStatement = connection.prepareStatement(CREATE_TABLE)) {
            createTableStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (PreparedStatement populateTableStatement = connection.prepareStatement(POPULATE_TABLE)) {
            populateTableStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        try (PreparedStatement createTableStatement = connection.prepareStatement(DROP_TABLE)) {
            createTableStatement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void addUser() {
        user = new User("kostik", "ivlevks@yandex.ru" , "1234", false);
        userRepositoryImpl.addUser(user);

        List<User> users = userRepositoryImpl.getAllUsers();
        Assertions.assertEquals(2, users.size());
    }

    @Test
    void getUser() {
        Optional<User> user = userRepositoryImpl.getUser("admin@yandex.ru");
        Assertions.assertEquals("12345", user.get().getPassword());
    }

    @Test
    void getAllUsers() {
        List<User> users = userRepositoryImpl.getAllUsers();
        Assertions.assertEquals(1, users.size());
    }

    @Test
    void updateUser() {
        user = new User("admin", "admin@yandex.ru" , "12345", false);
        userRepositoryImpl.updateUser(user);

        Optional<User> updatedUser = userRepositoryImpl.getUser("admin@yandex.ru");
        Assertions.assertEquals(false, updatedUser.get().isUserAdmin());
    }
}