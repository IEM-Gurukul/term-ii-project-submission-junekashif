package model;

public class ShortAnswerQuestion extends Question {
    private String expectedAnswer;

    public ShortAnswerQuestion(int questionId, String questionText, int marks, String expectedAnswer) {
        super(questionId, questionText, marks);
        this.expectedAnswer = expectedAnswer.toLowerCase().trim();
    }

    @Override
    public void display() {
        System.out.println("\nQuestion " + getId() + ": " + getText() + " (" + getMarks() + " marks)");
        System.out.println("  [Type your answer below]");
    }

    @Override
    public boolean evaluate(String answer) {
        if (answer == null) {
            return false;
        }
        return answer.toLowerCase().trim().equals(expectedAnswer);
    }

    @Override
    public int getScore(String answer) {
        if (evaluate(answer)) {
            return getMarks();
        }
        return 0;
    }

    public String getExpectedAnswer() {
        return expectedAnswer;
    }
}
