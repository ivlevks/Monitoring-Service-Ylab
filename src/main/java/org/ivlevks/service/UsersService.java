package org.ivlevks.service;

import org.ivlevks.starter.annotations.Loggable;
import org.ivlevks.domain.entity.User;
import org.ivlevks.service.port.UsersRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Класс реализации логики в части работы с пользователями
 */
@Loggable
@Service
public class UsersService {
    private final AdminHelper adminHelper;
    private final UsersRepository usersRepository;
    private final String regexPatternEmail = "^(.+)@(\\S+)$";

    public UsersService(AdminHelper adminHelper, UsersRepository usersRepository) {
        this.adminHelper = adminHelper;
        this.usersRepository = usersRepository;
    }

    /**
     * Регистрация пользователя
     * @param name имя
     * @param email email
     * @param password пароль
     * @param isAdmin является ли пользователь админом
     */
    public Optional<User> registry(String name, String email, String password, Boolean isAdmin) {
        if (isInputDataValid(name, email, password)) {
            Optional<User> user = usersRepository.getUserByEmail(email);
            if (user.isPresent()) {
                System.out.println("Ошибка регистрации, " +
                        "такой пользователь уже существует");
                return Optional.empty();
            } else {
                User newUser = new User(name, email, password, isAdmin);
                usersRepository.addUser(newUser);
                System.out.println("Регистрация прошла успешно");
                return getUserByEmail(newUser.getEmail());
            }
        } else {
            System.out.println("Ошибка регистрации, " +
                    "введенные данные не валидны!");
            return Optional.empty();
        }
    }

    /**
     * Проверка на валидацию входящих данных от пользователя
     * @param name имя
     * @param email email
     * @param password пароль
     * @return true если данные валидны
     */
    private boolean isInputDataValid(String name, String email, String password) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) return false;
        return Pattern.compile(regexPatternEmail).matcher(email).matches();
    }

    /**
     * Аутентификация пользователя
     * @param email email
     * @param password пароль
     */
    public Optional<User> auth(String email, String password) {
        Optional<User> user = usersRepository.getUserByEmail(email);
        boolean resultAuth = user.map(value -> value.getPassword().equals(password)).orElse(false);
        if (resultAuth) {
            adminHelper.setIdCurrentUser(user.get().getId());
            System.out.println("Авторизация администратора прошла успешно");
            return user;
        } else {
            System.out.println("Ошибка авторизации, пользователь не найден, либо " +
                    "пароль не верный");
            return Optional.empty();
        }
    }

    /**
     * Получение пользователя по email
     * @param email email
     * @return возвращает пользователя, обернутого в Optional<>
     */
    public Optional<User> getUserByEmail(String email) {
        return usersRepository.getUserByEmail(email);
    }

    /**
     * Получение пользователя по id
     * @param id - id
     * @return возвращает пользователя, обернутого в Optional<>
     */
    public Optional<User> getUserById(int id) {
        return usersRepository.getUserById(id);
    }

    /**
     * Изменение прав доступа пользователя
     * @param user пользователь
     */
    public Optional<User> updateUser(User user) {
        usersRepository.updateUser(user);
        return getUserById(user.getId());
    }

    /**
     * Выход текущего пользователя из системы
     */
    public void exit() {
        adminHelper.setIdCurrentUser(0);
        System.out.println("Пользователь вышел из системы.");
    }
}
