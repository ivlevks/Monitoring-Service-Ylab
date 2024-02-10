package org.ivlevks.configuration;

import java.sql.Connection;
import java.sql.SQLException;

public class DriverManager {
    private static String URL = PropertiesCache.getInstance().getProperty("URL");
    private static String USER_NAME = PropertiesCache.getInstance().getProperty("username");
    private static String PASSWORD = PropertiesCache.getInstance().getProperty("password");

    public static Connection getConnection() {
        Connection connection;
        try {
            connection = java.sql.DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return connection;
    }
}
