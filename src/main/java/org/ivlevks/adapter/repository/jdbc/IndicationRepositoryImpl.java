package org.ivlevks.adapter.repository.jdbc;

import org.ivlevks.configuration.ConnectionManager;
import org.ivlevks.configuration.DateTimeHelper;
import org.ivlevks.configuration.annotations.Loggable;
import org.ivlevks.domain.entity.Indication;
import org.ivlevks.domain.entity.User;
import org.ivlevks.service.port.IndicationsRepository;
import org.springframework.stereotype.Repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Имплементация интерфейса хранения показателей через JDBC
 */
@Loggable
@Repository
public class IndicationRepositoryImpl implements IndicationsRepository {
    private static final String GET_LIST_COUNTERS_NAME = "SELECT * FROM monitoring.counters";
    private static final String INSERT_INDICATIONS = "INSERT INTO monitoring.indications " +
            "(user_id, counter_id, value) VALUES (?, ?, ?)";
    private static final String INSERT_COUNTER = "INSERT INTO monitoring.counters " +
            "(name) VALUES (?)";
    private static final String GET_LAST_ACTUAL_INDICATIONS = "SELECT * FROM monitoring.indications WHERE user_id = ? " +
            "ORDER BY id DESC";
    private static final String GET_ALL_INDICATIONS = "SELECT * FROM monitoring.indications WHERE user_id = ? " +
            "ORDER BY id DESC";
    private final ConnectionManager connectionManager;

    public IndicationRepositoryImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Добавление показателей
     *
     * @param user       - пользователь, которому добавляются показания
     * @param indication - класс показаний
     */
    @Loggable
    @Override
    public void addIndication(User user, Indication indication) {
        Connection connection = connectionManager.getConnection();
        Set<String> listCounters = getListCounters();

        try (PreparedStatement insertIndicationStatement = connection.prepareStatement(INSERT_INDICATIONS)) {
            int userId = user.getId();
            int indexCounter = 1;
            for (String counter : listCounters) {
                insertIndicationStatement.setInt(1, userId);
                insertIndicationStatement.setInt(2, indexCounter++);
                insertIndicationStatement.setDouble(3, indication.getIndications().get(counter));
                insertIndicationStatement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Получение последних актуальных показаний
     *
     * @param user - пользователь, чьи показания выводятся
     * @return показания, обернутые в Optional
     */
    @Override
    public Optional<Indication> getLastActualIndication(User user) {
        Connection connection = connectionManager.getConnection();
        Indication lastActualIndication = null;
        HashMap<String, Double> indications = new HashMap<>();
        int indexCounters = 0;
        List<String> counters = new ArrayList<>(getListCounters());

        try (PreparedStatement getLastActualIndicationStatement = connection.prepareStatement(GET_LAST_ACTUAL_INDICATIONS)) {
            getLastActualIndicationStatement.setInt(1, user.getId());
            ResultSet resultSet = getLastActualIndicationStatement.executeQuery();

            while (resultSet.next() && indexCounters != 1) {
                LocalDateTime dateTime = DateTimeHelper.getDateTime(resultSet.getString("date_time"));

                indexCounters = resultSet.getInt("counter_id");
                String counter = counters.get(indexCounters - 1);

                indications.put(counter, resultSet.getDouble("value"));

                lastActualIndication = new Indication(dateTime, indications);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(lastActualIndication);
    }

    /**
     * Получение всех показаний пользователя
     *
     * @param user пользователь, чьи показания выводятся
     * @return список всех показаний
     */
    @Override
    public List<Indication> getAllIndications(User user) {
        Connection connection = connectionManager.getConnection();
        List<Indication> indications = new ArrayList<>();
        Indication indication = null;
        HashMap<String, Double> indicationsMap = new HashMap<>();
        int indexCounters = 0;
        List<String> counters = new ArrayList<>(getListCounters());

        try (PreparedStatement getAllIndicationStatement = connection.prepareStatement(GET_ALL_INDICATIONS)) {
            getAllIndicationStatement.setInt(1, user.getId());
            ResultSet resultSet = getAllIndicationStatement.executeQuery();

            while (resultSet.next()) {
                indexCounters = resultSet.getInt("counter_id");

                String counter = counters.get(indexCounters - 1);
                indicationsMap.put(counter, resultSet.getDouble("value"));

                if (indexCounters == 1) {
                    LocalDateTime dateTime = DateTimeHelper.getDateTime(resultSet.getString("date_time"));

                    indication = new Indication(dateTime, indicationsMap);
                    indications.add(indication);
                    indicationsMap = new HashMap<>();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return indications;
    }

    /**
     * Получение списка счетчиков
     *
     * @return список счетчиков
     */
    @Override
    public Set<String> getListCounters() {
        Connection connection = connectionManager.getConnection();
        Set<String> listCounters = new LinkedHashSet<>();

        try (PreparedStatement getListCountersStatement = connection.prepareStatement(GET_LIST_COUNTERS_NAME)) {
            ResultSet resultSet = getListCountersStatement.executeQuery();

            while (resultSet.next()) {
                String indication = resultSet.getString("name");
                listCounters.add(indication);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listCounters;
    }

    /**
     * Обновление списка наименований показаний
     * путем добавления новой строки
     *
     * @param newCounter новый счетчик
     */
    @Override
    public void updateListCounters(String newCounter) {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement insertCounterStatement = connection.prepareStatement(INSERT_COUNTER)) {
            insertCounterStatement.setString(1, newCounter);
            insertCounterStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
