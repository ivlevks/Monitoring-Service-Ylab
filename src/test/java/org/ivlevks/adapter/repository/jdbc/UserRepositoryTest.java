package org.ivlevks.adapter.repository.jdbc;

import org.ivlevks.domain.entity.User;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

class UserRepositoryTest {
    private static final String CREATE_SCHEMA = "CREATE SCHEMA IF NOT EXISTS monitoring";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS monitoring.users (" +
            "id serial primary key," +
            "username varchar not null," +
            "email varchar not null," +
            "password varchar not null," +
            "admin boolean)";


    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine");
    Connection connection;
    UserRepository userRepository;

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
        createUsersTable();
        userRepository = new UserRepository(postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword());
    }

    private void createUsersTable() {
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
    }

    @AfterEach
    void tearDown() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addUser() {
        User user = new User("kostik", "ivlevks@yandex.ru" , "1234", false);
        userRepository.addUser(user);

        List<User> users = userRepository.getAllUsers();
        Assertions.assertEquals(1, users.size());
    }

    @Test
    void getUser() {
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void updateUser() {
    }
}