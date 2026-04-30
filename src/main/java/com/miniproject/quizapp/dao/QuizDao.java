package com.miniproject.quizapp.dao;

import com.miniproject.quizapp.config.DatabaseConnection;
import com.miniproject.quizapp.model.Quiz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class QuizDao {

    public int createQuiz(String title) {
        String sql = "INSERT INTO quizzes(title) VALUES (?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, title);
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create quiz.", e);
        }

        return -1;
    }

    public List<Quiz> getAllQuizzes() {
        String sql = "SELECT id, title FROM quizzes ORDER BY id";
        List<Quiz> quizzes = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                quizzes.add(new Quiz(resultSet.getInt("id"), resultSet.getString("title")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch quizzes.", e);
        }

        return quizzes;
    }

    public Quiz findById(int quizId) {
        String sql = "SELECT id, title FROM quizzes WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, quizId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Quiz(resultSet.getInt("id"), resultSet.getString("title"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find quiz.", e);
        }

        return null;
    }
}
