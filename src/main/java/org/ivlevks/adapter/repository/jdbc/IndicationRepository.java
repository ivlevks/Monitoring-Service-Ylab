package org.ivlevks.adapter.repository.jdbc;

import org.ivlevks.configuration.PropertiesCache;
import org.ivlevks.domain.entity.Indication;
import org.ivlevks.domain.entity.User;
import org.ivlevks.usecase.port.GetUpdateIndications;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class IndicationRepository implements GetUpdateIndications {

    private static final String URL = PropertiesCache.getInstance().getProperty("URL");
    private static final String USER_NAME = PropertiesCache.getInstance().getProperty("username");
    private static final String PASSWORD = PropertiesCache.getInstance().getProperty("password");
    private static final String GET_LIST_INDICATIONS_NAME = "SELECT * FROM information_schema.columns " +
            "WHERE table_schema = 'monitoring' AND table_name = 'indications'";
    private static final String ADD_NEW_INDICATION_NAME = "ALTER TABLE IF EXISTS monitoring.indications " +
            "ADD COLUMN IF NOT EXISTS ? double precision";

    /**
     * @param user       - пользователь, которому добавляются показания
     * @param indication - класс показаний
     */
    @Override
    public void addIndication(User user, Indication indication) {

    }

    /**
     * @param user - пользователь, чьи показания выводятся
     * @return
     */
    @Override
    public Optional<Indication> getLastActualIndication(User user) {
        return Optional.empty();
    }

    /**
     * @param user пользователь, чьи показания выводятся
     * @return
     */
    @Override
    public List<Indication> getAllIndications(User user) {
        return null;
    }

    /**
     * @return 
     */
    @Override
    public Set<String> getListIndications() {
        Set<String> listIndications = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
             PreparedStatement getListIndicationsStatement = connection.prepareStatement(GET_LIST_INDICATIONS_NAME)) {
            ResultSet resultSet = getListIndicationsStatement.executeQuery();

            while (resultSet.next()) {
                String indication = resultSet.getString("column_name");
                if (!indication.equals("id") && !indication.equals("username_id")
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
     * @param newNameIndication 
     */
    @Override
    public void updateListIndications(String newNameIndication) {
        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
             PreparedStatement updateListIndicationsStatement = connection.prepareStatement(ADD_NEW_INDICATION_NAME)) {
            updateListIndicationsStatement.setString(1, newNameIndication);
            updateListIndicationsStatement.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
