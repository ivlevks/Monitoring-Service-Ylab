package org.ivlevks.application.manual_app;

import org.ivlevks.configuration.Audit;
import org.ivlevks.usecase.UseCaseIndications;
import org.ivlevks.usecase.UseCaseUsers;
import org.ivlevks.adapter.repository.in_memory.InMemoryDataProvider;
import org.ivlevks.adapter.repository.in_memory.InMemoryData;
import org.ivlevks.adapter.controller.console.in.ConsoleHandler;

/**
 * Точка входа в приложение
 *
 */
public class App {

    public static void main(String[] args) {
        Audit.addInfoInAudit("Start application");

        InMemoryDataProvider dataProvider = new InMemoryDataProvider(new InMemoryData());

        UseCaseUsers useCaseUsers = new UseCaseUsers(dataProvider);
        UseCaseIndications useCaseIndications = new UseCaseIndications(dataProvider);

        ConsoleHandler consoleHandler = new ConsoleHandler(useCaseUsers, useCaseIndications);
        consoleHandler.initialize();
    }
}
