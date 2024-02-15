package org.ivlevks.configuration;

import org.ivlevks.configuration.annotations.Loggable;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Loggable
@Component
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

    public static String getURL() {
        return URL;
    }

    public static void setURL(String URL) {
        DriverManager.URL = URL;
    }

    public static String getUserName() {
        return USER_NAME;
    }

    public static void setUserName(String userName) {
        USER_NAME = userName;
    }

    public static String getPASSWORD() {
        return PASSWORD;
    }

    public static void setPASSWORD(String PASSWORD) {
        DriverManager.PASSWORD = PASSWORD;
    }
}
