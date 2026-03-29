package core;

import exceptions.InvalidInputException;
import exceptions.SessionViolationException;
import model.Question;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class QuizSession implements Runnable {
    private Quiz quiz;
    private Map<Integer, String> userAnswers;
    private boolean sessionActive;
    private int timeRemaining;
    private Scanner scanner;

    public QuizSession(Quiz quiz) {
        this.quiz = quiz;
        this.userAnswers = new HashMap<>();
        this.sessionActive = false;
        this.timeRemaining = quiz.getTimeLimitSeconds();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        sessionActive = true;
        timeRemaining = quiz.getTimeLimitSeconds();

        // Start timer thread
        Thread timerThread = new Thread(this);
        timerThread.setDaemon(false);
        timerThread.start();

        // Collect answers
        List<Question> questions = quiz.getQuestions();
        for (Question question : questions) {
            if (!sessionActive) {
                break;
            }

            question.display();
            System.out.print("Your answer: ");
            System.out.flush();
            try {
                String answer = scanner.nextLine();
                if (answer == null || answer.trim().isEmpty()) {
                    throw new InvalidInputException("Answer cannot be empty. Please try again.");
                }
                submitAnswer(question.getId(), answer);
            } catch (InvalidInputException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                // Input stream closed or no more input - end session gracefully
                System.out.println("[Quiz session interrupted]");
                endSession();
                break;
            }
        }

        endSession();
    }

    @Override
    public void run() {
        while (sessionActive && timeRemaining > 0) {
            try {
                Thread.sleep(1000);
                timeRemaining--;

                if (timeRemaining % 10 == 0 && timeRemaining > 0) {
                    System.out.println("\n[Time remaining: " + timeRemaining + " seconds]");
                }

                if (timeRemaining == 0) {
                    System.out.println("\n[WARNING] TIME'S UP! Session ending automatically.");
                    sessionActive = false;
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void submitAnswer(int questionId, String answer) throws InvalidInputException {
        if (!sessionActive) {
            throw new SessionViolationException("Session is not active. Cannot submit answer.");
        }

        if (answer == null || answer.trim().isEmpty()) {
            throw new InvalidInputException("Answer cannot be blank.");
        }

        userAnswers.put(questionId, answer.trim());
        System.out.println("Answer submitted for Question " + questionId);
    }

    public void endSession() {
        sessionActive = false;
        System.out.println("\n--- Quiz Session Ended ---");
    }

    public Map<Integer, String> getResults() {
        return new HashMap<>(userAnswers);
    }

    public boolean isSessionActive() {
        return sessionActive;
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }
}
