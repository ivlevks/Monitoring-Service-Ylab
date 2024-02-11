package org.ivlevks.usecase;

import org.ivlevks.adapter.repository.jdbc.IndicationRepositoryImpl;
import org.ivlevks.adapter.repository.jdbc.UserRepositoryImpl;
import org.ivlevks.configuration.Audit;
import org.ivlevks.domain.entity.User;
import org.ivlevks.adapter.repository.in_memory.InMemoryDataProvider;
import org.ivlevks.adapter.controller.console.in.ConsoleHandler;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Подкласс реализации логики в части работы с пользователями
 */
public class UseCaseUsers extends UseCase {

    /**
     * Конструктор
     * @param dataProvider - реализация подключения к хранилищу данных
     */
    public UseCaseUsers(InMemoryDataProvider dataProvider) {
        super(dataProvider);
    }

    public UseCaseUsers(UserRepositoryImpl userRepositoryImpl, IndicationRepositoryImpl indicationRepositoryImpl) {
        super(userRepositoryImpl, indicationRepositoryImpl);
    }

    /**
     * Регистрация пользователя
     * @param name имя
     * @param email email
     * @param password пароль
     * @param isAdmin является ли пользователь админом
     */
    public boolean registry(String name, String email, String password, Boolean isAdmin) {
        if (isInputDataValid(name, email, password)) {
            Optional<User> user = usersRepository.getUser(email);
            if (user.isPresent()) {
                ConsoleHandler.typeInConsole("Ошибка регистрации, " +
                        "такой пользователь уже существует");

            } else {
                User newUser = new User(name, email, password, isAdmin);
                usersRepository.addUser(newUser);
                ConsoleHandler.typeInConsole("Регистрация прошла успешно");
                Audit.addInfoInAudit("User " + name + ", email " + email +
                        ", password " + password + " registry in system");
                return true;
            }
        } else {
            ConsoleHandler.typeInConsole("Ошибка регистрации, " +
                    "введенные данные не валидны!");
        }
        return false;
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
    public boolean auth(String email, String password) {
        Optional<User> user = usersRepository.getUser(email);
        boolean resultAuth = user.map(value -> value.getPassword().equals(password)).orElse(false);
        if (resultAuth) {
            setCurrentUser(user.get());
            if (currentUser.isUserAdmin()) {
                ConsoleHandler.typeInConsole("Авторизация администратора прошла успешно");
                Audit.addInfoInAudit("Admin with email " + email +
                        ", password " + password + " authorize in system");
            } else {
                ConsoleHandler.typeInConsole("Авторизация пользователя прошла успешно");
                Audit.addInfoInAudit("User with email " + email +
                        ", password " + password + " authorize in system");
            }
        } else {
            ConsoleHandler.typeInConsole("Ошибка авторизации, пользователь не найден, либо " +
                    "пароль не верный");
            Audit.addInfoInAudit("Failure authorization with email " + email +
                    ", password " + password);
        }
        return resultAuth;
    }

    /**
     * Получение пользователя по email
     * @param email email
     * @return возвращает пользователя, обернутого в Optional<>
     */
    public Optional<User> findUserByEmail(String email) {
        return usersRepository.getUser(email);
    }

    /**
     * Изменение прав доступа пользователя
     * @param user пользователь
     * @param isUserAdmin true - добавить права админа, false - убрать
     */
    public void setAccessUser(User user, boolean isUserAdmin) {
        user.setUserAdmin(isUserAdmin);
        usersRepository.updateUser(user);
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
