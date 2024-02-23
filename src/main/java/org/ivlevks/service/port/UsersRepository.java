package org.ivlevks.service.port;

import org.ivlevks.domain.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс действий с пользователями
 */
public interface UsersRepository {

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
    Optional<User> getUserByEmail(String email);

    /**
     * Получение пользователя по id
     * @param id - id
     * @return - возвращает пользователя, обернутого в Optional<>
     */
    Optional<User> getUserById(int id);

    /**
     * Получение списка всех пользователей
     * @return - список всех пользователей
     */
    List<User> getAllUsers();

    /**
     * Обновление данных пользователя
     * @param user пользователь
     */
    void updateUser(User user);
}
