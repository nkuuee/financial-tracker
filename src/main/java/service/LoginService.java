package service;

import org.mindrot.jbcrypt.BCrypt;
import utils.PasswordUtils;

import java.sql.*;
import java.util.Arrays;

public class LoginService {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/financial_tracker";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    //Проверка, существует ли данный пользователь
    public Boolean userExists(String username) throws SQLException {
        try (Connection connection = getConnection()) {
            String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) > 0; //Если 1+ юзер найден, то возвращает true
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка: " + e.getMessage());
        }
        return false;
    }

    public Boolean userAuthentication(String username, String password) {
        try (Connection connection = getConnection()) {
            String sql = "SELECT password FROM users WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    String passwordSave = rs.getString("password");
                    return PasswordUtils.checkPassword(password, passwordSave);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Пользователь не найден");
        return false;
    }
}
