package model;

public class TrueFalseQuestion extends Question {
    private boolean correctAnswer;

    public TrueFalseQuestion(int questionId, String questionText, int marks, boolean correctAnswer) {
        super(questionId, questionText, marks);
        this.correctAnswer = correctAnswer;
    }

    @Override
    public void display() {
        System.out.println("\nQuestion " + getId() + ": " + getText() + " (" + getMarks() + " marks)");
        System.out.println("  A) True");
        System.out.println("  B) False");
    }

    @Override
    public boolean evaluate(String answer) {
        if (answer == null) {
            return false;
        }
        String trimmedAnswer = answer.trim().toLowerCase();
        boolean userAnswer;
        if (trimmedAnswer.equals("true") || trimmedAnswer.equals("a")) {
            userAnswer = true;
        } else if (trimmedAnswer.equals("false") || trimmedAnswer.equals("b")) {
            userAnswer = false;
        } else {
            return false;
        }
        return userAnswer == correctAnswer;
    }

    @Override
    public int getScore(String answer) {
        if (evaluate(answer)) {
            return getMarks();
        }
        return 0;
    }

    public boolean getCorrectAnswer() {
        return correctAnswer;
    }
}
