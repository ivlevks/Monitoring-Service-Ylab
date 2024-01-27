package org.ivlevks.application.entrypoints.in;

import org.ivlevks.application.configuration.Audit;
import org.ivlevks.application.core.entity.Indication;
import org.ivlevks.application.core.entity.User;
import org.ivlevks.application.core.usecase.Logic;
import org.ivlevks.application.dataproviders.repositories.InMemoryDataProvider;
import org.ivlevks.application.dataproviders.resources.InMemoryData;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

/**
 * Класс верхнего уровня
 * получает ввод с консоли, передает управление в Use Case.
 */
public class ConsoleHandler {
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
        Audit.addInfoInAudit("Show welcome message");

        logic.registry("Костик", "ивл", "123", false);
        HashMap<String, Double> indications = new HashMap<>();
        indications.put("Heat", 100d);
        indications.put("Cold Water", 100d);
        indications.put("Hot Water", 100d);
        logic.auth("ивл", "123");
        logic.updateIndication(indications);
        logic.exit();

        logic.registry("Админ", "адм", "12345", true);
        logic.auth("адм", "12345");
        logic.addNewNameIndication("Новое показание");
//        logic.exit();
//
//        logic.auth("ивл", "123");

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
                System.out.print("Введите email: ");
                String email = scanner.nextLine();
                System.out.print("Введите пароль: ");
                String password = scanner.nextLine();
                logic.registry(name, email, password, false);
            }

            if (input.equalsIgnoreCase("Авторизация")) {
                System.out.print("Введите email: ");
                String email = scanner.nextLine();
                System.out.print("Введите пароль: ");
                String password = scanner.nextLine();
                logic.auth(email, password);
            }

            if (input.equalsIgnoreCase("Ввод показаний")) {
                HashMap<String, Double> indications = new HashMap<>();
                for (String nameIndication : logic.getNameIndications()) {
                    System.out.print("Введите показания " + nameIndication + " ");
                    Double value = Double.valueOf(scanner.nextLine());
                    indications.put(nameIndication, value);
                }
                logic.updateIndication(indications);
            }

            if (input.equalsIgnoreCase("Получить актуальные показания")) {
                Optional<Indication> lastActualIndication = logic.getLastActualIndicationUser();
                if (lastActualIndication.isPresent()) {
                    for (Map.Entry<String, Double> entry : lastActualIndication.get().getIndications().entrySet()) {
                        System.out.println(entry.getKey() + "   " + entry.getValue());
                    }
                } else {
                    System.out.println("Актуальные показания отсутствуют");
                }
                System.out.println();
            }

            if (input.equalsIgnoreCase("Показать историю показаний")) {
                if (logic.getAllIndicationsUser().isEmpty()) {
                    System.out.println("История показаний отсутствует");
                } else {
                    for (Indication indication : logic.getAllIndicationsUser()) {
                        System.out.println("Показания на " + indication.getDateTime() + " ");
                        for (Map.Entry<String, Double> entry : indication.getIndications().entrySet()) {
                            System.out.println(entry.getKey() + "   " + entry.getValue());
                        }
                        System.out.println();
                    }
                }
            }

            if (input.equalsIgnoreCase("Показать показания за месяц")) {
                System.out.println("Введите год за который хотите просмотреть показания в формате YYYY");
                int year = Integer.parseInt(scanner.nextLine());
                System.out.println("Введите номер месяца за который хотите просмотреть показания:");
                int month = Integer.parseInt(scanner.nextLine());

                System.out.println("Показания на " + month + " месяц " + year + " года:");
                for (Indication indication : logic.getAllIndicationsUser()) {
                    if (indication.getDateTime().getYear() == year &&
                            indication.getDateTime().getMonthValue() == month) {
                        for (Map.Entry<String, Double> entry : indication.getIndications().entrySet()) {
                            System.out.println(entry.getKey() + "   " + entry.getValue());
                        }
                    } else {
                        System.out.println("Показания на " + month + " месяц " + year + " года отсутствуют");
                    }
                    System.out.println();
                }
            }

            //для Админа
            if (input.equalsIgnoreCase("Показать историю показаний пользователя")) {
                if (!logic.isCurrentUserAdmin()) {
                    System.out.println("Ошибка, Вы не являетесь администратором!");
                } else {
                    System.out.println("Введите email пользователя:");
                    String email = scanner.nextLine();
                    Optional<User> user = logic.findUserByEmail(email);
                    if (user.isEmpty()) {
                        System.out.println("Такого пользователя не существует");
                    } else {
                        if (logic.getAllIndicationsUser(user.get()).isEmpty()) {
                            System.out.println("История показаний отсутствует");
                        } else {
                            for (Indication indication : logic.getAllIndicationsUser(user.get())) {
                                System.out.println("Показания на " + indication.getDateTime() + " ");
                                for (Map.Entry<String, Double> entry : indication.getIndications().entrySet()) {
                                    System.out.println(entry.getKey() + "   " + entry.getValue());
                                }
                                System.out.println();
                            }
                        }
                    }
                }
            }

            //для Админа
            if (input.equalsIgnoreCase("Показать историю показаний пользователя за месяц")) {
                if (!logic.isCurrentUserAdmin()) {
                    System.out.println("Ошибка, Вы не являетесь администратором!");
                } else {
                    System.out.println("Введите email пользователя:");
                    String email = scanner.nextLine();
                    Optional<User> user = logic.findUserByEmail(email);
                    if (user.isEmpty()) {
                        System.out.println("Такого пользователя не существует");
                    } else {
                        System.out.println("Введите год за который хотите просмотреть показания в формате YYYY");
                        int year = Integer.parseInt(scanner.nextLine());
                        System.out.println("Введите номер месяца за который хотите просмотреть показания:");
                        int month = Integer.parseInt(scanner.nextLine());

                        System.out.println("Показания на " + month + " месяц " + year + " года:");
                        for (Indication indication : logic.getAllIndicationsUser(user.get())) {
                            if (indication.getDateTime().getYear() == year &&
                                    indication.getDateTime().getMonthValue() == month) {
                                for (Map.Entry<String, Double> entry : indication.getIndications().entrySet()) {
                                    System.out.println(entry.getKey() + "   " + entry.getValue());
                                }
                            } else {
                                System.out.println("Показания на " + month + " месяц " + year + " года отсутствуют");
                            }
                            System.out.println();
                        }
                    }
                }
            }

            //для Админа
            if (input.equalsIgnoreCase("Расширить перечень показаний")) {
                if (!logic.isCurrentUserAdmin()) {
                    System.out.println("Ошибка, Вы не являетесь администратором!");
                } else {
                    System.out.println("Введите наименование нового показания:");
                    String newNameIndication = scanner.nextLine();

                    logic.addNewNameIndication(newNameIndication);
                    System.out.println("Новый вид показания " + newNameIndication + " добавлен!" );
                }
            }

            //для Админа
            if (input.equalsIgnoreCase("Изменить права пользователя")) {
                if (!logic.isCurrentUserAdmin()) {
                    System.out.println("Ошибка, Вы не являетесь администратором!");
                } else {
                    System.out.println("Введите email пользователя, у которого необходимо изменить права доступа:");
                    String email = scanner.nextLine();
                    Optional<User> user = logic.findUserByEmail(email);
                    if (user.isEmpty()) {
                        System.out.println("Такого пользователя не существует");
                    } else {
                        System.out.println("Необходимо добавить права администратора (введите Да), или убрать их" +
                                "(введите Нет)");
                        String access = scanner.nextLine();
                        boolean result = false;
                        if (access.equalsIgnoreCase("Да")) result = true;
                        logic.setAccessUser(user.get(), result);
                        System.out.println("Права пользователя " + user.get().getName() + " изменены.");
                    }
                }
            }

            if (input.equalsIgnoreCase("Выйти из системы")) {
                logic.exit();
            }

            if (input.equalsIgnoreCase("Помощь")) {
                System.out.println("Список доступных команд:");
                System.out.println("Регистрация");
                System.out.println("Авторизация");
                System.out.println("Ввод показаний");
                System.out.println("Получить актуальные показания");
                System.out.println("Показать историю показаний");
                System.out.println("Показать показания за месяц");
                System.out.println("Показать историю показаний пользователя - только для Администратора");
                System.out.println("Показать историю показаний пользователя за месяц - только для Администратора");
                System.out.println("Расширить перечень показаний - только для Администратора");
                System.out.println("Изменить права пользователя - только для Администратора");
                System.out.println("Выход");
            }
        }
    }

    public static void typeInConsole(String text) {
        System.out.println(text);
    }
}
