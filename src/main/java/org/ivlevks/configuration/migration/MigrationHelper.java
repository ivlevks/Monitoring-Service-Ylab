package org.ivlevks.configuration.migration;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.ivlevks.configuration.ConnectionManager;
import org.ivlevks.configuration.annotations.Loggable;

import java.sql.Connection;
import java.sql.PreparedStatement;

@Loggable
public class MigrationHelper {
    private final ConnectionManager connectionManager;
    private String DEFAULT_SCHEMA_MIGRATION = "CREATE SCHEMA IF NOT EXISTS migration";
    private String DEFAULT_SCHEMA_NAME;
    private String CHANGE_LOG_FILE;

    public MigrationHelper(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void migrate() {
        // без этой ручной загрузки драйвера при деплое в томкат
        // вылетает ошибка - No suitable driver found и миграции не накатываются
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement defaultSchemaStatement = connection.prepareStatement(DEFAULT_SCHEMA_MIGRATION)) {
            defaultSchemaStatement.executeUpdate();
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setLiquibaseSchemaName(DEFAULT_SCHEMA_NAME);
            Liquibase liquibase = new Liquibase(CHANGE_LOG_FILE, new ClassLoaderResourceAccessor(), database);
            liquibase.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDEFAULT_SCHEMA_NAME(String DEFAULT_SCHEMA_NAME) {
        this.DEFAULT_SCHEMA_NAME = DEFAULT_SCHEMA_NAME;
    }

    public void setCHANGE_LOG_FILE(String CHANGE_LOG_FILE) {
        this.CHANGE_LOG_FILE = CHANGE_LOG_FILE;
    }
}
