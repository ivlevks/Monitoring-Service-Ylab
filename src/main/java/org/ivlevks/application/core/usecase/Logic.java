package org.ivlevks.application.core.usecase;

import org.ivlevks.application.core.entity.User;
import org.ivlevks.application.dataproviders.repositories.InMemoryDataProvider;
import org.ivlevks.application.entrypoints.out.ConsoleHandlerOut;

import java.util.Optional;

public class Logic {

    private final Registry registry;
    private final GetUsersAndIndications getUsersAndIndications;
    private final ConsoleHandlerOut consoleHandlerOut;
    private boolean isUserAuthorize;

    public Logic(InMemoryDataProvider dataProvider) {
        this.registry = dataProvider;
        this.getUsersAndIndications = dataProvider;
        this.consoleHandlerOut = new ConsoleHandlerOut();
    }

    public void registry(String name, String email, String password, Boolean isAdmin) {
        registry.registry(name, email, password, isAdmin);
        // проверка на существующего пользователя
        consoleHandlerOut.typeInConsole("Регистрация прошла успешно");
    }

    public void auth(String email, String password) {
        Optional<User> user = getUsersAndIndications.getUser(email);
        boolean resultAuth = user.map(value -> value.getPassword().equals(password)).orElse(false);
        if (resultAuth) {
            isUserAuthorize = true;
            consoleHandlerOut.typeInConsole("Авторизация прошла успешно");
        } else {
            consoleHandlerOut.typeInConsole("Ошибка авторизации, пользователь не найден, либо " +
                    "пароль не верный");
        }
    }
}
