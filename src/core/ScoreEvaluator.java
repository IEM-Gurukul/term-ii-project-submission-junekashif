package core;

import java.util.Map;

public class ScoreEvaluator {
    private ScoreStrategy strategy;

    public ScoreEvaluator(ScoreStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(ScoreStrategy strategy) {
        this.strategy = strategy;
    }

    public ResultReport evaluate(Quiz quiz, Map<Integer, String> answers, String studentName) {
        return strategy.calculate(quiz, answers, studentName);
    }
}
