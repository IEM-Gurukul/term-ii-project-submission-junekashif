package core;

import java.util.HashMap;
import java.util.Map;

public class ResultReport {
    private String studentName;
    private String quizTitle;
    private int totalScore;
    private int maxScore;
    private double percentage;
    private Map<Integer, Boolean> questionResults;

    public ResultReport(String studentName, String quizTitle, int totalScore, int maxScore, Map<Integer, Boolean> questionResults) {
        this.studentName = studentName;
        this.quizTitle = quizTitle;
        this.totalScore = totalScore;
        this.maxScore = maxScore;
        this.percentage = (maxScore > 0) ? (totalScore * 100.0 / maxScore) : 0;
        this.questionResults = new HashMap<>(questionResults);
    }

    public String getStudentName() {
        return studentName;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public double getPercentage() {
        return percentage;
    }

    public Map<Integer, Boolean> getQuestionResults() {
        return new HashMap<>(questionResults);
    }

    public void printSummary() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("QUIZ RESULT REPORT");
        System.out.println("=".repeat(60));
        System.out.println("Student Name: " + studentName);
        System.out.println("Quiz Title: " + quizTitle);
        System.out.println("-".repeat(60));
        
        System.out.println("Question Results:");
        for (Integer questionId : questionResults.keySet()) {
            boolean result = questionResults.get(questionId);
            String status = result ? "✓ CORRECT" : "✗ WRONG";
            System.out.println("  Question " + questionId + ": " + status);
        }
        
        System.out.println("-".repeat(60));
        System.out.println("Total Score: " + totalScore + " / " + maxScore);
        System.out.printf("Percentage: %.2f%%\n", percentage);
        System.out.println("=".repeat(60));
    }

    @Override
    public String toString() {
        return String.format("Student: %s | Quiz: %s | Score: %d/%d (%.2f%%)", 
            studentName, quizTitle, totalScore, maxScore, percentage);
    }
}
