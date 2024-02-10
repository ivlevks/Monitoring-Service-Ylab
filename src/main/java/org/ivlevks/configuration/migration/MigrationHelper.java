package org.ivlevks.configuration.migration;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.ivlevks.configuration.PropertiesCache;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class MigrationHelper {

    private static final String DEFAULT_SCHEMA_NAME = PropertiesCache.getInstance().getProperty("default-schema-name");
    private static final String CHANGE_LOG_FILE = PropertiesCache.getInstance().getProperty("changeLogFile");
    private static final String DEFAULT_SCHEMA_MIGRATION = "CREATE SCHEMA IF NOT EXISTS migration";
    private final String URL;
    private final String USER_NAME;
    private final String PASSWORD;

    public MigrationHelper(String url, String userName, String password) {
        URL = url;
        USER_NAME = userName;
        PASSWORD = password;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
             PreparedStatement defaultSchemaStatement = connection.prepareStatement(DEFAULT_SCHEMA_MIGRATION)) {
            defaultSchemaStatement.executeUpdate();
            MigrationHelper.migrate(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void migrate(Connection connection) {
        try {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setLiquibaseSchemaName(DEFAULT_SCHEMA_NAME);
            Liquibase liquibase = new Liquibase(CHANGE_LOG_FILE, new ClassLoaderResourceAccessor(), database);
            liquibase.update();
        } catch (LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }
}
