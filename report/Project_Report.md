# Online Quiz Engine - Project Report

**Course Name:** OOPS Lab  
**Project Title:** Online Quiz Engine  
**Student Name:** Mohammed Kashif Ansari  
**Roll Number:** 45  
**Semester:** 4

---

## 1. Problem Statement

The Online Quiz Engine is a comprehensive Java-based application designed to facilitate the creation, administration, and evaluation of online quizzes. The system supports multiple question types including Multiple Choice Questions (MCQ), True/False questions, and Short Answer questions. It provides real-time quiz sessions with background countdown timers, persistent storage for quizzes and results, and a strategy-based scoring system.

The application addresses the need for an automated quiz management system that can handle different question formats, enforce time limits, provide immediate feedback, and maintain records of quiz attempts. Key challenges include implementing polymorphic question evaluation, managing concurrent timer threads, ensuring data persistence, and providing a user-friendly console interface.

---

## 2. System Design

### UML Class Diagram

```
+-------------------+     +-------------------+
|     Question      |     |    QuizSession    |
| (Abstract Class)  |     | (Runnable)        |
+-------------------+     +-------------------+
| - questionId: int |     | - quiz: Quiz      |
| - questionText: String| | - userAnswers: Map|
| - marks: int      |     | - sessionActive: bool|
+-------------------+     | - timeRemaining: int|
| + evaluate(ans): bool|  | - scanner: Scanner|
| + getScore(ans): int|   +-------------------+
| + display(): void  |     | + start(): void   |
+-------------------+     | + run(): void     |
          ^               | + submitAnswer():void|
          |               | + endSession(): void|
          |               +-------------------+
+-------------------+     +-------------------+
|   MCQQuestion     |     |   ScoreStrategy   |
+-------------------+     |   (Interface)     |
| - options: List   |     +-------------------+
| - correctOption: String| | + calculate(): ResultReport|
+-------------------+     +-------------------+
| + evaluate(): bool|           ^
| + getScore(): int |           |
| + display(): void |     +-------------------+
+-------------------+     |DefaultScoringStrategy|
          ^               +-------------------+
          |               | + calculate(): ResultReport|
+-------------------+     +-------------------+
| TrueFalseQuestion |
+-------------------+
| - correctAnswer: bool|
+-------------------+
| + evaluate(): bool|
| + getScore(): int |
| + display(): void |
+-------------------+
          ^
+-------------------+
|ShortAnswerQuestion|
+-------------------+
| - expectedAnswer: String|
+-------------------+
| + evaluate(): bool|
| + getScore(): int |
| + display(): void |
+-------------------+

+-------------------+     +-------------------+
|       Quiz        |     |   ResultReport    |
+-------------------+     +-------------------+
| - quizId: int     |     | - studentName: String|
| - title: String   |     | - quizTitle: String|
| - timeLimitSeconds|     | - totalScore: int |
| - questions: List |     | - maxScore: int   |
+-------------------+     | - questionResults: Map|
| + addQuestion():void|   +-------------------+
| + getTotalMarks():int|  | + printSummary():void|
| + getQuestions():List|  +-------------------+
+-------------------+     +-------------------+

+-------------------+     +-------------------+
|   FileManager     |     | ScoreEvaluator    |
+-------------------+     +-------------------+
|                   |     | - strategy: ScoreStrategy|
+-------------------+     +-------------------+
| + saveResult():void|    | + evaluate(): ResultReport|
| + saveQuiz():void |     +-------------------+
| + loadQuiz(): Quiz|     +-------------------+
| + viewPastResults()|    +-------------------+
+-------------------+     +-------------------+

+-------------------+
|InvalidInputException|
+-------------------+
| (extends Exception)|
+-------------------+

+-------------------+
|SessionViolationException|
+-------------------+
| (extends RuntimeException)|
+-------------------+
```

### Description of Major Classes

