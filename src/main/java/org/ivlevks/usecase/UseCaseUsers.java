package org.ivlevks.usecase;

import org.ivlevks.adapter.repository.jdbc.IndicationRepositoryImpl;
import org.ivlevks.adapter.repository.jdbc.UserRepositoryImpl;
import org.ivlevks.configuration.Audit;
import org.ivlevks.configuration.annotations.Loggable;
import org.ivlevks.domain.entity.User;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Подкласс реализации логики в части работы с пользователями
 */
@Loggable
@Service
public class UseCaseUsers extends UseCase {

    public UseCaseUsers(UserRepositoryImpl userRepositoryImpl, IndicationRepositoryImpl indicationRepositoryImpl) {
        super(userRepositoryImpl, indicationRepositoryImpl);
    }

    public UseCaseUsers() {
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
                Audit.addInfoInAudit("User " + name + ", email " + email +
                        ", password " + password + " registry in system");
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
            setCurrentUser(user.get());
            if (currentUser.isUserAdmin()) {
                System.out.println("Авторизация администратора прошла успешно");
                Audit.addInfoInAudit("Admin with email " + email +
                        ", password " + password + " authorize in system");
            } else {
                System.out.println("Авторизация пользователя прошла успешно");
                Audit.addInfoInAudit("User with email " + email +
                        ", password " + password + " authorize in system");
            }
            return user;
        } else {
            System.out.println("Ошибка авторизации, пользователь не найден, либо " +
                    "пароль не верный");
            Audit.addInfoInAudit("Failure authorization with email " + email +
                    ", password " + password);
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
        System.out.println("Пользователь " + currentUser.getName() + " вышел из системы.");
        Audit.addInfoInAudit("User with email " + currentUser.getEmail() + " exit from system");
        setCurrentUser(null);
    }
}
