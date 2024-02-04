package org.ivlevks.adapter.repository.jdbc;

import org.ivlevks.configuration.DateTimeHelper;
import org.ivlevks.configuration.PropertiesCache;
import org.ivlevks.domain.entity.Indication;
import org.ivlevks.domain.entity.User;
import org.ivlevks.usecase.port.GetUpdateIndications;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Имплементация интерфейса хранения показателей через JDBC
 */
public class IndicationRepository implements GetUpdateIndications {
    private static final String URL = PropertiesCache.getInstance().getProperty("URL");
    private static final String USER_NAME = PropertiesCache.getInstance().getProperty("username");
    private static final String PASSWORD = PropertiesCache.getInstance().getProperty("password");
    private static final String GET_LIST_INDICATIONS_NAME = "SELECT * FROM information_schema.columns " +
            "WHERE table_schema = 'monitoring' AND table_name = 'indications'";
    private static final String INSERT_INDICATIONS = "INSERT INTO monitoring.indications " +
            "(user_id, heat, cold_water, hot_water) VALUES (?, ?, ?, ?)";
    private static final String GET_LAST_ACTUAL_INDICATIONS = "SELECT * FROM monitoring.indications WHERE user_id = ? " +
            "ORDER BY id DESC LIMIT 1";
    private static final String GET_ALL_INDICATIONS = "SELECT * FROM monitoring.indications WHERE user_id = ?";

    /**
     * Добавление показателей
     * @param user       - пользователь, которому добавляются показания
     * @param indication - класс показаний
     */
    @Override
    public void addIndication(User user, Indication indication) {
        Set<String> listIndications = getListIndications();

        // перечень показаний не расширялся (в DB 3 изначальных показания)
        if (listIndications.size() == 3) {
            try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
                 PreparedStatement insertIndicationStatement = connection.prepareStatement(INSERT_INDICATIONS)) {
                insertIndicationStatement.setInt(1, user.getId());
                insertIndicationStatement.setDouble(2, indication.getIndications().get("heat"));
                insertIndicationStatement.setDouble(3, indication.getIndications().get("cold_water"));
                insertIndicationStatement.setDouble(4, indication.getIndications().get("hot_water"));
                insertIndicationStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // перечень показаний расширен, колонок с показаниями больше чем 3
        } else {
            String UPDATED_INSERT_INDICATIONS = createUpdatedInsertIndicationsQuery(listIndications);

            try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
                 PreparedStatement insertUpdatedIndicationStatement = connection.prepareStatement(UPDATED_INSERT_INDICATIONS)) {
                insertUpdatedIndicationStatement.setInt(1, user.getId());
                insertUpdatedIndicationStatement.setDouble(2, indication.getIndications().get("heat"));
                insertUpdatedIndicationStatement.setDouble(3, indication.getIndications().get("cold_water"));
                insertUpdatedIndicationStatement.setDouble(4, indication.getIndications().get("hot_water"));

                int parameterIndex = 5;
                for (String newIndication : listIndications) {
                    if (!newIndication.equals("heat") && !newIndication.equals("cold_water") && !newIndication.equals("hot_water")) {
                        insertUpdatedIndicationStatement.setDouble(parameterIndex++, indication.getIndications().get(newIndication));
                    }
                }

                insertUpdatedIndicationStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Изменение запроса при добавлении новых показателей
     * @param listIndications список показаний
     * @return измененный запрос
     */
    private String createUpdatedInsertIndicationsQuery(Set<String> listIndications) {
        StringBuilder UPDATED_INSERT_INDICATIONS = new StringBuilder();

        UPDATED_INSERT_INDICATIONS.append("INSERT INTO monitoring.indications (user_id, heat, cold_water, hot_water");

        for (String indication : listIndications) {
            if (!indication.equals("heat") && !indication.equals("cold_water") && !indication.equals("hot_water")) {
                UPDATED_INSERT_INDICATIONS.append(", ");
                UPDATED_INSERT_INDICATIONS.append(indication);
            }
        }

        UPDATED_INSERT_INDICATIONS.append(") VALUES (?, ?, ?, ?");
        for (int i = 0; i < listIndications.size() - 3; i++) {
            UPDATED_INSERT_INDICATIONS.append(", ?");
        }
        UPDATED_INSERT_INDICATIONS.append(")");

        return UPDATED_INSERT_INDICATIONS.toString();
    }

    /**
     * Получение последних актуальных показаний
     * @param user - пользователь, чьи показания выводятся
     * @return показания, обернутые в Optional
     */
    @Override
    public Optional<Indication> getLastActualIndication(User user) {
        Indication lastActualIndication = null;
        HashMap<String, Double> indications = new HashMap<>();

        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
             PreparedStatement getLastActualIndicationStatement = connection.prepareStatement(GET_LAST_ACTUAL_INDICATIONS)) {
            getLastActualIndicationStatement.setInt(1, user.getId());
            ResultSet resultSet = getLastActualIndicationStatement.executeQuery();

            while (resultSet.next()) {
                LocalDateTime dateTime = DateTimeHelper.getDateTime(resultSet.getString("date_time"));
                for (String indication : getListIndications()) {
                    indications.put(indication, resultSet.getDouble(indication));
                }
                lastActualIndication = new Indication(dateTime, indications);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(lastActualIndication);
    }

    /**
     * Получение всех показаний пользователя
     * @param user пользователь, чьи показания выводятся
     * @return список всех показаний
     */
    @Override
    public List<Indication> getAllIndications(User user) {
        List<Indication> indications = new ArrayList<>();
        Indication indication = null;
        HashMap<String, Double> indicationsMap = null;

        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
             PreparedStatement getAllIndicationStatement = connection.prepareStatement(GET_ALL_INDICATIONS)) {
            getAllIndicationStatement.setInt(1, user.getId());
            ResultSet resultSet = getAllIndicationStatement.executeQuery();

            while (resultSet.next()) {
                LocalDateTime dateTime = DateTimeHelper.getDateTime(resultSet.getString("date_time"));
                indicationsMap = new HashMap<>();
                for (String currentIndication : getListIndications()) {
                    indicationsMap.put(currentIndication, resultSet.getDouble(currentIndication));
                }
                indication = new Indication(dateTime, indicationsMap);
                indications.add(indication);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return indications;
    }

    /**
     * Получение списка наименований показаний
     * @return спиок наименований показаний
     */
    @Override
    public Set<String> getListIndications() {
        Set<String> listIndications = new LinkedHashSet<>();

        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
             PreparedStatement getListIndicationsStatement = connection.prepareStatement(GET_LIST_INDICATIONS_NAME)) {
            ResultSet resultSet = getListIndicationsStatement.executeQuery();

            while (resultSet.next()) {
                String indication = resultSet.getString("column_name");
                if (!indication.equals("id") && !indication.equals("user_id")
                        && !indication.equals("date_time")) {
                    listIndications.add(indication);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listIndications;
    }

    /**
     * Обновление списка наименований показаний
     * путем добавления новых колонок с дефолтными занчениями 0
     * @param newNameIndication новое показание
     */
    @Override
    public void updateListIndications(String newNameIndication) {
        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
             PreparedStatement updateListIndicationsStatement = connection.prepareStatement("ALTER TABLE IF EXISTS " +
                     "monitoring.indications ADD COLUMN IF NOT EXISTS " + newNameIndication + " double precision " +
                     "DEFAULT 0")) {
            updateListIndicationsStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