#### Core Classes:
- **Question (Abstract)**: Base class for all question types, providing common attributes (ID, text, marks) and abstract methods for evaluation and display.
- **Quiz**: Container class that holds a collection of questions, manages quiz metadata (title, time limit), and provides methods to add questions and calculate total marks.
- **QuizSession**: Implements Runnable interface to manage quiz execution with background timer thread, handles user input collection, and enforces time limits.
- **ScoreEvaluator**: Uses Strategy pattern to delegate scoring logic to configurable strategies.

#### Model Classes:
- **MCQQuestion**: Handles multiple choice questions with options list and correct answer validation.
- **TrueFalseQuestion**: Manages boolean-type questions with true/false answers.
- **ShortAnswerQuestion**: Processes text-based answers with case-insensitive matching.

#### Storage Classes:
- **FileManager**: Handles persistent storage operations for quizzes and results using text file format.

#### Exception Classes:
- **InvalidInputException**: Checked exception for input validation errors.
- **SessionViolationException**: Runtime exception for quiz session violations.

### Package Structure

```
src/
├── Main.java                      (Application entry point)
├── UnitTest.java                  (Comprehensive test suite)
├── core/                          (Core business logic)
│   ├── Quiz.java
│   ├── QuizSession.java
│   ├── ScoreEvaluator.java
│   ├── ScoreStrategy.java
│   ├── DefaultScoringStrategy.java
│   └── ResultReport.java
├── model/                         (Data models)
│   ├── Question.java
│   ├── MCQQuestion.java
│   ├── TrueFalseQuestion.java
│   └── ShortAnswerQuestion.java
├── storage/                       (Data persistence)
│   └── FileManager.java
└── exceptions/                    (Custom exceptions)
    ├── InvalidInputException.java
    └── SessionViolationException.java
```

### Interaction Flow

1. **Application Start**: Main.java initializes console UI and displays menu options
2. **Quiz Loading**: FileManager.loadQuiz() reads quiz data from text files and reconstructs Quiz objects
3. **Session Initialization**: QuizSession is created with loaded Quiz, starts background timer thread
4. **Question Presentation**: Each Question.display() shows question text and options
5. **Answer Collection**: User inputs are validated and stored in QuizSession
6. **Timer Management**: Background thread counts down, enforces time limits
7. **Score Evaluation**: ScoreEvaluator uses Strategy pattern to calculate results
8. **Result Persistence**: FileManager.saveResult() stores results to file
9. **Result Display**: ResultReport.printSummary() shows final scores and feedback

---

## 3. OOP Concepts Demonstrated

### Abstraction

**Where it is used:** The `Question` abstract class provides a common interface for all question types.

**Why it is used:** Abstraction allows different question types to be treated uniformly while hiding implementation details.

**Short relevant code snippet:**
```java
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
}
```

**Explanation:** The abstract methods `evaluate()`, `getScore()`, and `display()` must be implemented by all concrete question classes, ensuring consistent behavior across different question types.

### Inheritance

**Where it is used:** `MCQQuestion`, `TrueFalseQuestion`, and `ShortAnswerQuestion` inherit from the abstract `Question` class.

**Why it is used:** Inheritance promotes code reuse and establishes an "is-a" relationship between base and derived classes.

**Short relevant code snippet:**
```java
public class MCQQuestion extends Question {
    private List<String> options;
    private String correctOption;

    public MCQQuestion(int questionId, String questionText, int marks, 
                      List<String> options, String correctOption) {
        super(questionId, questionText, marks);
        this.options = new ArrayList<>(options);
        this.correctOption = correctOption.toUpperCase();
    }

    @Override
    public boolean evaluate(String answer) {
        return answer.trim().toUpperCase().equals(correctOption);
    }
}
```

**Explanation:** `MCQQuestion` inherits common attributes and methods from `Question` while adding specific functionality for multiple choice questions.

### Polymorphism

**Where it is used:** Polymorphic method calls in `QuizSession` and `ScoreEvaluator` treat all questions uniformly.

