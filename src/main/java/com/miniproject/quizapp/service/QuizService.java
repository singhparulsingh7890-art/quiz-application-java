package com.miniproject.quizapp.service;

import com.miniproject.quizapp.dao.QuestionDao;
import com.miniproject.quizapp.dao.QuizDao;
import com.miniproject.quizapp.dao.ResultDao;
import com.miniproject.quizapp.model.Question;
import com.miniproject.quizapp.model.Quiz;
import com.miniproject.quizapp.model.Result;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizService {
    private final QuizDao quizDao;
    private final QuestionDao questionDao;
    private final ResultDao resultDao;

    public QuizService() {
        this.quizDao = new QuizDao();
        this.questionDao = new QuestionDao();
        this.resultDao = new ResultDao();
    }

    public List<Quiz> getAvailableQuizzes() {
        return quizDao.getAllQuizzes();
    }

    public List<Question> getQuestionsForQuiz(int quizId, boolean shuffleQuestions) {
        List<Question> questions = new ArrayList<>(questionDao.getQuestionsByQuizId(quizId));
        if (shuffleQuestions) {
            Collections.shuffle(questions);
        }
        return questions;
    }

    public Result submitQuiz(int userId, int quizId, List<Question> questions, List<Integer> selectedAnswers) {
        if (questions == null || selectedAnswers == null || questions.size() != selectedAnswers.size()) {
            throw new IllegalArgumentException("Questions and answers must match.");
        }

        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getCorrectAnswerIndex() == selectedAnswers.get(i)) {
                score++;
            }
        }

        Result result = new Result();
        result.setUserId(userId);
        result.setQuizId(quizId);
        result.setScore(score);
        result.setTotalQuestions(questions.size());
        result.setCreatedAt(LocalDateTime.now());
        resultDao.saveResult(result);
        return result;
    }

    public List<Result> getResultsByUserId(int userId) {
        return resultDao.getResultsByUserId(userId);
    }
}
