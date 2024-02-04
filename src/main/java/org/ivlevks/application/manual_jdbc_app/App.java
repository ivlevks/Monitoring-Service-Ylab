package org.ivlevks.application.manual_jdbc_app;

import org.ivlevks.adapter.controller.console.in.ConsoleHandler;
import org.ivlevks.adapter.repository.jdbc.IndicationRepository;
import org.ivlevks.adapter.repository.jdbc.UserRepository;
import org.ivlevks.configuration.Audit;
import org.ivlevks.configuration.PropertiesCache;
import org.ivlevks.configuration.migration.MigrationHelper;
import org.ivlevks.usecase.UseCaseIndications;
import org.ivlevks.usecase.UseCaseUsers;

/**
 * Точка входа в приложение использующее jdbc
 *
 */
public class App {

    public static void main(String[] args) {
        Audit.addInfoInAudit("Start application");
        String url = PropertiesCache.getInstance().getProperty("URL");
        String userName = PropertiesCache.getInstance().getProperty("username");
        String password = PropertiesCache.getInstance().getProperty("password");


        MigrationHelper migrationHelper = new MigrationHelper(url, userName, password);

        UserRepository userRepository = new UserRepository(url, userName, password);
        IndicationRepository indicationRepository = new IndicationRepository(url, userName, password);

        UseCaseUsers useCaseUsers = new UseCaseUsers(userRepository, indicationRepository);
        UseCaseIndications useCaseIndications = new UseCaseIndications(userRepository, indicationRepository);

        ConsoleHandler consoleHandler = new ConsoleHandler(useCaseUsers, useCaseIndications);
        consoleHandler.initialize();
    }
}
