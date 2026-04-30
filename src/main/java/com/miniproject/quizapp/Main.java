package com.miniproject.quizapp;

import com.miniproject.quizapp.ui.ConsoleApp;
import com.miniproject.quizapp.ui.SwingApp;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String mode = resolveMode(args, scanner);

        if ("swing".equals(mode)) {
            new SwingApp().start();
            return;
        }

        new ConsoleApp(scanner).start();
    }

    private static String resolveMode(String[] args, Scanner scanner) {
        if (args != null && args.length > 0) {
            String mode = args[0].trim().toLowerCase();
            if ("console".equals(mode) || "swing".equals(mode)) {
                return mode;
            }
            System.out.println("Unknown mode '" + args[0] + "'. Falling back to interactive mode selection.");
        }

        return promptForMode(scanner);
    }

    private static String promptForMode(Scanner scanner) {
        while (true) {
            System.out.println("Choose application mode:");
            System.out.println("1. Console mode");
            System.out.println("2. Swing mode");
            System.out.print("Enter choice (1 or 2): ");

            String input = scanner.nextLine().trim();
            if ("1".equals(input)) {
                return "console";
            }
            if ("2".equals(input)) {
                return "swing";
            }

            System.out.println("Invalid selection. Please enter 1 or 2.\n");
        }
    }
}
