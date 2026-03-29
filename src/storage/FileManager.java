package storage;

import core.Quiz;
import core.ResultReport;
import model.MCQQuestion;
import model.Question;
import model.ShortAnswerQuestion;
import model.TrueFalseQuestion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String RESULTS_FILE = "data/results.txt";
    private static final String QUIZZES_FILE = "data/quizzes.txt";

    public static void saveResult(ResultReport report) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RESULTS_FILE, true))) {
            writer.write("========================================");
            writer.newLine();
            writer.write("Student: " + report.getStudentName());
            writer.newLine();
            writer.write("Quiz: " + report.getQuizTitle());
            writer.newLine();
            writer.write("Score: " + report.getTotalScore() + " / " + report.getMaxScore());
            writer.newLine();
            writer.write(String.format("Percentage: %.2f%%", report.getPercentage()));
            writer.newLine();
            writer.write("Question Results:");
            writer.newLine();
            for (Integer qId : report.getQuestionResults().keySet()) {
                boolean result = report.getQuestionResults().get(qId);
                String status = result ? "CORRECT" : "WRONG";
                writer.write("  Q" + qId + ": " + status);
                writer.newLine();
            }
            writer.write("========================================");
            writer.newLine();
            writer.newLine();
            System.out.println("Result saved successfully to " + RESULTS_FILE);
        } catch (IOException e) {
            System.err.println("Error saving result: " + e.getMessage());
        }
    }

    public static void saveQuiz(Quiz quiz) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(QUIZZES_FILE, true))) {
            writer.write("QUIZ|" + quiz.getQuizId() + "|" + quiz.getTitle() + "|" + quiz.getTimeLimitSeconds());
            writer.newLine();

            for (Question q : quiz.getQuestions()) {
                if (q instanceof MCQQuestion) {
                    MCQQuestion mcq = (MCQQuestion) q;
                    StringBuilder optionsStr = new StringBuilder();
                    for (String opt : mcq.getOptions()) {
                        optionsStr.append(opt).append("||");
                    }
                    writer.write("MCQ|" + q.getId() + "|" + q.getText() + "|" + q.getMarks() + "|" 
                        + optionsStr.toString() + "|" + mcq.getCorrectOption());
                    writer.newLine();
                } else if (q instanceof TrueFalseQuestion) {
                    TrueFalseQuestion tf = (TrueFalseQuestion) q;
                    writer.write("TF|" + q.getId() + "|" + q.getText() + "|" + q.getMarks() + "|" 
                        + tf.getCorrectAnswer());
                    writer.newLine();
                } else if (q instanceof ShortAnswerQuestion) {
                    ShortAnswerQuestion sa = (ShortAnswerQuestion) q;
                    writer.write("SA|" + q.getId() + "|" + q.getText() + "|" + q.getMarks() + "|" 
                        + sa.getExpectedAnswer());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving quiz: " + e.getMessage());
        }
    }

    public static Quiz loadQuiz(String quizName) {
        Quiz quiz = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(QUIZZES_FILE))) {
            String line;
            boolean foundQuiz = false;

            while ((line = reader.readLine()) != null) {
                if (foundQuiz && line.startsWith("QUIZ")) {
                    break;
                }

                if (line.startsWith("QUIZ") && line.contains(quizName)) {
                    String[] parts = line.split("\\|");
                    int quizId = Integer.parseInt(parts[1]);
                    String title = parts[2];
                    int timeLimitSeconds = Integer.parseInt(parts[3]);
                    quiz = new Quiz(quizId, title, timeLimitSeconds);
                    foundQuiz = true;
                } else if (foundQuiz && (line.startsWith("MCQ") || line.startsWith("TF") || line.startsWith("SA"))) {
                    Question question = parseQuestion(line);
                    if (question != null) {
                        quiz.addQuestion(question);
                    }
                }
            }

            if (quiz != null) {
                System.out.println("Quiz '" + quizName + "' loaded successfully.");
                return quiz;
            } else {
                System.out.println("Quiz '" + quizName + "' not found.");
                return null;
            }
        } catch (IOException e) {
            System.err.println("Error loading quiz: " + e.getMessage());
            return null;
        }
    }

    private static Question parseQuestion(String line) {
        try {
            String[] parts = line.split("\\|");

            if (parts[0].equals("MCQ")) {
                int qId = Integer.parseInt(parts[1]);
                String text = parts[2];
                int marks = Integer.parseInt(parts[3]);
                List<String> options = new ArrayList<>();
                String[] optionParts = parts[4].split("\\|\\|");
                for (String opt : optionParts) {
                    if (!opt.isEmpty()) {
                        options.add(opt.trim());
                    }
                }
                String correctOption = parts[5];
                return new MCQQuestion(qId, text, marks, options, correctOption);
            } else if (parts[0].equals("TF")) {
                int qId = Integer.parseInt(parts[1]);
                String text = parts[2];
                int marks = Integer.parseInt(parts[3]);
                boolean correctAnswer = Boolean.parseBoolean(parts[4]);
                return new TrueFalseQuestion(qId, text, marks, correctAnswer);
            } else if (parts[0].equals("SA")) {
                int qId = Integer.parseInt(parts[1]);
                String text = parts[2];
                int marks = Integer.parseInt(parts[3]);
                String expectedAnswer = parts[4];
                return new ShortAnswerQuestion(qId, text, marks, expectedAnswer);
            }
        } catch (Exception e) {
            System.err.println("Error parsing question: " + e.getMessage());
        }
        return null;
    }

    public static void viewPastResults() {
        File file = new File(RESULTS_FILE);
        if (!file.exists() || file.length() == 0) {
            System.out.println("No past results found.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(RESULTS_FILE))) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("PAST QUIZ RESULTS");
            System.out.println("=".repeat(60));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading results: " + e.getMessage());
        }
    }
}
