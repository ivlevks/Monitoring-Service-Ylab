package org.ivlevks.application.entrypoints;

import org.ivlevks.application.entrypoints.in.ConsoleHandlerIn;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        ConsoleHandlerIn consoleHandlerIn = new ConsoleHandlerIn();
        consoleHandlerIn.initialize();
    }

}
