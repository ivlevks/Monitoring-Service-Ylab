package org.ivlevks.adapter.repository.jdbc;

import org.ivlevks.configuration.annotations.Loggable;
import org.ivlevks.domain.entity.User;
import org.ivlevks.usecase.port.UsersRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Имплементация интерфейса хранения пользователей через JDBC
 */
@Loggable
@Repository
public class UserRepositoryImpl implements UsersRepository {
    private static final String USER_GET = "SELECT * FROM monitoring.users WHERE email = ?";
    private static final String USER_ADD = "INSERT INTO monitoring.users (username, email," +
            " password, admin) VALUES (?, ?, ?, ?)";
    private static final String USER_UPDATE = "UPDATE monitoring.users SET admin = ? WHERE email = ?";
    private static final String USER_GET_ALL = "SELECT * FROM monitoring.users";


    /**
     * Добавление пользователя
     * @param user - добавляемый пользователь
     */
    @Override
    public void addUser(User user) {
        Connection connection = org.ivlevks.configuration.DriverManager.getConnection();

        try (PreparedStatement addUserStatement = connection.prepareStatement(USER_ADD)) {
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
        Connection connection = org.ivlevks.configuration.DriverManager.getConnection();

        try (PreparedStatement getUserStatement = connection.prepareStatement(USER_GET)) {
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
        Connection connection = org.ivlevks.configuration.DriverManager.getConnection();

        try (PreparedStatement getUserStatement = connection.prepareStatement(USER_GET_ALL)) {
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
        Connection connection = org.ivlevks.configuration.DriverManager.getConnection();

        try (PreparedStatement userUpdateDataStatement = connection.prepareStatement(USER_UPDATE)) {
            userUpdateDataStatement.setBoolean(1, user.isUserAdmin());
            userUpdateDataStatement.setString(2, user.getEmail());
            userUpdateDataStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
