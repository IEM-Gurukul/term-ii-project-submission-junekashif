package core;

import java.util.Map;

public interface ScoreStrategy {
    ResultReport calculate(Quiz quiz, Map<Integer, String> answers, String studentName);
}
