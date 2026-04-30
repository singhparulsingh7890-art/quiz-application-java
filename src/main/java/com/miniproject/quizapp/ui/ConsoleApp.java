package com.miniproject.quizapp.ui;

import com.miniproject.quizapp.model.Question;
import com.miniproject.quizapp.model.Quiz;
import com.miniproject.quizapp.model.Result;
import com.miniproject.quizapp.model.Role;
import com.miniproject.quizapp.model.User;
import com.miniproject.quizapp.service.AdminService;
import com.miniproject.quizapp.service.AuthService;
import com.miniproject.quizapp.service.QuizService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleApp {
    private final AuthService authService;
    private final AdminService adminService;
    private final QuizService quizService;
    private final Scanner scanner;

    public ConsoleApp(Scanner scanner) {
        this.authService = new AuthService();
        this.adminService = new AdminService();
        this.quizService = new QuizService();
        this.scanner = scanner;
    }

    public void start() {
        System.out.println("==============================");
        System.out.println(" Welcome to Quiz Application ");
        System.out.println("==============================");

        while (true) {
            try {
                User user = loginPrompt();
                if (user == null) {
                    System.out.println("Invalid credentials. Try again.\n");
                    continue;
                }

                if (user.getRole() == Role.ADMIN) {
                    adminMenu();
                } else {
                    userMenu(user);
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private User loginPrompt() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        return authService.login(username, password);
    }

    private void adminMenu() {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Create quiz");
            System.out.println("2. Add question to quiz");
            System.out.println("3. Update question");
            System.out.println("4. Delete question");
            System.out.println("5. View all quizzes and questions");
            System.out.println("6. Logout");
            System.out.print("Choose option: ");
            int choice = readInt();

            switch (choice) {
                case 1:
                    createQuiz();
                    break;
                case 2:
                    addQuestion();
                    break;
                case 3:
                    updateQuestion();
                    break;
                case 4:
                    deleteQuestion();
                    break;
                case 5:
                    viewAllQuizzes();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void userMenu(User user) {
        while (true) {
            System.out.println("\n--- User Menu ---");
            System.out.println("1. View available quizzes");
            System.out.println("2. Attempt quiz");
            System.out.println("3. View my results");
            System.out.println("4. Logout");
            System.out.print("Choose option: ");
            int choice = readInt();

            switch (choice) {
                case 1:
                    listQuizzes();
                    break;
                case 2:
                    attemptQuiz(user);
                    break;
                case 3:
                    viewResults(user);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void createQuiz() {
        System.out.print("Enter quiz title: ");
        String title = scanner.nextLine();
        int quizId = adminService.createQuiz(title);
        System.out.println("Quiz created with ID: " + quizId);
    }

    private void addQuestion() {
        listQuizzes();
        System.out.print("Enter quiz ID: ");
        int quizId = readInt();
        QuestionInput input = readQuestionInput();
        int questionId = adminService.addQuestion(quizId, input.questionText, input.options, input.correctAnswerIndex);
        System.out.println("Question added with ID: " + questionId);
    }

    private void updateQuestion() {
        viewAllQuizzes();
        System.out.print("Enter question ID to update: ");
        int questionId = readInt();
        QuestionInput input = readQuestionInput();
        boolean updated = adminService.updateQuestion(questionId, input.questionText, input.options, input.correctAnswerIndex);
        System.out.println(updated ? "Question updated successfully." : "Question not found.");
    }

    private void deleteQuestion() {
        viewAllQuizzes();
        System.out.print("Enter question ID to delete: ");
        int questionId = readInt();
        boolean deleted = adminService.deleteQuestion(questionId);
        System.out.println(deleted ? "Question deleted successfully." : "Question not found.");
    }

    private void viewAllQuizzes() {
        List<Quiz> quizzes = adminService.getAllQuizzes();
        if (quizzes.isEmpty()) {
            System.out.println("No quizzes found.");
            return;
        }

        for (Quiz quiz : quizzes) {
            System.out.println("\nQuiz ID: " + quiz.getId() + " | Title: " + quiz.getTitle());
            List<Question> questions = adminService.getQuestionsByQuizId(quiz.getId());
            if (questions.isEmpty()) {
                System.out.println("  No questions added yet.");
                continue;
            }
            for (Question question : questions) {
                System.out.println("  Question ID: " + question.getId() + " - " + question.getQuestionText());
                for (int i = 0; i < question.getOptions().size(); i++) {
                    System.out.println("    " + (i + 1) + ". " + question.getOptions().get(i));
                }
                System.out.println("    Correct Answer: Option " + question.getCorrectAnswerIndex());
            }
        }
    }

    private void listQuizzes() {
        List<Quiz> quizzes = quizService.getAvailableQuizzes();
        if (quizzes.isEmpty()) {
            System.out.println("No quizzes available.");
            return;
        }
        for (Quiz quiz : quizzes) {
            System.out.println("Quiz ID: " + quiz.getId() + " | Title: " + quiz.getTitle());
        }
    }

    private void attemptQuiz(User user) {
        listQuizzes();
        System.out.print("Enter quiz ID to attempt: ");
        int quizId = readInt();
        List<Question> questions = quizService.getQuestionsForQuiz(quizId, true);
        if (questions.isEmpty()) {
            System.out.println("This quiz has no questions.");
            return;
        }

        List<Integer> answers = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        int timeLimitSeconds = Math.max(questions.size() * 30, 60);

        for (int i = 0; i < questions.size(); i++) {
            long elapsed = (System.currentTimeMillis() - startTime) / 1000;
            if (elapsed > timeLimitSeconds) {
                System.out.println("Time is up. Submitting your quiz automatically.");
                while (answers.size() < questions.size()) {
                    answers.add(-1);
                }
                break;
            }

            Question question = questions.get(i);
            System.out.println("\nQ" + (i + 1) + ": " + question.getQuestionText());
            for (int j = 0; j < question.getOptions().size(); j++) {
                System.out.println((j + 1) + ". " + question.getOptions().get(j));
            }
            System.out.print("Your answer: ");
            answers.add(readInt());
        }

        Result result = quizService.submitQuiz(user.getId(), quizId, questions, answers);
        System.out.println("Quiz submitted. Your score: " + result.getScore() + "/" + result.getTotalQuestions());
    }

    private void viewResults(User user) {
        List<Result> results = quizService.getResultsByUserId(user.getId());
        if (results.isEmpty()) {
            System.out.println("No results found.");
            return;
        }

        for (Result result : results) {
            System.out.println("Quiz ID: " + result.getQuizId()
                    + " | Score: " + result.getScore() + "/" + result.getTotalQuestions()
                    + " | Date: " + result.getCreatedAt());
        }
    }

    private QuestionInput readQuestionInput() {
        System.out.print("Enter question text: ");
        String questionText = scanner.nextLine();
        System.out.print("How many options? ");
        int optionCount = readInt();

        List<String> options = new ArrayList<>();
        for (int i = 1; i <= optionCount; i++) {
            System.out.print("Enter option " + i + ": ");
            options.add(scanner.nextLine());
        }

        System.out.print("Enter correct answer option number: ");
        int correctAnswerIndex = readInt();
        return new QuestionInput(questionText, options, correctAnswerIndex);
    }

    private int readInt() {
        while (true) {
            String value = scanner.nextLine();
            try {
                return Integer.parseInt(value.trim());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }

    private static class QuestionInput {
        private final String questionText;
        private final List<String> options;
        private final int correctAnswerIndex;

        private QuestionInput(String questionText, List<String> options, int correctAnswerIndex) {
            this.questionText = questionText;
            this.options = options;
            this.correctAnswerIndex = correctAnswerIndex;
        }
    }
}
