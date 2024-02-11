package org.ivlevks.adapter.repository.jdbc;

import org.ivlevks.configuration.DriverManager;
import org.ivlevks.configuration.migration.MigrationHelper;
import org.ivlevks.domain.entity.Indication;
import org.ivlevks.domain.entity.User;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

// такая же история как и с UserRep..Test
class IndicationRepositoryImplTest {
    private static final String DROP_TABLE_USERS = "DROP TABLE monitoring.users";
    private static final String DROP_TABLE_INDICATIONS = "DROP TABLE monitoring.indications";
    private static final String DROP_TABLE_COUNTERS = "DROP TABLE monitoring.counters";
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine");
    private Connection connection;
    private IndicationRepositoryImpl indicationRepositoryImpl;
    private User user = new User(1, "kostik", "ivlevks@yandex.ru" , "1234", false);
    private Indication indication;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
        org.ivlevks.configuration.DriverManager.setURL(postgres.getJdbcUrl());
        org.ivlevks.configuration.DriverManager.setUserName(postgres.getUsername());
        org.ivlevks.configuration.DriverManager.setPASSWORD(postgres.getPassword());
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
        connection = DriverManager.getConnection();

        MigrationHelper migrationHelper = new MigrationHelper(connection);
        indicationRepositoryImpl = new IndicationRepositoryImpl();
    }

    @AfterEach
    void tearDown() {
        Connection connectionDropIndications = org.ivlevks.configuration.DriverManager.getConnection();
        try (PreparedStatement dropTableIndicationsStatement = connectionDropIndications.prepareStatement(DROP_TABLE_INDICATIONS)) {
            dropTableIndicationsStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Connection connectionDropUsers = org.ivlevks.configuration.DriverManager.getConnection();
        try (PreparedStatement dropTableUsersStatement = connectionDropUsers.prepareStatement(DROP_TABLE_USERS)) {
            dropTableUsersStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Connection connectionDropCounters = org.ivlevks.configuration.DriverManager.getConnection();
        try (PreparedStatement dropTableCountersStatement = connectionDropCounters.prepareStatement(DROP_TABLE_COUNTERS)) {
            dropTableCountersStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void addIndication() {
        HashMap<String, Double> correctIndications = new HashMap<>();
        correctIndications.put("Heat", 200d);
        correctIndications.put("ColdWater", 200d);
        correctIndications.put("HotWater", 200d);
        indication = new Indication(correctIndications);

        indicationRepositoryImpl.addIndication(user, indication);
        List<Indication> indications = indicationRepositoryImpl.getAllIndications(user);

        Assertions.assertEquals(2, indications.size());
    }

    @Test
    void getLastActualIndication() {
        Optional<Indication> lastActualIndication = indicationRepositoryImpl.getLastActualIndication(user);
        Assertions.assertEquals(100, lastActualIndication.get().getIndications().get("heat"));
        Assertions.assertEquals(100, lastActualIndication.get().getIndications().get("cold_water"));
        Assertions.assertEquals(100, lastActualIndication.get().getIndications().get("hot_water"));
    }

    @Test
    void getAllIndications() {
        int size = indicationRepositoryImpl.getAllIndications(user).size();

        Assertions.assertEquals(1, size);
    }

    @Test
    void getListIndications() {
        int size = indicationRepositoryImpl.getListCounters().size();

        Assertions.assertEquals(3, size);
    }

    @Test
    void updateListIndications() {
        indicationRepositoryImpl.updateListCounters("electricity");

        int size = indicationRepositoryImpl.getListCounters().size();
        Assertions.assertEquals(4, size);
    }
}