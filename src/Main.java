import core.*;
import exceptions.InvalidInputException;
import model.MCQQuestion;
import model.TrueFalseQuestion;
import model.ShortAnswerQuestion;
import storage.FileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("===============================================");
        System.out.println("    WELCOME TO ONLINE QUIZ ENGINE");
        System.out.println("===============================================\n");

        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getUserChoice(1, 4);

            switch (choice) {
                case 1:
                    startQuiz();
                    break;
                case 2:
                    createAndRunSampleQuiz();
                    break;
                case 3:
                    FileManager.viewPastResults();
                    break;
                case 4:
                    running = false;
                    System.out.println("\nThank you for using Online Quiz Engine. Goodbye!");
                    break;
            }
        }

        scanner.close();
    }

    private static void displayMainMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Start Quiz (Load from file)");
        System.out.println("2. Create Sample Quiz (Demo)");
        System.out.println("3. View Past Results");
        System.out.println("4. Exit");
        System.out.print("Enter your choice (1-4): ");
    }

    private static int getUserChoice(int min, int max) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("Please enter a number.");
                    continue;
                }
                int choice = Integer.parseInt(input);
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.println("Invalid choice. Please enter a number between " + min + " and " + max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            } catch (Exception e) {
                System.out.println("\n[Input stream closed - Exiting]");
                System.exit(0);
            }
        }
    }

    private static void startQuiz() {
        System.out.print("\nEnter quiz name to load: ");
        System.out.flush();
        try {
            String quizName = scanner.nextLine().trim();

            if (quizName.isEmpty()) {
                System.out.println("Quiz name cannot be empty.");
                return;
            }

            Quiz quiz = FileManager.loadQuiz(quizName);
            if (quiz == null) {
                System.out.println("Failed to load quiz.");
                return;
            }

            runQuiz(quiz);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void runQuiz(Quiz quiz) {
        System.out.print("\nEnter your name: ");
        System.out.flush();
        try {
            String studentName = scanner.nextLine().trim();

            if (studentName.isEmpty()) {
                System.out.println("Name cannot be empty.");
                return;
            }

            try {
                System.out.println("\n--- QUIZ DETAILS ---");
                System.out.println("Title: " + quiz.getTitle());
                System.out.println("Total Questions: " + quiz.getQuestions().size());
                System.out.println("Total Marks: " + quiz.getTotalMarks());
                System.out.println("Time Limit: " + quiz.getTimeLimitSeconds() + " seconds");
                System.out.println("\nStarting quiz...\n");

                QuizSession session = new QuizSession(quiz);
                session.start();

                Map<Integer, String> answers = session.getResults();

                // Evaluate scores
                ScoreEvaluator evaluator = new ScoreEvaluator(new DefaultScoringStrategy());
                ResultReport report = evaluator.evaluate(quiz, answers, studentName);

                // Display and save results
                report.printSummary();
                FileManager.saveResult(report);

            } catch (Exception e) {
                System.err.println("Error during quiz: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void createAndRunSampleQuiz() {
        System.out.println("\n--- CREATING SAMPLE QUIZ ---");

        Quiz sampleQuiz = new Quiz(1, "Java Fundamentals Quiz", 60);

        // MCQ Question
        List<String> mcqOptions = new ArrayList<>();
        mcqOptions.add("Object Oriented Programming");
        mcqOptions.add("Open Office Protocol");
        mcqOptions.add("Operational Output Process");
        mcqOptions.add("None");
        MCQQuestion mcq = new MCQQuestion(1, "What does OOP stand for?", 2, mcqOptions, "A");
        sampleQuiz.addQuestion(mcq);

        // True/False Question
        TrueFalseQuestion tf = new TrueFalseQuestion(2, 
            "Java supports multiple inheritance through classes.", 1, false);
        sampleQuiz.addQuestion(tf);

        // Short Answer Question
        ShortAnswerQuestion sa = new ShortAnswerQuestion(3, 
            "What keyword is used to inherit a class in Java?", 2, "extends");
        sampleQuiz.addQuestion(sa);

        System.out.println("Sample quiz created successfully!");
        System.out.println("Total marks: " + sampleQuiz.getTotalMarks());
        System.out.println("Time limit: " + sampleQuiz.getTimeLimitSeconds() + " seconds\n");

        // Run the quiz
        runQuiz(sampleQuiz);
    }
}
