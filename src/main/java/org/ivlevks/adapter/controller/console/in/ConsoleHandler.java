package org.ivlevks.adapter.controller.console.in;

import org.ivlevks.configuration.Audit;
import org.ivlevks.domain.entity.Indication;
import org.ivlevks.domain.entity.User;
import org.ivlevks.usecase.UseCaseIndications;
import org.ivlevks.usecase.UseCaseUsers;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

/**
 * Уровень представления
 * получает ввод с консоли, передает управление в логику прилоежния - в Use Case.
 */
public class ConsoleHandler {
    private final Scanner scanner = new Scanner(System.in);
    private final UseCaseUsers useCaseUsers;
    private final UseCaseIndications useCaseIndications;
    private String input = "";

    public ConsoleHandler(UseCaseUsers useCaseUsers, UseCaseIndications useCaseIndications) {
        this.useCaseUsers = useCaseUsers;
        this.useCaseIndications = useCaseIndications;
    }

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

        startConsoleHandlerIn();
    }

    /**
     * Принятие команд из консоли, передача данных на следующий уровень - Use Case
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
                useCaseUsers.registry(name, email, password, false);
            }

            if (input.equalsIgnoreCase("Авторизация")) {
                System.out.print("Введите email: ");
                String email = scanner.nextLine();
                System.out.print("Введите пароль: ");
                String password = scanner.nextLine();
                useCaseUsers.auth(email, password);
            }

            if (input.equalsIgnoreCase("Ввод показаний")) {
                HashMap<String, String> indications = new HashMap<>();
                for (String nameIndication : useCaseIndications.getNamesIndications()) {
                    System.out.print("Введите показания " + nameIndication + " ");
                    String value = scanner.nextLine();
                    indications.put(nameIndication, value);
                }
                useCaseIndications.addIndication(indications);
            }

            if (input.equalsIgnoreCase("Получить актуальные показания")) {
                Optional<Indication> lastActualIndication = useCaseIndications.getLastActualIndicationUser();
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
                if (useCaseIndications.getAllIndicationsUser().isEmpty()) {
                    System.out.println("История показаний отсутствует");
                } else {
                    for (Indication indication : useCaseIndications.getAllIndicationsUser()) {
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
                Optional<Indication> result = useCaseIndications.getMonthIndicationsUser(year, month);
                if (result.isEmpty()) {
                    System.out.println("Показания на " + month + " месяц " + year + " года отсутствуют");
                } else {
                    for (Map.Entry<String, Double> entry : result.get().getIndications().entrySet()) {
                        System.out.println(entry.getKey() + "   " + entry.getValue());
                    }
                }
            }

            //для Админа
            if (input.equalsIgnoreCase("Показать историю показаний пользователя")) {
                if (!useCaseUsers.isCurrentUserAdmin()) {
                    System.out.println("Ошибка, Вы не являетесь администратором!");
                } else {
                    System.out.println("Введите email пользователя:");
                    String email = scanner.nextLine();
                    Optional<User> user = useCaseUsers.findUserByEmail(email);
                    if (user.isEmpty()) {
                        System.out.println("Такого пользователя не существует");
                    } else {
                        if (useCaseIndications.getAllIndicationsUser(user.get()).isEmpty()) {
                            System.out.println("История показаний отсутствует");
                        } else {
                            for (Indication indication : useCaseIndications.getAllIndicationsUser(user.get())) {
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
                if (!useCaseUsers.isCurrentUserAdmin()) {
                    System.out.println("Ошибка, Вы не являетесь администратором!");
                } else {
                    System.out.println("Введите email пользователя:");
                    String email = scanner.nextLine();
                    Optional<User> user = useCaseUsers.findUserByEmail(email);
                    if (user.isEmpty()) {
                        System.out.println("Такого пользователя не существует");
                    } else {
                        System.out.println("Введите год за который хотите просмотреть показания в формате YYYY");
                        int year = Integer.parseInt(scanner.nextLine());
                        System.out.println("Введите номер месяца за который хотите просмотреть показания:");
                        int month = Integer.parseInt(scanner.nextLine());

                        System.out.println("Показания на " + month + " месяц " + year + " года:");
                        for (Indication indication : useCaseIndications.getAllIndicationsUser(user.get())) {
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
                if (!useCaseUsers.isCurrentUserAdmin()) {
                    System.out.println("Ошибка, Вы не являетесь администратором!");
                } else {
                    System.out.println("Введите наименование нового показания:");
                    String newNameIndication = scanner.nextLine();

                    useCaseIndications.addNewNameIndication(newNameIndication);
                    System.out.println("Новый вид показания " + newNameIndication + " добавлен!" );
                }
            }

            //для Админа
            if (input.equalsIgnoreCase("Изменить права пользователя")) {
                if (!useCaseUsers.isCurrentUserAdmin()) {
                    System.out.println("Ошибка, Вы не являетесь администратором!");
                } else {
                    System.out.println("Введите email пользователя, у которого необходимо изменить права доступа:");
                    String email = scanner.nextLine();
                    Optional<User> user = useCaseUsers.findUserByEmail(email);
                    if (user.isEmpty()) {
                        System.out.println("Такого пользователя не существует");
                    } else {
                        System.out.println("Необходимо добавить права администратора (введите Да), или убрать их" +
                                "(введите Нет)");
                        String access = scanner.nextLine();
                        boolean result = false;
                        if (access.equalsIgnoreCase("Да")) result = true;
                        useCaseUsers.setAccessUser(user.get(), result);
                        System.out.println("Права пользователя " + user.get().getName() + " изменены.");
                    }
                }
            }

            if (input.equalsIgnoreCase("Выйти из системы")) {
                useCaseUsers.exit();
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

    /**
     * Вывод на констоль
     * @param text - выводимый текст
     */
    public static void typeInConsole(String text) {
        System.out.println(text);
    }
}
