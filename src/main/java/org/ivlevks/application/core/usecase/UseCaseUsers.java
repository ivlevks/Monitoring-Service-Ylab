package org.ivlevks.application.core.usecase;

import org.ivlevks.application.configuration.Audit;
import org.ivlevks.application.core.entity.User;
import org.ivlevks.application.dataproviders.repositories.InMemoryDataProvider;
import org.ivlevks.application.presentation.in.ConsoleHandler;
import java.util.Optional;

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

    /**
     * Регистрация пользователя
     * @param name имя
     * @param email email
     * @param password пароль
     * @param isAdmin является ли пользователь админом
     */
    public void registry(String name, String email, String password, Boolean isAdmin) {
        Optional<User> user = getUpdateUsers.getUser(email);
        if (user.isPresent()) {
            ConsoleHandler.typeInConsole("Ошибка регистрации, " +
                    "такой пользователь уже существует");
        } else {
            User newUser = new User(name, email, password, isAdmin);
            getUpdateUsers.addUser(newUser);
            ConsoleHandler.typeInConsole("Регистрация прошла успешно");
            Audit.addInfoInAudit("User " + name + ", email " + email +
                    ", password " + password + " registry in system");
        }
    }

    /**
     * Аутентификация пользователя
     * @param email email
     * @param password пароль
     */
    public void auth(String email, String password) {
        Optional<User> user = getUpdateUsers.getUser(email);
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
    }

    /**
     * Получение пользователя по email
     * @param email email
     * @return возвращает пользователя, обернутого в Optional<>
     */
    public Optional<User> findUserByEmail(String email) {
        return getUpdateUsers.getUser(email);
    }

    /**
     * Изменение прав доступа пользователя
     * @param user пользователь
     * @param isUserAdmin true - добавить права админа, false - убрать
     */
    public void setAccessUser(User user, boolean isUserAdmin) {
        user.setUserAdmin(isUserAdmin);
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
