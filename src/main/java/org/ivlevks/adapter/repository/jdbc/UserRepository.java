package org.ivlevks.adapter.repository.jdbc;

import org.ivlevks.domain.entity.User;
import org.ivlevks.usecase.port.GetUpdateUsers;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Имплементация интерфейса хранения пользователей через JDBC
 */
public class UserRepository implements GetUpdateUsers {
    private static final String USER_GET = "SELECT * FROM monitoring.users WHERE email = ?";
    private static final String USER_ADD = "INSERT INTO monitoring.users (username, email," +
            " password, admin) VALUES (?, ?, ?, ?)";
    private static final String USER_UPDATE = "UPDATE monitoring.users SET admin = ? WHERE email = ?";
    private static final String USER_GET_ALL = "SELECT * FROM monitoring.users";
    private final String URL;
    private final String USER_NAME;
    private final String PASSWORD;

    public UserRepository(String URL, String USER_NAME, String PASSWORD) {
        this.URL = URL;
        this.USER_NAME = USER_NAME;
        this.PASSWORD = PASSWORD;
    }

    /**
     * Добавление пользователя
     * @param user - добавляемый пользователь
     */
    @Override
    public void addUser(User user) {
        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
             PreparedStatement addUserStatement = connection.prepareStatement(USER_ADD)) {
            addUserStatement.setString(1, user.getName());
            addUserStatement.setString(2, user.getEmail());
            addUserStatement.setString(3, user.getPassword());
            addUserStatement.setBoolean(4, user.isUserAdmin());
            addUserStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Получение пользователя по email
     * @param email - email
     * @return
     */
    @Override
    public Optional<User> getUser(String email) {
        User user = null;

        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
             PreparedStatement getUserStatement = connection.prepareStatement(USER_GET)) {
            getUserStatement.setString(1, email);
            ResultSet resultSet = getUserStatement.executeQuery();

            while (resultSet.next()) {
                Integer userId = resultSet.getInt("id");
                String userName = resultSet.getString("username");
                String userEmail = resultSet.getString("email");
                String userPassword = resultSet.getString("password");
                Boolean admin = resultSet.getBoolean("admin");
                user = new User(userId, userName, userEmail, userPassword, admin);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(user);
    }

    /**
     * Получение списка всех пользователей
     * @return - список всех пользователей
     */
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
             PreparedStatement getUserStatement = connection.prepareStatement(USER_GET_ALL)) {
            ResultSet resultSet = getUserStatement.executeQuery();

            while (resultSet.next()) {
                String userName = resultSet.getString("username");
                String userEmail = resultSet.getString("email");
                String userPassword = resultSet.getString("password");
                Boolean admin = resultSet.getBoolean("admin");
                users.add(new User(userName, userEmail, userPassword, admin));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Обновление пользователя
     * @param user пользователь
     */
    @Override
    public void updateUser(User user) {
        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
             PreparedStatement userUpdateDataStatement = connection.prepareStatement(USER_UPDATE)) {
            userUpdateDataStatement.setBoolean(1, user.isUserAdmin());
            userUpdateDataStatement.setString(2, user.getEmail());
            userUpdateDataStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
