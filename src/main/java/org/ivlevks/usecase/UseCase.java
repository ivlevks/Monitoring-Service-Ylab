package org.ivlevks.usecase;

import org.ivlevks.domain.entity.User;
import org.ivlevks.adapter.repository.in_memory.InMemoryDataProvider;
import org.ivlevks.usecase.port.GetUpdateIndications;
import org.ivlevks.usecase.port.GetUpdateUsers;

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
    String regexPatternEmail = "^(.+)@(\\S+)$";

    /**
     * Конструктор, инициализирует начальный перечень видов показаний
     * счетчиков в количестве 3 шт.
     * для jdbc реализации
     * @param getUpdateUsers, getUpdateIndications - реализации подключения к хранилищу данных
     */
    public UseCase(GetUpdateUsers getUpdateUsers, GetUpdateIndications getUpdateIndications) {
        this.getUpdateUsers = getUpdateUsers;
        this.getUpdateIndications = getUpdateIndications;
        nameIndications = new HashSet<>();
        nameIndications.add("Heat");
        nameIndications.add("Cold Water");
        nameIndications.add("Hot Water");
    }

    /**
     * Конструктор, инициализирует начальный перечень видов показаний
     * счетчиков в количестве 3 шт.
     * для in-memory реализации
     * @param dataProvider
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
