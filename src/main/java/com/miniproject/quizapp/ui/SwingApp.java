package com.miniproject.quizapp.ui;

import com.miniproject.quizapp.model.Question;
import com.miniproject.quizapp.model.Quiz;
import com.miniproject.quizapp.model.Result;
import com.miniproject.quizapp.model.Role;
import com.miniproject.quizapp.model.User;
import com.miniproject.quizapp.service.AdminService;
import com.miniproject.quizapp.service.AuthService;
import com.miniproject.quizapp.service.QuizService;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

public class SwingApp {
    private final AuthService authService;
    private final AdminService adminService;
    private final QuizService quizService;

    public SwingApp() {
        this.authService = new AuthService();
        this.adminService = new AdminService();
        this.quizService = new QuizService();
    }

    public void start() {
        SwingUtilities.invokeLater(this::showLoginScreen);
    }

    private void showLoginScreen() {
        JFrame frame = new JFrame("Quiz Application - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 220);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel());
        panel.add(loginButton);

        loginButton.addActionListener(e -> {
            try {
                User user = authService.login(usernameField.getText(), new String(passwordField.getPassword()));
                if (user == null) {
                    JOptionPane.showMessageDialog(frame, "Invalid credentials.");
                    return;
                }
                frame.dispose();
                if (user.getRole() == Role.ADMIN) {
                    showAdminDashboard(user);
                } else {
                    showUserDashboard(user);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    private void showAdminDashboard(User user) {
        JFrame frame = new JFrame("Admin Dashboard - " + user.getUsername());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);

        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);

        JButton createQuizButton = new JButton("Create Quiz");
        JButton addQuestionButton = new JButton("Add Question");
        JButton updateQuestionButton = new JButton("Update Question");
        JButton deleteQuestionButton = new JButton("Delete Question");
        JButton refreshButton = new JButton("View All");
        JButton logoutButton = new JButton("Logout");

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(createQuizButton);
        topPanel.add(addQuestionButton);
        topPanel.add(updateQuestionButton);
        topPanel.add(deleteQuestionButton);
        topPanel.add(refreshButton);
        topPanel.add(logoutButton);

        createQuizButton.addActionListener(e -> {
            String title = JOptionPane.showInputDialog(frame, "Enter quiz title:");
            if (title != null && !title.trim().isEmpty()) {
                int quizId = adminService.createQuiz(title);
                outputArea.setText("Created quiz with ID: " + quizId + "\n\n" + buildQuizSummary());
            }
        });

        addQuestionButton.addActionListener(e -> {
            try {
                int quizId = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter quiz ID:"));
                String questionText = JOptionPane.showInputDialog(frame, "Enter question text:");
                String optionsText = JOptionPane.showInputDialog(frame, "Enter options separated by commas:");
                int correctIndex = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter correct option number:"));
                List<String> options = parseOptions(optionsText);
                adminService.addQuestion(quizId, questionText, options, correctIndex);
                outputArea.setText(buildQuizSummary());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        updateQuestionButton.addActionListener(e -> {
            try {
                int questionId = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter question ID:"));
                String questionText = JOptionPane.showInputDialog(frame, "Enter updated question text:");
                String optionsText = JOptionPane.showInputDialog(frame, "Enter updated options separated by commas:");
                int correctIndex = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter correct option number:"));
                boolean updated = adminService.updateQuestion(questionId, questionText, parseOptions(optionsText), correctIndex);
                outputArea.setText((updated ? "Question updated.\n\n" : "Question not found.\n\n") + buildQuizSummary());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteQuestionButton.addActionListener(e -> {
            try {
                int questionId = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter question ID to delete:"));
                boolean deleted = adminService.deleteQuestion(questionId);
                outputArea.setText((deleted ? "Question deleted.\n\n" : "Question not found.\n\n") + buildQuizSummary());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        refreshButton.addActionListener(e -> outputArea.setText(buildQuizSummary()));
        logoutButton.addActionListener(e -> {
            frame.dispose();
            showLoginScreen();
        });

        outputArea.setText(buildQuizSummary());
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void showUserDashboard(User user) {
        JFrame frame = new JFrame("User Dashboard - " + user.getUsername());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(720, 520);
        frame.setLocationRelativeTo(null);

        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setText(buildQuizList());

        JButton attemptQuizButton = new JButton("Attempt Quiz");
        JButton myResultsButton = new JButton("My Results");
        JButton refreshButton = new JButton("Refresh Quizzes");
        JButton logoutButton = new JButton("Logout");

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(attemptQuizButton);
        topPanel.add(myResultsButton);
        topPanel.add(refreshButton);
        topPanel.add(logoutButton);

        attemptQuizButton.addActionListener(e -> openQuizAttemptDialog(frame, user, outputArea));
        myResultsButton.addActionListener(e -> outputArea.setText(buildResultsText(user.getId())));
        refreshButton.addActionListener(e -> outputArea.setText(buildQuizList()));
        logoutButton.addActionListener(e -> {
            frame.dispose();
            showLoginScreen();
        });

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void openQuizAttemptDialog(JFrame parent, User user, JTextArea outputArea) {
        try {
            int quizId = Integer.parseInt(JOptionPane.showInputDialog(parent, "Enter quiz ID to attempt:"));
            List<Question> questions = quizService.getQuestionsForQuiz(quizId, true);
            if (questions.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "This quiz has no questions.");
                return;
            }

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            List<JTextField> answerFields = new ArrayList<>();

            for (int i = 0; i < questions.size(); i++) {
                Question question = questions.get(i);
                JTextArea questionArea = new JTextArea();
                questionArea.setEditable(false);
                questionArea.setLineWrap(true);
                questionArea.setWrapStyleWord(true);
                StringBuilder text = new StringBuilder();
                text.append("Q").append(i + 1).append(": ").append(question.getQuestionText()).append("\n");
                for (int j = 0; j < question.getOptions().size(); j++) {
                    text.append("  ").append(j + 1).append(". ").append(question.getOptions().get(j)).append("\n");
                }
                questionArea.setText(text.toString());
                JTextField answerField = new JTextField();
                answerField.setBorder(BorderFactory.createTitledBorder("Enter option number"));
                panel.add(questionArea);
                panel.add(answerField);
                answerFields.add(answerField);
            }

            int choice = JOptionPane.showConfirmDialog(parent, new JScrollPane(panel), "Attempt Quiz", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (choice == JOptionPane.OK_OPTION) {
                List<Integer> answers = new ArrayList<>();
                for (JTextField answerField : answerFields) {
                    answers.add(Integer.parseInt(answerField.getText().trim()));
                }
                Result result = quizService.submitQuiz(user.getId(), quizId, questions, answers);
                outputArea.setText("Quiz submitted. Score: " + result.getScore() + "/" + result.getTotalQuestions() + "\n\n" + buildResultsText(user.getId()));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parent, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String buildQuizSummary() {
        StringBuilder builder = new StringBuilder();
        List<Quiz> quizzes = adminService.getAllQuizzes();
        if (quizzes.isEmpty()) {
            return "No quizzes available.";
        }
        for (Quiz quiz : quizzes) {
            builder.append("Quiz ID: ").append(quiz.getId()).append(" | Title: ").append(quiz.getTitle()).append("\n");
            List<Question> questions = adminService.getQuestionsByQuizId(quiz.getId());
            if (questions.isEmpty()) {
                builder.append("  No questions added yet.\n");
            }
            for (Question question : questions) {
                builder.append("  Question ID: ").append(question.getId()).append(" - ").append(question.getQuestionText()).append("\n");
                for (int i = 0; i < question.getOptions().size(); i++) {
                    builder.append("    ").append(i + 1).append(". ").append(question.getOptions().get(i)).append("\n");
                }
                builder.append("    Correct answer: Option ").append(question.getCorrectAnswerIndex()).append("\n");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private String buildQuizList() {
        StringBuilder builder = new StringBuilder("Available Quizzes\n----------------\n");
        List<Quiz> quizzes = quizService.getAvailableQuizzes();
        if (quizzes.isEmpty()) {
            return builder.append("No quizzes available.").toString();
        }
        for (Quiz quiz : quizzes) {
            builder.append("Quiz ID: ").append(quiz.getId()).append(" | Title: ").append(quiz.getTitle()).append("\n");
        }
        return builder.toString();
    }

    private String buildResultsText(int userId) {
        StringBuilder builder = new StringBuilder("My Results\n----------\n");
        List<Result> results = quizService.getResultsByUserId(userId);
        if (results.isEmpty()) {
            return builder.append("No results available.").toString();
        }
        for (Result result : results) {
            builder.append("Quiz ID: ").append(result.getQuizId())
                    .append(" | Score: ").append(result.getScore()).append("/").append(result.getTotalQuestions())
                    .append(" | Date: ").append(result.getCreatedAt())
                    .append("\n");
        }
        return builder.toString();
    }

    private List<String> parseOptions(String optionsText) {
        String[] rawParts = optionsText.split(",");
        List<String> options = new ArrayList<>();
        for (String rawPart : rawParts) {
            if (!rawPart.trim().isEmpty()) {
                options.add(rawPart.trim());
            }
        }
        return options;
    }
}
