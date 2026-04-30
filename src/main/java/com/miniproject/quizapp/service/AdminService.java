package com.miniproject.quizapp.service;

import com.miniproject.quizapp.dao.QuestionDao;
import com.miniproject.quizapp.dao.QuizDao;
import com.miniproject.quizapp.model.Question;
import com.miniproject.quizapp.model.Quiz;

import java.util.List;

public class AdminService {
    private final QuizDao quizDao;
    private final QuestionDao questionDao;

    public AdminService() {
        this.quizDao = new QuizDao();
        this.questionDao = new QuestionDao();
    }

    public int createQuiz(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Quiz title cannot be empty.");
        }
        return quizDao.createQuiz(title.trim());
    }

    public int addQuestion(int quizId, String questionText, List<String> options, int correctAnswerIndex) {
        validateQuestionData(questionText, options, correctAnswerIndex);
        Question question = new Question();
        question.setQuizId(quizId);
        question.setQuestionText(questionText.trim());
        question.setOptions(options);
        question.setCorrectAnswerIndex(correctAnswerIndex);
        return questionDao.addQuestion(question);
    }

    public boolean updateQuestion(int questionId, String questionText, List<String> options, int correctAnswerIndex) {
        validateQuestionData(questionText, options, correctAnswerIndex);
        Question existing = questionDao.findById(questionId);
        if (existing == null) {
            return false;
        }
        existing.setQuestionText(questionText.trim());
        existing.setOptions(options);
        existing.setCorrectAnswerIndex(correctAnswerIndex);
        return questionDao.updateQuestion(existing);
    }

    public boolean deleteQuestion(int questionId) {
        return questionDao.deleteQuestion(questionId);
    }

    public List<Quiz> getAllQuizzes() {
        return quizDao.getAllQuizzes();
    }

    public List<Question> getQuestionsByQuizId(int quizId) {
        return questionDao.getQuestionsByQuizId(quizId);
    }

    private void validateQuestionData(String questionText, List<String> options, int correctAnswerIndex) {
        if (questionText == null || questionText.trim().isEmpty()) {
            throw new IllegalArgumentException("Question text cannot be empty.");
        }
        if (options == null || options.size() < 2) {
            throw new IllegalArgumentException("A question must have at least 2 options.");
        }
        for (String option : options) {
            if (option == null || option.trim().isEmpty()) {
                throw new IllegalArgumentException("Options cannot be empty.");
            }
        }
        if (correctAnswerIndex < 1 || correctAnswerIndex > options.size()) {
            throw new IllegalArgumentException("Correct answer index is out of range.");
        }
    }
}
