package org.ivlevks.application.entrypoints;

import org.ivlevks.application.configuration.Audit;
import org.ivlevks.application.entrypoints.in.ConsoleHandler;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        Audit.addInfoInAudit("Start application");
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.initialize();
    }
}
