package org.ivlevks.application.core.usecase;

import org.ivlevks.application.core.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс действий с пользователями
 */
public interface GetUpdateUsers {

    /**
     * Добавление пользователя
     * @param user - добавляемый пользователь
     */
    void addUser(User user);

    /**
     * Получение пользователя по email
     * @param email - email
     * @return - возвращает пользователя, обернутого в Optional<>
     */
    Optional<User> getUser(String email);

    /**
     * Получение списка всех пользователей
     * @return - список всех пользователей
     */
    List<User> getAllUsers();

}
