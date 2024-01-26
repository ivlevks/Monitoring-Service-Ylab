package org.ivlevks.application.entrypoints.in;

import org.ivlevks.application.core.usecase.Logic;
import org.ivlevks.application.dataproviders.repositories.InMemoryDataProvider;
import org.ivlevks.application.dataproviders.resources.InMemoryData;

import java.util.Scanner;

/**
 * Класс верхнего уровня
 * получает ввод с консоли, передает управление в Use Case.
 */
public class ConsoleHandlerIn {
    // сделать синглтон и засунуть в конструктор
    private final Logic logic = new Logic(new InMemoryDataProvider(new InMemoryData()));
    private final Scanner scanner = new Scanner(System.in);
    private String input = "";

    /**
     * Приветственное сообщение, старт принятия команд из консоли
     */
    public void initialize() {
        System.out.println("Добро пожаловать в Monitoring-Service. Это сервис, который управляет " +
                "вашими показаниями коммунальных услуг.");
        System.out.println("Перед началом управления показаниями необходимо авторизоваться " +
                "используя команду 'Авторизация'.");
        System.out.println("Если вы еще не зарегистрированы в системе " +
                "используйте команду 'Регистрация'.");
        System.out.println("Введите 'Помощь' для получения списка доступных команд.");
        System.out.println("Введите 'Выход' для выхода из сервиса.");

        startConsoleHandlerIn();
    }

    /**
     * Принятие команд из консоли, передача данных на следующий уровень Core
     */
    private void startConsoleHandlerIn() {
        while (!input.equalsIgnoreCase("Выход")) {
            input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("Регистрация")) {
                System.out.print("Введите имя пользователя: ");
                String name = scanner.nextLine();
                System.out.print("Введите пароль: ");
                String password = scanner.nextLine();
                System.out.print("Введите email: ");
                String email = scanner.nextLine();
                logic.registry(name, email, password, false);
            }

            if (input.equalsIgnoreCase("Авторизация")) {
                System.out.print("Введите email: ");
                String email = scanner.nextLine();
                System.out.print("Введите пароль: ");
                String password = scanner.nextLine();
                logic.auth(email, password);
            }

//            if (input.equalsIgnoreCase("Баланс")) {
//                infrastructure.showBalance();
//            }
//
//            if (input.equalsIgnoreCase("Снятие")) {
//                System.out.print("Введите идентификатор транзакции : ");
//                String transactionID = scanner.nextLine();
//                System.out.print("Введите сумму снятия: ");
//                String value = scanner.nextLine();
//                infrastructure.debitOperation(value, transactionID);
//            }
//
//            if (input.equalsIgnoreCase("Пополнение")) {
//                System.out.print("Введите идентификатор транзакции : ");
//                String transactionID = scanner.nextLine();
//                System.out.print("Введите сумму пополнения: ");
//                String value = scanner.nextLine();
//                infrastructure.creditOperation(value, transactionID);
//            }
//
//            if (input.equalsIgnoreCase("Смена пользователя")) {
//                infrastructure.logout();
//            }
//
//            if (input.equalsIgnoreCase("История")) {
//                infrastructure.showHistory();
//            }
//
//            if (input.equalsIgnoreCase("Помощь")) {
//                System.out.println("Список доступных команд:");
//                System.out.println("Регистрация");
//                System.out.println("Авторизация");
//                System.out.println("Баланс");
//                System.out.println("История");
//                System.out.println("Снятие");
//                System.out.println("Пополнение");
//                System.out.println("Смена пользователя");
//                System.out.println("Выход");
//            }
        }
    }
}
