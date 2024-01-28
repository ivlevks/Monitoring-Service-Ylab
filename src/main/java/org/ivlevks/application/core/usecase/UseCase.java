package org.ivlevks.application.core.usecase;

import org.ivlevks.application.core.entity.User;
import org.ivlevks.application.dataproviders.repositories.InMemoryDataProvider;
import java.util.*;

/**
 * Родительский класс логики действий с пользователями и показаниями
 * хранит в себе ссылку на авторизованного пользователя
 * и перечень изначальных видов счетчиков - 3 шт.
 */
public class UseCase {
    static User currentUser;
    static Set<String> nameIndications;
    final GetUpdateUsers getUpdateUsers;
    final GetUpdateIndications getUpdateIndications;

    /**
     * Конструктор, инициализирует начальный перечень видов показаний
     * счетчиков в количестве 3 шт.
     * @param dataProvider - реализация подключения к хранилищу данных
     */
    public UseCase(InMemoryDataProvider dataProvider) {
        this.getUpdateUsers = dataProvider;
        this.getUpdateIndications = dataProvider;
        nameIndications = new HashSet<>();
        nameIndications.add("Heat");
        nameIndications.add("Cold Water");
        nameIndications.add("Hot Water");
    }

    /**
     * Проверка на авторизованного пользоателя
     * @return true если пользователь авторизован
     */
    boolean isUserAuthorize() {
        return currentUser != null;
    }

    /**
     * Проверка на админа
     * @return true если авторизован админ
     */
    public boolean isCurrentUserAdmin() {
        return currentUser.isUserAdmin();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        UseCase.currentUser = currentUser;
    }
}
