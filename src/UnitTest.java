import core.*;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Unit test to verify all OOP components work correctly
 */
public class UnitTest {
    private static int passCount = 0;
    private static int failCount = 0;

    public static void main(String[] args) {
        System.out.println("Running OnlineQuizEngine Unit Tests...\n");
        
        testQuestionTypes();
        testQuiz();
        testScoring();
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Test Results: " + passCount + " passed, " + failCount + " failed");
        if (failCount == 0) {
            System.out.println("✓ All unit tests PASSED!");
        } else {
            System.out.println("✗ Some tests FAILED!");
        }
        System.out.println("=".repeat(60));
    }

    private static void test(String name, boolean condition) {
        if (condition) {
            System.out.println("    ✓ " + name);
            passCount++;
        } else {
            System.out.println("    ✗ " + name);
            failCount++;
        }
    }

    private static void testQuestionTypes() {
        System.out.println("Testing Question Types...");
        
        // Test MCQ
        List<String> options = new ArrayList<>();
        options.add("Object Oriented Programming");
        options.add("Open Office Protocol");
        options.add("Operational Output Process");
        options.add("None");
        MCQQuestion mcq = new MCQQuestion(1, "What does OOP stand for?", 2, options, "A");
        
        test("MCQ: case-insensitive 'a' should match 'A'", mcq.evaluate("a"));
        test("MCQ: 'B' should not match 'A'", !mcq.evaluate("B"));
        test("MCQ: correct answer should give full marks", mcq.getScore("A") == 2);
        test("MCQ: wrong answer should give 0 marks", mcq.getScore("B") == 0);
        
        // Test True/False
        TrueFalseQuestion tf = new TrueFalseQuestion(2, "Java supports multiple inheritance", 1, false);
        test("TF: 'false' should match", tf.evaluate("false"));
        test("TF: 'B' is also false", tf.evaluate("B"));
        test("TF: true is wrong answer", !tf.evaluate("true"));
        test("TF: correct answer should give full marks", tf.getScore("false") == 1);
        
        // Test Short Answer
        ShortAnswerQuestion sa = new ShortAnswerQuestion(3, "Inheritance keyword?", 2, "extends");
        test("SA: exact match", sa.evaluate("extends"));
        test("SA: case-insensitive", sa.evaluate("EXTENDS"));
        test("SA: whitespace trimmed", sa.evaluate("  extends  "));
        test("SA: partial match should fail", !sa.evaluate("extend"));
        test("SA: correct answer gives full marks", sa.getScore("extends") == 2);
    }

    private static void testQuiz() {
        System.out.println("\nTesting Quiz Container...");
        
        Quiz quiz = new Quiz(1, "Test Quiz", 300);
        
        List<String> options = new ArrayList<>();
        options.add("A");
        options.add("B");
        options.add("C");
        options.add("D");
        
        MCQQuestion mcq = new MCQQuestion(1, "Q1?", 2, options, "A");
        TrueFalseQuestion tf = new TrueFalseQuestion(2, "Q2?", 1, false);
        ShortAnswerQuestion sa = new ShortAnswerQuestion(3, "Q3?", 2, "answer");
        
        quiz.addQuestion(mcq);
        quiz.addQuestion(tf);
        quiz.addQuestion(sa);
        
        test("Quiz should have 3 questions", quiz.getQuestions().size() == 3);
        test("Total marks should be 5", quiz.getTotalMarks() == 5);
        test("Quiz title should match", quiz.getTitle().equals("Test Quiz"));
        test("Time limit should match", quiz.getTimeLimitSeconds() == 300);
    }

    private static void testScoring() {
        System.out.println("\nTesting Score Evaluation (Strategy Pattern)...");
        
        // Create quiz
        Quiz quiz = new Quiz(1, "Scoring Test", 300);
        
        List<String> options = new ArrayList<>();
        options.add("Correct");
        options.add("Wrong");
        options.add("Wrong");
        options.add("Wrong");
        
        MCQQuestion mcq = new MCQQuestion(1, "Which is correct?", 2, options, "A");
        TrueFalseQuestion tf = new TrueFalseQuestion(2, "Is this true?", 1, false);
        ShortAnswerQuestion sa = new ShortAnswerQuestion(3, "Answer?", 2, "correct");
        
        quiz.addQuestion(mcq);
        quiz.addQuestion(tf);
        quiz.addQuestion(sa);
        
        // Create answers - all correct
        Map<Integer, String> answers = new HashMap<>();
        answers.put(1, "A");        // Correct - MCQ
        answers.put(2, "B");        // Correct - TF (B = false)
        answers.put(3, "correct");  // Correct - SA
        
        // Evaluate using Strategy Pattern
        ScoreStrategy strategy = new DefaultScoringStrategy();
        ResultReport report = strategy.calculate(quiz, answers, "Test Student");
        
        test("All correct answers should give 5 marks", report.getTotalScore() == 5);
        test("Max score should be 5", report.getMaxScore() == 5);
        test("Percentage should be 100%", report.getPercentage() == 100.0);
        test("Student name should match", report.getStudentName().equals("Test Student"));
        
        // Test partial scores
        answers.put(1, "B");    // Wrong MCQ
        answers.put(2, "A");    // Wrong TF
        // SA still correct
        
        report = strategy.calculate(quiz, answers, "Test Student");
        test("Only SA correct should give 2 marks", report.getTotalScore() == 2);
        test("Percentage should be 40%", Math.abs(report.getPercentage() - 40.0) < 0.1);
        test("Question 1 result should be wrong", !report.getQuestionResults().get(1));
        test("Question 3 result should be correct", report.getQuestionResults().get(3));
    }
}

