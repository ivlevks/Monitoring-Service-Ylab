package org.ivlevks.configuration.migration;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.ivlevks.configuration.DriverManager;
import org.ivlevks.configuration.PropertiesCache;
import org.ivlevks.configuration.annotations.Loggable;

import java.sql.Connection;
import java.sql.PreparedStatement;

@Loggable
public class MigrationHelper {

    private static final String DEFAULT_SCHEMA_NAME = PropertiesCache.getInstance().getProperty("default-schema-name");
    private static final String CHANGE_LOG_FILE = PropertiesCache.getInstance().getProperty("changeLogFile");
    private static final String DEFAULT_SCHEMA_MIGRATION = "CREATE SCHEMA IF NOT EXISTS migration";

    public static void migrate() {
        // без этой ручной загрузки драйвера при деплое в томкат
        // вылетает ошибка - No suitable driver found и миграции не накатываются
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = DriverManager.getConnection();
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
}
