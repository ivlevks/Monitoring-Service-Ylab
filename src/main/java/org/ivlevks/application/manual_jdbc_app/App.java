package org.ivlevks.application.manual_jdbc_app;

import org.ivlevks.adapter.controller.console.in.ConsoleHandler;
import org.ivlevks.adapter.repository.in_memory.InMemoryData;
import org.ivlevks.adapter.repository.in_memory.InMemoryDataProvider;
import org.ivlevks.configuration.Audit;
import org.ivlevks.configuration.migration.MigrationHelper;
import org.ivlevks.usecase.UseCaseIndications;
import org.ivlevks.usecase.UseCaseUsers;

/**
 * Точка входа в приложение
 *
 */
public class App {

    public static void main(String[] args) {
        Audit.addInfoInAudit("Start application");

        MigrationHelper migrationHelper = new MigrationHelper();

        InMemoryDataProvider dataProvider = new InMemoryDataProvider(new InMemoryData());

        UseCaseUsers useCaseUsers = new UseCaseUsers(dataProvider);
        UseCaseIndications useCaseIndications = new UseCaseIndications(dataProvider);

        ConsoleHandler consoleHandler = new ConsoleHandler(useCaseUsers, useCaseIndications);
        consoleHandler.initialize();
    }
}
