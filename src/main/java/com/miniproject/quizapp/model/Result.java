package com.miniproject.quizapp.model;

import java.time.LocalDateTime;

public class Result {
    private int id;
    private int userId;
    private int quizId;
    private int score;
    private int totalQuestions;
    private LocalDateTime createdAt;

    public Result() {
    }

    public Result(int id, int userId, int quizId, int score, int totalQuestions, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.quizId = quizId;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
