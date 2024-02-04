package org.ivlevks.adapter.repository.jdbc;

import org.ivlevks.domain.entity.Indication;
import org.ivlevks.domain.entity.User;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class IndicationRepositoryTest {
    private static final String CREATE_SCHEMA = "CREATE SCHEMA IF NOT EXISTS monitoring";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS monitoring.indications (" +
            "id serial primary key," +
            "user_id integer," +
            "date_time timestamp DEFAULT NOW()," +
            "heat DOUBLE PRECISION," +
            "cold_water DOUBLE PRECISION," +
            "hot_water DOUBLE PRECISION)";
    private static final String POPULATE_TABLE = "INSERT INTO monitoring.indications (" +
            "user_id, heat, cold_water, hot_water) VALUES('1', '100', '100', '100')";
    private static final String DROP_TABLE = "DROP TABLE monitoring.indications";
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine");
    private Connection connection;
    private IndicationRepository indicationRepository;
    private User user = new User(1, "kostik", "ivlevks@yandex.ru" , "1234", false);
    private Indication indication;

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
        indicationRepository = new IndicationRepository(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword());
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
    void addIndication() {
        HashMap<String, Double> correctIndications = new HashMap<>();
        correctIndications.put("heat", 200d);
        correctIndications.put("cold_water", 200d);
        correctIndications.put("hot_water", 200d);
        indication = new Indication(correctIndications);

        indicationRepository.addIndication(user, indication);
        List<Indication> indications = indicationRepository.getAllIndications(user);

        Assertions.assertEquals(2, indications.size());
    }

    @Test
    void getLastActualIndication() {
        Optional<Indication> lastActualIndication = indicationRepository.getLastActualIndication(user);
        Assertions.assertEquals(100, lastActualIndication.get().getIndications().get("heat"));
        Assertions.assertEquals(100, lastActualIndication.get().getIndications().get("cold_water"));
        Assertions.assertEquals(100, lastActualIndication.get().getIndications().get("hot_water"));
    }

    @Test
    void getAllIndications() {
        int size = indicationRepository.getAllIndications(user).size();

        Assertions.assertEquals(1, size);
    }

    @Test
    void getListIndications() {
        int size = indicationRepository.getListIndications().size();

        Assertions.assertEquals(3, size);
    }

    @Test
    void updateListIndications() {
        indicationRepository.updateListIndications("electricity");

        int size = indicationRepository.getListIndications().size();
        Assertions.assertEquals(4, size);
    }
}