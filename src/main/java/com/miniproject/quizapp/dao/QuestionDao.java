package com.miniproject.quizapp.dao;

import com.miniproject.quizapp.config.DatabaseConnection;
import com.miniproject.quizapp.model.Question;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionDao {

    public int addQuestion(Question question) {
        String sql = "INSERT INTO questions(quiz_id, question_text, options_text, correct_answer_index) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, question.getQuizId());
            statement.setString(2, question.getQuestionText());
            statement.setString(3, joinOptions(question.getOptions()));
            statement.setInt(4, question.getCorrectAnswerIndex());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add question.", e);
        }

        return -1;
    }

    public List<Question> getQuestionsByQuizId(int quizId) {
        String sql = "SELECT id, quiz_id, question_text, options_text, correct_answer_index FROM questions WHERE quiz_id = ? ORDER BY id";
        List<Question> questions = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, quizId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    questions.add(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch questions.", e);
        }

        return questions;
    }

    public Question findById(int questionId) {
        String sql = "SELECT id, quiz_id, question_text, options_text, correct_answer_index FROM questions WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, questionId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find question.", e);
        }

        return null;
    }

    public boolean updateQuestion(Question question) {
        String sql = "UPDATE questions SET question_text = ?, options_text = ?, correct_answer_index = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, question.getQuestionText());
            statement.setString(2, joinOptions(question.getOptions()));
            statement.setInt(3, question.getCorrectAnswerIndex());
            statement.setInt(4, question.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update question.", e);
        }
    }

    public boolean deleteQuestion(int questionId) {
        String sql = "DELETE FROM questions WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, questionId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete question.", e);
        }
    }

    private Question mapRow(ResultSet resultSet) throws SQLException {
        return new Question(
                resultSet.getInt("id"),
                resultSet.getInt("quiz_id"),
                resultSet.getString("question_text"),
                splitOptions(resultSet.getString("options_text")),
                resultSet.getInt("correct_answer_index")
        );
    }

    private String joinOptions(List<String> options) {
        return options.stream().map(String::trim).collect(Collectors.joining("||"));
    }

    private List<String> splitOptions(String optionsText) {
        return new ArrayList<>(Arrays.asList(optionsText.split("\\|\\|")));
    }
}
