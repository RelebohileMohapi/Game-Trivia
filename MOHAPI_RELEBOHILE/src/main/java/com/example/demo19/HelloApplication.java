package com.example.demo19;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {

    private int currentQuestionIndex = 0;
    private int score = 0;
    private List<Question> questions;

    private Label questionLabel;
    private Button answer1Button;
    private Button answer2Button;
    private Button answer3Button;
    private Button answer4Button;
    private Label feedbackLabel;
    private Label scoreLabel;
    private Label progressLabel;
    private Label questionNumberLabel;
    private Button nextButton;

    private Timeline timeline;
    private boolean isFirstResponse = true;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Lesotho Trivia Game");

        // Initialize questions
        initializeQuestions();

        // UI components
        questionLabel = new Label();
        questionLabel.getStyleClass().add("question-label");

        answer1Button = new Button();
        answer1Button.getStyleClass().add("answer-button");
        answer2Button = new Button();
        answer2Button.getStyleClass().add("answer-button");
        answer3Button = new Button();
        answer3Button.getStyleClass().add("answer-button");
        answer4Button = new Button();
        answer4Button.getStyleClass().add("answer-button");

        feedbackLabel = new Label();
        feedbackLabel.getStyleClass().add("feedback-label");
        scoreLabel = new Label("Score: 0");


        progressLabel = new Label();
        
        questionNumberLabel = new Label();
        nextButton = new Button("Next Question");
        nextButton.getStyleClass().add("next-button");
        nextButton.setDisable(true);

        // Load initial question
        loadQuestion();

        // Layout setup
        VBox questionLayout = new VBox(10);
        questionLayout.setAlignment(Pos.TOP_CENTER);
        questionLayout.getChildren().addAll(questionLabel, createAnswersContainer(), feedbackLabel, nextButton, scoreLabel, questionNumberLabel);
        questionLayout.getStyleClass().add("question-container");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setCenter(questionLayout);
        root.setBottom(progressLabel);

        // Event handlers for answer buttons
        answer1Button.setOnAction(e -> checkAnswer(answer1Button.getText()));
        answer2Button.setOnAction(e -> checkAnswer(answer2Button.getText()));
        answer3Button.setOnAction(e -> checkAnswer(answer3Button.getText()));
        answer4Button.setOnAction(e -> checkAnswer(answer4Button.getText()));
        nextButton.setOnAction(e -> loadNextQuestion());

        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeQuestions() {
        // Initialize questions
        questions = new ArrayList<>();
        questions.add(new Question("What is the capital of Lesotho?", "A. Maseru", "B. Johannesburg", "C. Cape Town", "D. Nairobi", "A. Maseru", "maseru.jpg"));
        questions.add(new Question("Which mountain range covers much of Lesotho?", "A. Andes", "B. Himalayas", "C. Alps", "D. Drakensberg", "D. Drakensberg", "drakensberg.jpg"));
        questions.add(new Question("What is the traditional Basotho hut called?", "A. Igloo", "B. Rondavel", "C. Teepee", "D. Yurt", "B. Rondavel", "rondavel.jpg"));
        questions.add(new Question("Which river forms part of the border between Lesotho and South Africa?", "A. Zambezi", "B. Nile", "C. Orange-River", "D. Congo", "C. Orange-River", "orange_river.jpg"));
        questions.add(new Question("What is the currency of Lesotho?", "A. Loti", "B. Dollar", "C. Euro", "D. Pula", "A. Loti", "lesotho_currency.jpg"));
    }

    private HBox createAnswersContainer() {
        HBox answersContainer = new HBox(10);
        answersContainer.setAlignment(Pos.CENTER);
        answersContainer.getChildren().addAll(answer1Button, answer2Button, answer3Button, answer4Button);
        answersContainer.getStyleClass().add("answers-container");
        return answersContainer;
    }

    private void loadQuestion() {
        Question currentQuestion = questions.get(currentQuestionIndex);
        questionLabel.setText(currentQuestion.question());
        answer1Button.setText(currentQuestion.option1());
        answer2Button.setText(currentQuestion.option2());
        answer3Button.setText(currentQuestion.option3());
        answer4Button.setText(currentQuestion.option4());
        feedbackLabel.setText("");
        progressLabel.setText("Question " + (currentQuestionIndex + 1) + " of " + questions.size());
        questionNumberLabel.setText("Question Number: " + (currentQuestionIndex + 1));
        loadQuestionImage(currentQuestion.imageFileName());
    }

    private void loadQuestionImage(String fileName) {
        ImageView imageView = new ImageView(new Image(fileName));
        imageView.setFitHeight(200);
        imageView.setFitWidth(300);
        questionLabel.setGraphic(imageView);
    }

    private void checkAnswer(String selectedAnswer) {
        // Stop time tracking if this is the first response
        if (isFirstResponse) {
            stopTimer();
            isFirstResponse = false;
            nextButton.setDisable(false); // Enable next button after first response
        }

        Question currentQuestion = questions.get(currentQuestionIndex);
        if (selectedAnswer.equals(currentQuestion.correctAnswer())) {
            feedbackLabel.setText("Correct!");
            score++;
        } else {
            feedbackLabel.setText("Incorrect! Correct answer is: " + currentQuestion.correctAnswer());
        }
        scoreLabel.setText("Score: " + score);
    }

    private void loadNextQuestion() {
        // Move to next question
        currentQuestionIndex++;
        if (currentQuestionIndex < questions.size()) {
            loadQuestion();
            feedbackLabel.setText(""); // Clear previous feedback
            isFirstResponse = true; // Set flag for the first response
            startTimer(); // Start timer for the next question
            nextButton.setDisable(true); // Disable next button until the first response for the next question
        } else {
            endGame();
        }
    }

    private void endGame() {
        // Display final score
        feedbackLabel.setText("Game Over! Your final score is: " + score + "/" + questions.size());
        nextButton.setDisable(true); // Disable next button as game is over
        // Optional: Add more end game features here
    }

    private void startTimer() {
        // Start the timer for 10 seconds
        timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.seconds(10), e -> checkAnswer("")),
                new KeyFrame(Duration.seconds(0), e -> progressLabel.setText("Time: 10")),
                new KeyFrame(Duration.seconds(1), e -> progressLabel.setText("Time: 9")),
                new KeyFrame(Duration.seconds(2), e -> progressLabel.setText("Time: 8")),
                new KeyFrame(Duration.seconds(3), e -> progressLabel.setText("Time: 7")),
                new KeyFrame(Duration.seconds(4), e -> progressLabel.setText("Time: 6")),
                new KeyFrame(Duration.seconds(5), e -> progressLabel.setText("Time: 5")),
                new KeyFrame(Duration.seconds(6), e -> progressLabel.setText("Time: 4")),
                new KeyFrame(Duration.seconds(7), e -> progressLabel.setText("Time: 3")),
                new KeyFrame(Duration.seconds(8), e -> progressLabel.setText("Time: 2")),
                new KeyFrame(Duration.seconds(9), e -> progressLabel.setText("Time: 1")),
                new KeyFrame(Duration.seconds(10), e -> {
                    progressLabel.setText("Time: 0");
                    checkAnswer("");
                })
        );
        timeline.setCycleCount(1);
        timeline.play();
    }

    private void stopTimer() {
        // Stop the timer
        if (timeline != null) {
            timeline.stop();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

record Question(String question, String option1, String option2, String option3, String option4, String correctAnswer,
                String imageFileName) {
}
