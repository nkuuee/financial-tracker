package service;

import utils.PasswordUtils;

import java.sql.*;

public class UserService {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/financial_tracker";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // Проверка, существует ли пользователь в БД
    public boolean userExists(String username) {
        try (Connection connection = getConnection()) {
            String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Если хотя бы один пользователь найден
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Регистрация нового пользователя
    public boolean registerUser(String username, String password) {
        // Проверка, существует ли уже пользователь с таким именем
        if (userExists(username)) {
            System.out.println("Пользователь с таким именем уже существует.");
            return false;
        }

        //Хэшируем пароль
        String hashPassword = PasswordUtils.hashPassword(password);

        //Сохраняем в БД
        try (Connection connection = getConnection()) {
            String sql = "INSERT INTO users(username,password) VALUES (?,?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(1, hashPassword);
                preparedStatement.executeUpdate();
                return true; //Регистрация прошла успешна
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; //Ошибка при регистрации
    }

}