**Why it is used:** Polymorphism allows the same method calls to behave differently based on the actual object type at runtime.

**Short relevant code snippet:**
```java
// In QuizSession.start()
for (Question question : questions) {
    question.display();  // Polymorphic call
    String answer = scanner.nextLine();
    submitAnswer(question.getId(), answer);
}

// In DefaultScoringStrategy.calculate()
for (Question question : questions) {
    int score = question.getScore(userAnswer);  // Polymorphic call
    totalScore += score;
}
```

**Explanation:** The same `display()` and `getScore()` method calls work for all question types, with the actual behavior determined by the concrete class implementation.

### Encapsulation

**Where it is used:** All classes use private fields with public getter methods to protect internal state.

**Why it is used:** Encapsulation hides implementation details and prevents unauthorized access to object state.

**Short relevant code snippet:**
```java
public class Quiz {
    private int quizId;
    private String title;
    private int timeLimitSeconds;
    private List<Question> questions;

    public Quiz(int quizId, String title, int timeLimitSeconds) {
        this.quizId = quizId;
        this.title = title;
        this.timeLimitSeconds = timeLimitSeconds;
        this.questions = new ArrayList<>();
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    public List<Question> getQuestions() {
        return new ArrayList<>(questions);  // Defensive copy
    }
}
```

**Explanation:** Private fields ensure data integrity, and getter methods provide controlled access to internal state.

### Exception Handling

**Where it is used:** Custom exceptions `InvalidInputException` and `SessionViolationException` handle error conditions.

**Why it is used:** Exception handling provides robust error management and graceful failure recovery.

**Short relevant code snippet:**
```java
public void submitAnswer(int questionId, String answer) throws InvalidInputException {
    if (!sessionActive) {
        throw new SessionViolationException("Session is not active. Cannot submit answer.");
    }

    if (answer == null || answer.trim().isEmpty()) {
        throw new InvalidInputException("Answer cannot be blank.");
    }

    userAnswers.put(questionId, answer.trim());
}
```

**Explanation:** Checked exceptions for input validation and runtime exceptions for session violations ensure proper error handling throughout the application.

### Collections Framework

**Where it is used:** `List<Question>` in Quiz, `Map<Integer, String>` for answers, `Map<Integer, Boolean>` for results.

**Why it is used:** Collections provide flexible, type-safe data structures for managing groups of objects.

**Short relevant code snippet:**
```java
private List<Question> questions = new ArrayList<>();
private Map<Integer, String> userAnswers = new HashMap<>();
private Map<Integer, Boolean> questionResults = new HashMap<>();

// Adding questions
quiz.addQuestion(mcq);
quiz.addQuestion(tf);

// Storing answers
userAnswers.put(questionId, answer.trim());

// Processing results
for (Integer qId : questionResults.keySet()) {
    boolean result = questionResults.get(qId);
}
```

**Explanation:** ArrayList provides ordered storage for questions, HashMap enables efficient key-value lookups for answers and results.

### Threading

**Where it is used:** `QuizSession` implements `Runnable` for background countdown timer.

**Why it is used:** Threading allows concurrent execution of timer and user input collection.

**Short relevant code snippet:**
```java
public class QuizSession implements Runnable {
    // ... fields ...

    public void start() {
        sessionActive = true;
        Thread timerThread = new Thread(this);
        timerThread.setDaemon(false);
        timerThread.start();

        // Main thread continues with question display and input
        for (Question question : questions) {
            question.display();
            String answer = scanner.nextLine();
            submitAnswer(question.getId(), answer);
        }
    }

    @Override
    public void run() {
        while (sessionActive && timeRemaining > 0) {
            try {
                Thread.sleep(1000);
                timeRemaining--;
                if (timeRemaining == 0) {
                    sessionActive = false;
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
```

**Explanation:** Background timer thread runs concurrently with main quiz execution, enforcing time limits independently.

### Design Patterns - Strategy Pattern

