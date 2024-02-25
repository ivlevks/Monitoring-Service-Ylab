package org.ivlevks.configuration;

import org.ivlevks.starter.annotations.Loggable;
import java.sql.Connection;
import java.sql.SQLException;

@Loggable
public class ConnectionManager {
    private String URL;
    private String USER_NAME;
    private String PASSWORD;

    public Connection getConnection() {
        Connection connection;
        try {
            connection = java.sql.DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getUserName() {
        return USER_NAME;
    }

    public void setUserName(String userName) {
        USER_NAME = userName;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }
}
