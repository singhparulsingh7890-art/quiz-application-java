package com.miniproject.quizapp.model;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private int id;
    private int quizId;
    private String questionText;
    private List<String> options;
    private int correctAnswerIndex;

    public Question() {
        this.options = new ArrayList<>();
    }

    public Question(int id, int quizId, String questionText, List<String> options, int correctAnswerIndex) {
        this.id = id;
        this.quizId = quizId;
        this.questionText = questionText;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }
}