**Where it is used:** `ScoreStrategy` interface and `DefaultScoringStrategy` implementation for flexible scoring logic.

**Why it is used:** Strategy pattern allows different scoring algorithms to be plugged in without changing client code.

**Short relevant code snippet:**
```java
public interface ScoreStrategy {
    ResultReport calculate(Quiz quiz, Map<Integer, String> answers, String studentName);
}

public class DefaultScoringStrategy implements ScoreStrategy {
    @Override
    public ResultReport calculate(Quiz quiz, Map<Integer, String> answers, String studentName) {
        // Scoring implementation
        int totalScore = 0;
        for (Question question : questions) {
            int score = question.getScore(userAnswer);
            totalScore += score;
        }
        return new ResultReport(studentName, quiz.getTitle(), totalScore, maxScore, questionResults);
    }
}

// Usage in ScoreEvaluator
public class ScoreEvaluator {
    private ScoreStrategy strategy;

    public ScoreEvaluator(ScoreStrategy strategy) {
        this.strategy = strategy;
    }

    public ResultReport evaluate(Quiz quiz, Map<Integer, String> answers, String studentName) {
        return strategy.calculate(quiz, answers, studentName);
    }
}
```

**Explanation:** Strategy pattern enables runtime selection of scoring algorithms, making the system extensible for different evaluation methods.

---

## 4. Implementation Highlights

### Polymorphic Question Processing

```java
// QuizSession.java - Lines 35-50
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
    }
}
```

**Justification:** This code demonstrates polymorphism by treating all question types uniformly through the `Question` interface, enabling the same loop to handle MCQ, True/False, and Short Answer questions without type-specific code.

### Background Timer Implementation

```java
// QuizSession.java - Lines 65-85
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
```

**Justification:** The background timer thread provides real-time countdown functionality without blocking user input, ensuring time limits are enforced even during answer submission.

### Strategy Pattern Implementation

```java
// ScoreEvaluator.java - Lines 8-15
public class ScoreEvaluator {
    private ScoreStrategy strategy;

    public ScoreEvaluator(ScoreStrategy strategy) {
        this.strategy = strategy;
    }

    public ResultReport evaluate(Quiz quiz, Map<Integer, String> answers, String studentName) {
        return strategy.calculate(quiz, answers, studentName);
    }
}
```

**Justification:** This design allows different scoring strategies to be injected at runtime, making the system extensible for future scoring requirements like partial credit or weighted scoring.

### File Persistence with Error Handling

```java
// FileManager.java - Lines 15-35
public static void saveResult(ResultReport report) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(RESULTS_FILE, true))) {
        writer.write("========================================");
        writer.newLine();
        writer.write("Student: " + report.getStudentName());
        writer.newLine();
        writer.write("Quiz: " + report.getQuizTitle());
        writer.newLine();
        writer.write("Score: " + report.getTotalScore() + " / " + report.getMaxScore());
        writer.newLine();
        writer.write(String.format("Percentage: %.2f%%", report.getPercentage()));
        writer.newLine();
        writer.write("Question Results:");
        writer.newLine();
        for (Integer qId : report.getQuestionResults().keySet()) {
            boolean result = report.getQuestionResults().get(qId);
            String status = result ? "CORRECT" : "WRONG";
            writer.write("  Q" + qId + ": " + status);
            writer.newLine();
        }
        writer.write("========================================");
        writer.newLine();
        writer.newLine();
        System.out.println("Result saved successfully to " + RESULTS_FILE);
    } catch (IOException e) {
        System.err.println("Error saving result: " + e.getMessage());
    }
}
```

**Justification:** Robust file I/O with try-with-resources ensures proper resource management and provides user feedback on save operations.

---

## 5. Testing & Error Handling

### Test Cases Considered

The application includes comprehensive unit tests in `UnitTest.java` covering:

