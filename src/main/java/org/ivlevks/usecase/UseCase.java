package org.ivlevks.usecase;

import org.ivlevks.adapter.repository.jdbc.IndicationRepositoryImpl;
import org.ivlevks.adapter.repository.jdbc.UserRepositoryImpl;
import org.ivlevks.configuration.annotations.Loggable;
import org.ivlevks.domain.entity.User;
import org.ivlevks.usecase.port.IndicationsRepository;
import org.ivlevks.usecase.port.UsersRepository;
import org.springframework.stereotype.Service;

/**
 * Родительский класс логики действий с пользователями и показаниями
 * хранит в себе ссылку на авторизованного пользователя
 * и перечень изначальных видов счетчиков - 3 шт.
 */
@Loggable
@Service
public class UseCase {
    static User currentUser;
    final UsersRepository usersRepository;
    final IndicationsRepository indicationsRepository;
    String regexPatternEmail = "^(.+)@(\\S+)$";

    /**
     * Конструктор, инициализирует начальный перечень видов показаний
     * счетчиков в количестве 3 шт.
     * для jdbc реализации
     * @param usersRepository, getUpdateIndications - реализации подключения к хранилищу данных
     */
    public UseCase(UsersRepository usersRepository, IndicationsRepository indicationsRepository) {
        this.usersRepository = usersRepository;
        this.indicationsRepository = indicationsRepository;
    }

    /**
     * Конструктор, инициализирует начальный перечень видов показаний
     */
    public UseCase() {
        indicationsRepository = new IndicationRepositoryImpl();
        usersRepository = new UserRepositoryImpl();
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
