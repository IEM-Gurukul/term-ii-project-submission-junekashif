package core;

import model.Question;
import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private int quizId;
    private String title;
    private List<Question> questions;
    private int timeLimitSeconds;

    public Quiz(int quizId, String title, int timeLimitSeconds) {
        this.quizId = quizId;
        this.title = title;
        this.timeLimitSeconds = timeLimitSeconds;
        this.questions = new ArrayList<>();
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public int getQuizId() {
        return quizId;
    }

    public String getTitle() {
        return title;
    }

    public List<Question> getQuestions() {
        return new ArrayList<>(questions);
    }

    public int getTotalMarks() {
        int total = 0;
        for (Question q : questions) {
            total += q.getMarks();
        }
        return total;
    }

    public int getTimeLimitSeconds() {
        return timeLimitSeconds;
    }

    public Question getQuestionById(int questionId) {
        for (Question q : questions) {
            if (q.getId() == questionId) {
                return q;
            }
        }
        return null;
    }
}
