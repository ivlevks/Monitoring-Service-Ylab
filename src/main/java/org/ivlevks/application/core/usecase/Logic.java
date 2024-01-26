package org.ivlevks.application.core.usecase;

import org.ivlevks.application.core.entity.Indication;
import org.ivlevks.application.core.entity.User;
import org.ivlevks.application.dataproviders.repositories.InMemoryDataProvider;
import org.ivlevks.application.entrypoints.out.ConsoleHandlerOut;
import java.util.Optional;

public class Logic {

    private final Registry registry;
    private final GetUpdateUsers getUpdateUsers;
    private final GetUpdateIndications getUpdateIndications;
    private final ConsoleHandlerOut consoleHandlerOut;
    private User currentUser;

    public Logic(InMemoryDataProvider dataProvider) {
        this.registry = dataProvider;
        this.getUpdateUsers = dataProvider;
        this.getUpdateIndications = dataProvider;
        this.consoleHandlerOut = new ConsoleHandlerOut();
    }

    public void registry(String name, String email, String password, Boolean isAdmin) {
        Optional<User> user = getUpdateUsers.getUser(email);
        if (user.isPresent()) {
            consoleHandlerOut.typeInConsole("Ошибка регистрации, " +
                    "такой пользователь уже существует");
        } else {
            registry.registry(name, email, password, isAdmin);
            consoleHandlerOut.typeInConsole("Регистрация прошла успешно");
        }
    }

    public void auth(String email, String password) {
        Optional<User> user = getUpdateUsers.getUser(email);
        boolean resultAuth = user.map(value -> value.getPassword().equals(password)).orElse(false);
        if (resultAuth) {
            currentUser = user.get();
            consoleHandlerOut.typeInConsole("Авторизация прошла успешно");
        } else {
            consoleHandlerOut.typeInConsole("Ошибка авторизации, пользователь не найден, либо " +
                    "пароль не верный");
        }
    }

    public void updateIndication(Double heat, Double hotWater, Double coldWater) {
        if (!isUserAuthorize()) {
            System.out.println("Ошибка, Вы не авторизованы!");
            return;
        }

        Optional<Indication> lastActualIndication = getUpdateIndications.getLastActualIndication(currentUser);
        if (isIndicationValid(heat, hotWater, coldWater, lastActualIndication)) {
            getUpdateIndications.updateIndication(currentUser, new Indication(heat, hotWater, coldWater));
        } else {
            System.out.println("Ошибка, введенные данные не коррректны");
        }
    }

    private boolean isIndicationValid(Double heat, Double hotWater, Double coldWater, Optional<Indication> lastActualIndication) {
        if (lastActualIndication.isEmpty()) return true;

        Indication lastIndication = lastActualIndication.get();



        return heat >= lastIndication.getHeat() &&
                hotWater >= lastIndication.getHotWater() &&
                coldWater >= lastIndication.getColdWater();
    }

    private boolean isUserAuthorize() {
        return currentUser != null;
    }
}