1. **Question Type Validation:**
   - MCQ case-insensitive answer matching
   - True/False boolean and text input handling
   - Short Answer exact and case-insensitive matching
   - Score calculation for correct/incorrect answers

2. **Quiz Container Testing:**
   - Question addition and counting
   - Total marks calculation
   - Quiz metadata validation

3. **Scoring Strategy Testing:**
   - Full score calculation for all correct answers
   - Partial scoring for mixed results
   - Percentage calculation accuracy
   - Question result mapping

### Edge Cases Handled

- **Empty Input:** Throws `InvalidInputException` for blank answers
- **Session Timeout:** Background timer automatically ends quiz when time expires
- **File I/O Errors:** Graceful handling of file read/write failures with error messages
- **Invalid Quiz Loading:** Returns null for non-existent quizzes with user feedback
- **Case Insensitivity:** Answers are trimmed and compared case-insensitively where appropriate
- **Thread Interruption:** Timer thread handles interruption gracefully

### Failure Scenarios

1. **Input Stream Closed:** Main loop catches exceptions and exits gracefully
2. **File Not Found:** Load operations return null with error messages
3. **Timer Thread Failure:** Session continues with manual termination option
4. **Memory Constraints:** Uses efficient data structures (ArrayList, HashMap) for scalability
5. **Invalid Question Data:** Parse methods include try-catch blocks for malformed data

---

## 6. Git Workflow


```
2e5ede6 (HEAD -> main, origin/main, origin/HEAD) added bin folder cz forgot
50b206a updated look on github
82a9dc7 Update README.md
292dbfa test: add comprehensive unit test suite
a2ccfb8 feat: add Main application entry point with console UI
1f05132 feat: add FileManager for persistent storage
321365b feat: add QuizSession with background countdown timer
aefdda6 feat: implement Strategy pattern for score evaluation
91a1690 feat: add Quiz container and ResultReport model
540be9b feat: add abstract Question class and implementations
fb93d6d feat: add custom exception classes
f7815e2 build: add build scripts and data directory structure
c96313b docs: add comprehensive project documentation
0460716 chore: add gitignore for Java project
```

### Explanation of Development Progression

The development followed a structured approach with clear separation of concerns:

1. **Initial Setup (0460716, f7815e2):** Project structure and build configuration
2. **Core Architecture (fb93d6d, 540be9b, 91a1690):** Exception classes, question hierarchy, and data models
3. **Business Logic (aefdda6, 321365b, 1f05132):** Scoring strategy, session management, and persistence
4. **User Interface (a2ccfb8):** Console application with menu system
5. **Testing (292dbfa):** Comprehensive unit test suite
6. **Documentation (82a9dc7, 50b206a, 2e5ede6):** README updates and final adjustments

Each commit represents a complete feature implementation, following the principle of atomic commits that introduce one logical change.

---

## 7. Conclusion & Future Scope

### Conclusion

The Online Quiz Engine successfully demonstrates comprehensive application of Object-Oriented Programming principles in Java. The project implements abstraction, inheritance, polymorphism, and encapsulation effectively, while incorporating advanced concepts like threading and design patterns. The Strategy pattern provides extensibility for future scoring methods, and the background timer ensures real-time quiz management.

The application achieves all core requirements: multiple question types, persistent storage, time-limited sessions, and comprehensive error handling. The modular architecture with clear package separation enhances maintainability and testability.

### Future Scope

1. **Web Interface:** Migrate from console to web-based UI using Spring Boot
2. **Database Integration:** Replace file storage with relational database (MySQL/PostgreSQL)
3. **Advanced Question Types:** Add support for image-based, audio, or video questions
4. **Real-time Collaboration:** Multi-user quiz sessions with live leaderboards
5. **Analytics Dashboard:** Detailed performance analytics and quiz statistics
6. **Mobile Application:** Cross-platform mobile app using React Native
7. **Plugin Architecture:** Extensible question type system with plugin support
8. **AI-Powered Features:** Automated question generation and difficulty assessment

---
