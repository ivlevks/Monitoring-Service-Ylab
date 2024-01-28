package org.ivlevks.application.presentation;

import org.ivlevks.application.configuration.Audit;
import org.ivlevks.application.core.usecase.UseCaseIndications;
import org.ivlevks.application.core.usecase.UseCaseUsers;
import org.ivlevks.application.dataproviders.repositories.InMemoryDataProvider;
import org.ivlevks.application.dataproviders.resources.InMemoryData;
import org.ivlevks.application.presentation.in.ConsoleHandler;

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
