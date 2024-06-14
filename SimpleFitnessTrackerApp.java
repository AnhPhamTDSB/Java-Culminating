import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SimpleFitnessTrackerApp extends Application {

    // Food tracker UI components
    private ListView<String> morningHistoryView;  // List to store morning food history
    private ListView<String> lunchHistoryView;    // List to store lunch food history
    private ListView<String> dinnerHistoryView;   // List to store dinner food history
    private int totalCalories = 0;                // Total calories consumed counter
    private Label totalCaloriesLabel;             // Label to display total calories

    // Fitness tracker UI components
    private ListView<String> fitnessHistoryView;  // List to store fitness activity history
    private int totalWorkoutMinutes = 0;          // Total workout minutes counter
    private int totalCaloriesBurned = 0;          // Total calories burned counter
    private Label totalWorkoutMinutesLabel;       // Label to display total workout minutes
    private Label totalCaloriesBurnedLabel;       // Label to display total calories burned

    // Workout planner UI components
    private ListView<String> workoutPlanView;     // List to display workout plans
    private ObservableList<String> workoutPlans;  // Observable list to manage workout plans
    private TextArea workoutTextArea;            // Text area for adding/editing workout plans

    // Calendar UI components
    private DatePicker calendarPicker;            // Date picker for selecting dates
    private VBox calendarView;                    // Vertical box to contain calendar UI elements

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Fitlife");

        // Initialize workout plans list
        workoutPlans = FXCollections.observableArrayList();
        workoutPlanView = new ListView<>(workoutPlans);

        // Initialize workout planner UI components
        Label workoutPlanLabel = new Label("Workout Plans");
        workoutTextArea = new TextArea();
        workoutTextArea.setPromptText("Add exercises here...");
        workoutTextArea.setPrefHeight(150);
        Button addWorkoutPlanButton = new Button("Add Workout Plan");
        Button saveWorkoutPlanButton = new Button("Save Workout Plan");

        // Action event for adding a new workout plan
        addWorkoutPlanButton.setOnAction(e -> {
            String planDescription = workoutTextArea.getText();

            if (!planDescription.isEmpty()) {
                String selectedDate = calendarPicker.getValue() != null ? calendarPicker.getValue().toString() : "";
                String planEntry = selectedDate.isEmpty() ? planDescription : planDescription + " (Date: " + selectedDate + ")";
                workoutPlans.add(planEntry);
                workoutTextArea.clear();
            }
        });

        // Action event for saving a workout plan
        saveWorkoutPlanButton.setOnAction(e -> {
            int selectedIndex = workoutPlanView.getSelectionModel().getSelectedIndex();
            if (selectedIndex != -1) {
                String planDescription = workoutTextArea.getText();
                String selectedDate = calendarPicker.getValue() != null ? calendarPicker.getValue().toString() : "";
                String planEntry = selectedDate.isEmpty() ? planDescription : planDescription + " (Date: " + selectedDate + ")";
                workoutPlans.set(selectedIndex, planEntry);
                workoutTextArea.clear();
            }
        });

        // Initialize UI for food tracker
        totalCaloriesLabel = new Label("Total Calories: 0");
        TextField foodInput = new TextField();
        foodInput.setPromptText("Enter food (e.g., banana, pizza, soda)");
        ComboBox<String> timeOfDay = new ComboBox<>();
        timeOfDay.getItems().addAll("Morning", "Lunch", "Dinner");
        timeOfDay.setPromptText("Select time of day");
        Button addFoodButton = new Button("Add Food");

        morningHistoryView = new ListView<>();
        lunchHistoryView = new ListView<>();
        dinnerHistoryView = new ListView<>();

        // Action event for the Add Food button
        addFoodButton.setOnAction(e -> {
            String food = foodInput.getText().toLowerCase();
            String time = timeOfDay.getValue();
            int calories = 0;

            // Determine calories based on food
            switch (food) {
                case "banana":
                    calories = 105;
                    break;
                case "pizza":
                    calories = 272;
                    break;
                case "soda":
                    calories = 190;
                    break;
                default:
                    foodInput.clear();
                    return;
            }

            // Add food to the respective list and update calories
            if (time != null) {
                String entry = food + " - " + calories + " calories";
                switch (time) {
                    case "Morning":
                        morningHistoryView.getItems().add(entry);
                        break;
                    case "Lunch":
                        lunchHistoryView.getItems().add(entry);
                        break;
                    case "Dinner":
                        dinnerHistoryView.getItems().add(entry);
                        break;
                }
                totalCalories += calories;
                totalCaloriesLabel.setText("Total Calories: " + totalCalories);
                foodInput.clear();
            }
        });

        // Initialize UI for fitness tracker
        totalWorkoutMinutesLabel = new Label("Total Workout Minutes: 0");
        totalCaloriesBurnedLabel = new Label("Total Calories Burned: 0");
        ComboBox<String> workoutInput = new ComboBox<>();
        workoutInput.getItems().addAll("Running", "Cycling", "Swimming");
        workoutInput.setPromptText("Select workout");
        TextField minutesInput = new TextField();
        minutesInput.setPromptText("Enter minutes");
        Button addWorkoutButton = new Button("Add Workout");

        fitnessHistoryView = new ListView<>();

        // Action event for the Add Workout button
        addWorkoutButton.setOnAction(e -> {
            String workout = workoutInput.getValue();
            int minutes;
            try {
                minutes = Integer.parseInt(minutesInput.getText());
            } catch (NumberFormatException ex) {
                workoutInput.getSelectionModel().clearSelection();
                minutesInput.clear();
                return;
            }

            if (workout != null && !workout.isEmpty()) {
                int caloriesBurned = 0;
                switch (workout) {
                    case "Running":
                        caloriesBurned = 10 * minutes; // Example calories burned per minute for Running
                        break;
                    case "Cycling":
                        caloriesBurned = 8 * minutes; // Example calories burned per minute for Cycling
                        break;
                    case "Swimming":
                        caloriesBurned = 9 * minutes; // Example calories burned per minute for Swimming
                        break;
                }

                String entry = workout + " - " + minutes + " minutes - " + caloriesBurned + " calories burned";
                fitnessHistoryView.getItems().add(entry);
                totalWorkoutMinutes += minutes;
                totalCaloriesBurned += caloriesBurned;
                totalWorkoutMinutesLabel.setText("Total Workout Minutes: " + totalWorkoutMinutes);
                totalCaloriesBurnedLabel.setText("Total Calories Burned: " + totalCaloriesBurned);
                workoutInput.getSelectionModel().clearSelection();
                minutesInput.clear();
            }
        });

        // Initialize UI for calendar view
        calendarPicker = new DatePicker();
        calendarPicker.setPromptText("Select date");

        calendarView = new VBox(10);
        calendarView.getChildren().addAll(
                new Label("Workout Plans"),
                calendarPicker,
                new Label("Workout Plan Details"),
                workoutPlanView,
                workoutTextArea,
                new HBox(addWorkoutPlanButton, saveWorkoutPlanButton)
        );

        calendarView.setPadding(new Insets(10));

        // Layout for food tracker
        VBox foodLayout = new VBox(10);
        foodLayout.getStyleClass().add("food-tracker");
        foodLayout.getChildren().addAll(
                new Label("Food Tracker"),
                new HBox(foodInput, timeOfDay, addFoodButton),
                new Label("Morning"),
                morningHistoryView,
                new Label("Lunch"),
                lunchHistoryView,
                new Label("Dinner"),
                dinnerHistoryView,
                totalCaloriesLabel
        );

        // Layout for fitness tracker
        VBox fitnessLayout = new VBox(10);
        fitnessLayout.getStyleClass().add("fitness-tracker");
        fitnessLayout.getChildren().addAll(
                new Label("Fitness Tracker"),
                new HBox(workoutInput, minutesInput, addWorkoutButton),
                totalWorkoutMinutesLabel,
                totalCaloriesBurnedLabel,
                fitnessHistoryView
        );

        // Layout for workout planner
        calendarView.getStyleClass().add("workout-planner");

        // Create tabs for different sections
        TabPane tabPane = new TabPane();

        Tab foodTab = new Tab("Food Tracker", foodLayout);
        Tab fitnessTab = new Tab("Fitness Tracker", fitnessLayout);
        Tab plannerTab = new Tab("Workout Planner", calendarView);

        tabPane.getTabs().addAll(foodTab, fitnessTab, plannerTab);

        // Set up the scene and display the primary stage
        Scene scene = new Scene(tabPane, 800, 600); // Set scene dimensions
        scene.getStylesheets().add("styles.css");   // Add CSS styling if desired
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);  // Launch the JavaFX application
    }
}