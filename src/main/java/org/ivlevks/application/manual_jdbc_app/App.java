package org.ivlevks.application.manual_jdbc_app;

import org.ivlevks.adapter.controller.console.in.ConsoleHandler;
import org.ivlevks.adapter.repository.jdbc.IndicationRepositoryImpl;
import org.ivlevks.adapter.repository.jdbc.UserRepositoryImpl;
import org.ivlevks.configuration.Audit;
import org.ivlevks.configuration.DriverManager;
import org.ivlevks.configuration.PropertiesCache;
import org.ivlevks.configuration.migration.MigrationHelper;
import org.ivlevks.usecase.UseCaseIndications;
import org.ivlevks.usecase.UseCaseUsers;

import java.sql.Connection;

/**
 * Точка входа в приложение использующее jdbc
 *
 */
public class App {

    public static void main(String[] args) {
        Audit.addInfoInAudit("Start application");

        Connection connection = org.ivlevks.configuration.DriverManager.getConnection();
        MigrationHelper migrationHelper = new MigrationHelper(connection);

        UserRepositoryImpl userRepositoryImpl = new UserRepositoryImpl();
        IndicationRepositoryImpl indicationRepositoryImpl = new IndicationRepositoryImpl();

        UseCaseUsers useCaseUsers = new UseCaseUsers(userRepositoryImpl, indicationRepositoryImpl);
        UseCaseIndications useCaseIndications = new UseCaseIndications(userRepositoryImpl, indicationRepositoryImpl);

        ConsoleHandler consoleHandler = new ConsoleHandler(useCaseUsers, useCaseIndications);
        consoleHandler.initialize();
    }
}
