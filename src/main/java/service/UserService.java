package service;

import java.sql.*;

public class UserService {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/financial_tracker";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public Boolean equalsUser(String username) throws SQLException {
        try (Connection connection = getConnection()) {
            String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

    }

}
