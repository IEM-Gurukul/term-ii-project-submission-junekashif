[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/pG3gvzt-)
# PCCCS495 – Term II Project

##Online Quiz Engine
---

## Problem Statement (max 150 words)
The Online Quiz Engine is a comprehensive Java-based application designed to facilitate the creation, administration, and evaluation of online quizzes. The system supports multiple question types including Multiple Choice Questions (MCQ), True/False questions, and Short Answer questions. It provides real-time quiz sessions with background countdown timers, persistent storage for quizzes and results, and a strategy-based scoring system.

The application addresses the need for an automated quiz management system that can handle different question formats, enforce time limits, provide immediate feedback, and maintain records of quiz attempts. Key challenges include implementing polymorphic question evaluation, managing concurrent timer threads, ensuring data persistence, and providing a user-friendly console interface.

---

## Target User
The Online Quiz Engine targets educators, instructors, and educational institutions who need to create, administer, and evaluate quizzes for students. It supports various question types and provides automated scoring and result tracking, making it suitable for academic assessments, training programs, and certification exams.
---

## Core Features

- Multiple Question Types: Support for Multiple Choice Questions (MCQ), True/False, and Short Answer questions
- Timed Quiz Sessions: Background countdown timer that enforces time limits and auto-terminates sessions
- Persistent Storage: File-based storage for saving/loading quizzes and storing quiz results
- Automated Scoring: Strategy pattern implementation for flexible score evaluation with immediate feedback
- User-Friendly Interface: Console-based menu system for quiz creation, loading, and result viewing
- Error Handling: Custom exceptions for input validation and session management
- Comprehensive Testing: Unit test suite covering all components and edge cases
- Modular Architecture: Clean separation of concerns with packages for core logic, models, storage, and exceptions

---

## OOP Concepts Used

- Abstraction
- Inheritance
- Polymorphism
- Exception Handling
- Collections / Threads 

---

## Proposed Architecture Description
Follows a modular, layered design with clear separation of concerns:

Layered Architecture:

Presentation Layer: Main.java provides console-based UI with menu-driven interaction
Business Logic Layer: core package handles quiz sessions, scoring, and session management
Data Model Layer: model package contains question types and data structures
Persistence Layer: storage package manages file I/O for quizzes and results
Exception Layer: exceptions package provides custom error handling

Key Architectural Patterns:

Abstract Factory: Question hierarchy with abstract base class
Strategy Pattern: Pluggable scoring algorithms via ScoreStrategy interface
Observer Pattern: Timer thread monitors session state
Repository Pattern: FileManager abstracts data persistence operations

Interaction Flow:

User initiates quiz via Main menu
FileManager loads quiz data from files
QuizSession starts with background timer thread
Questions displayed polymorphically through Question interface
Answers collected and validated with custom exceptions
ScoreEvaluator applies scoring strategy
Results saved to file and displayed to user

Design Principles:

Single Responsibility: Each class has one primary function
Open/Closed: Extensible through inheritance and interfaces
Dependency Inversion: High-level modules depend on abstractions
Interface Segregation: Focused interfaces for specific behaviors
This architecture ensures maintainability, testability, and extensibility for future enhancements.

---

## How to Run

1. Go to the directory where the main method is.
2. Open terminal
3. Run java Main.java
---

## Git Discipline Notes

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
