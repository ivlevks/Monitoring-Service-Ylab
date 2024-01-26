package org.ivlevks.application.core.usecase;

import org.ivlevks.application.core.entity.Indication;
import org.ivlevks.application.core.entity.User;
import org.ivlevks.application.dataproviders.repositories.InMemoryDataProvider;
import org.ivlevks.application.entrypoints.in.ConsoleHandler;
import java.time.LocalDateTime;
import java.util.*;

public class Logic {

    private final Registry registry;
    private final GetUpdateUsers getUpdateUsers;
    private final GetUpdateIndications getUpdateIndications;
    private User currentUser;
    private Set<String> setNameIndications;

    public Logic(InMemoryDataProvider dataProvider) {
        this.registry = dataProvider;
        this.getUpdateUsers = dataProvider;
        this.getUpdateIndications = dataProvider;
        this.setNameIndications = new HashSet<>();
        setNameIndications.add("Heat");
        setNameIndications.add("Cold Water");
        setNameIndications.add("Hot Water");
    }

    public void registry(String name, String email, String password, Boolean isAdmin) {
        Optional<User> user = getUpdateUsers.getUser(email);
        if (user.isPresent()) {
            ConsoleHandler.typeInConsole("Ошибка регистрации, " +
                    "такой пользователь уже существует");
        } else {
            registry.registry(name, email, password, isAdmin);
            ConsoleHandler.typeInConsole("Регистрация прошла успешно");
        }
    }

    public void auth(String email, String password) {
        Optional<User> user = getUpdateUsers.getUser(email);
        boolean resultAuth = user.map(value -> value.getPassword().equals(password)).orElse(false);
        if (resultAuth) {
            currentUser = user.get();
            ConsoleHandler.typeInConsole("Авторизация прошла успешно");
        } else {
            ConsoleHandler.typeInConsole("Ошибка авторизации, пользователь не найден, либо " +
                    "пароль не верный");
        }
    }

    public Optional<Indication> getLastActualIndicationUser() {
        return getUpdateIndications.getLastActualIndication(currentUser);
    }

    public List<Indication> getAllIndicationsUser () {
        return getUpdateIndications.getAllIndications(currentUser);
    }

    public void updateIndication(HashMap<String, Double> indications) {
        if (!isUserAuthorize()) {
            System.out.println("Ошибка, Вы не авторизованы!");
            return;
        }

        Optional<Indication> lastActualIndications = getUpdateIndications.getLastActualIndication(currentUser);
        if (isIndicationValid(indications, lastActualIndications)) {
            getUpdateIndications.updateIndication(currentUser, new Indication(indications));
            System.out.println("Показания введены");
        } else {
            System.out.println("Ошибка, введенные данные не коррректны");
        }
    }

    private boolean isIndicationValid(HashMap<String, Double> indications, Optional<Indication> lastActualIndication) {
        if (lastActualIndication.isEmpty()) return true;

        Indication lastIndication = lastActualIndication.get();
        // временно отключена проверка по дате
//        if (hasCurrentMonthIndication(lastIndication)) {
//            System.out.println("Ошибка, в данном месяце показания уже вводились");
//            return false;
//        }

        for (Map.Entry<String, Double> entry : lastIndication.getIndications().entrySet()) {
            if (entry.getValue() > indications.get(entry.getKey())) return false;
        }
        return true;
    }

    private boolean hasCurrentMonthIndication(Indication lastIndication) {
        LocalDateTime lastIndicationDateTime = lastIndication.getDateTime();
        if (lastIndicationDateTime.getYear() < LocalDateTime.now().getYear()) return false;
        if (lastIndicationDateTime.getMonthValue() < LocalDateTime.now().getMonthValue()) return false;
        return true;
    }

    private boolean isUserAuthorize() {
        return currentUser != null;
    }

    public Set<String> getSetNameIndications() {
        return setNameIndications;
    }

    public void setSetNameIndications(Set<String> setNameIndications) {
        this.setNameIndications = setNameIndications;
    }
}
