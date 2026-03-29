package model;

public abstract class Question {
    private int questionId;
    private String questionText;
    private int marks;

    public Question(int questionId, String questionText, int marks) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.marks = marks;
    }

    public abstract boolean evaluate(String answer);

    public abstract int getScore(String answer);

    public abstract void display();

    public int getId() {
        return questionId;
    }

    public String getText() {
        return questionText;
    }

    public int getMarks() {
        return marks;
    }
}
