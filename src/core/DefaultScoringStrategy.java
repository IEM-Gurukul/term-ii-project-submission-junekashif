package core;

import model.Question;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultScoringStrategy implements ScoreStrategy {
    @Override
    public ResultReport calculate(Quiz quiz, Map<Integer, String> answers, String studentName) {
        List<Question> questions = quiz.getQuestions();
        int totalScore = 0;
        int maxScore = 0;
        Map<Integer, Boolean> questionResults = new HashMap<>();

        for (Question question : questions) {
            maxScore += question.getMarks();
            int questionId = question.getId();
            String userAnswer = answers.getOrDefault(questionId, "");

            int score = question.getScore(userAnswer);
            totalScore += score;

            boolean isCorrect = score == question.getMarks();
            questionResults.put(questionId, isCorrect);
        }

        return new ResultReport(studentName, quiz.getTitle(), totalScore, maxScore, questionResults);
    }
}
