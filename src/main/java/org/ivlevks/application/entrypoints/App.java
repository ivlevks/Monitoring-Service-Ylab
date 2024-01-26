package org.ivlevks.application.entrypoints;

import org.ivlevks.application.entrypoints.in.ConsoleHandler;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.initialize();
    }

}
