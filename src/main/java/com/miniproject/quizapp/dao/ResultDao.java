package com.miniproject.quizapp.dao;

import com.miniproject.quizapp.config.DatabaseConnection;
import com.miniproject.quizapp.model.Result;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ResultDao {

    public void saveResult(Result result) {
        String sql = "INSERT INTO results(user_id, quiz_id, score, total_questions) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, result.getUserId());
            statement.setInt(2, result.getQuizId());
            statement.setInt(3, result.getScore());
            statement.setInt(4, result.getTotalQuestions());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save result.", e);
        }
    }

    public List<Result> getResultsByUserId(int userId) {
        String sql = "SELECT id, user_id, quiz_id, score, total_questions, created_at FROM results WHERE user_id = ? ORDER BY created_at DESC";
        List<Result> results = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Timestamp timestamp = resultSet.getTimestamp("created_at");
                    results.add(new Result(
                            resultSet.getInt("id"),
                            resultSet.getInt("user_id"),
                            resultSet.getInt("quiz_id"),
                            resultSet.getInt("score"),
                            resultSet.getInt("total_questions"),
                            timestamp == null ? null : timestamp.toLocalDateTime()
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch results.", e);
        }

        return results;
    }
}
