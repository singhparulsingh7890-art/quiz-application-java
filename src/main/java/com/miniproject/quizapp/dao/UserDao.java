package com.miniproject.quizapp.dao;

import com.miniproject.quizapp.config.DatabaseConnection;
import com.miniproject.quizapp.model.Role;
import com.miniproject.quizapp.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    public User findByUsernameAndPassword(String username, String password) {
        String sql = "SELECT id, username, password, role FROM users WHERE username = ? AND password = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to login user.", e);
        }

        return null;
    }

    private User mapRow(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                Role.valueOf(resultSet.getString("role"))
        );
    }
}
