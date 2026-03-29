package model;

import java.util.ArrayList;
import java.util.List;

public class MCQQuestion extends Question {
    private List<String> options;
    private String correctOption;

    public MCQQuestion(int questionId, String questionText, int marks, List<String> options, String correctOption) {
        super(questionId, questionText, marks);
        this.options = new ArrayList<>(options);
        this.correctOption = correctOption.toUpperCase();
    }

    @Override
    public void display() {
        System.out.println("\nQuestion " + getId() + ": " + getText() + " (" + getMarks() + " marks)");
        for (int i = 0; i < options.size(); i++) {
            char optionLabel = (char) ('A' + i);
            System.out.println("  " + optionLabel + ") " + options.get(i));
        }
    }

    @Override
    public boolean evaluate(String answer) {
        if (answer == null) {
            return false;
        }
        return answer.trim().toUpperCase().equals(correctOption);
    }

    @Override
    public int getScore(String answer) {
        if (evaluate(answer)) {
            return getMarks();
        }
        return 0;
    }

    public List<String> getOptions() {
        return new ArrayList<>(options);
    }

    public String getCorrectOption() {
        return correctOption;
    }
}
