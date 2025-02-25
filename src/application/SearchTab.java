package application;

import components.BackButton;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.Task;
import models.Category;
import models.Priority;
import models.Reminder;

import java.util.List;
import java.util.stream.Collectors;

public class SearchTab {

    public static Scene getScene(Stage primaryStage, List<Task> tasks, List<Category> categories, List<Priority> priorities, List<Reminder> reminders) {
        VBox mainLayout = new VBox();
        mainLayout.setSpacing(15);
        mainLayout.setStyle("-fx-padding: 20px; -fx-background-color: #121212;");

        // Header Section
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setSpacing(20);
        headerBox.setStyle("-fx-padding: 15px; -fx-background-color: #1E1E1E;");

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> primaryStage.setScene(new Main().getMainScene(primaryStage, tasks, categories, priorities, reminders)));
        backButton.setStyle("-fx-font-size: 14px; -fx-padding: 8px 15px; -fx-background-color: #3498db; -fx-text-fill: white; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        Label titleLabel = new Label("Task Search");
        titleLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: white;");

        headerBox.getChildren().addAll(backButton, titleLabel);

        // Search Form Section
        GridPane searchGrid = new GridPane();
        searchGrid.setHgap(10);
        searchGrid.setVgap(15);
        searchGrid.setStyle("-fx-padding: 20px; -fx-background-color: #1E1E1E; -fx-border-color: #3498db; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        Label titleLabelInput = new Label("Title:");
        titleLabelInput.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        TextField titleField = new TextField();
        titleField.setPromptText("Enter title");
        titleField.setStyle("-fx-background-color: #2C2C2C; -fx-text-fill: white; -fx-prompt-text-fill: #7F8C8D;");

        Label deadlineLabel = new Label("Deadline:");
        deadlineLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        TextField deadlineField = new TextField();
        deadlineField.setPromptText("yyyy-MM-dd");
        deadlineField.setStyle("-fx-background-color: #2C2C2C; -fx-text-fill: white; -fx-prompt-text-fill: #7F8C8D;");

        Label categoryLabel = new Label("Category:");
        categoryLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().add("All");
        categoryComboBox.getItems().addAll(
            categories.stream().map(Category::getName).distinct().collect(Collectors.toList()));
        categoryComboBox.setValue("All");
        categoryComboBox.setStyle("-fx-background-color: #2C2C2C; -fx-text-fill: white;");

        Label priorityLabel = new Label("Priority:");
        priorityLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        ComboBox<String> priorityComboBox = new ComboBox<>();
        priorityComboBox.getItems().add("All");
        priorityComboBox.getItems().addAll(
            priorities.stream().map(Priority::getName).distinct().collect(Collectors.toList()));
        priorityComboBox.setValue("All");
        priorityComboBox.setStyle("-fx-background-color: #2C2C2C; -fx-text-fill: white;");

        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-font-size: 16px; -fx-padding: 10px; -fx-background-color: #2ecc71; -fx-text-fill: white; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        searchGrid.add(titleLabelInput, 0, 0);
        searchGrid.add(titleField, 1, 0);
        searchGrid.add(deadlineLabel, 0, 1);
        searchGrid.add(deadlineField, 1, 1);
        searchGrid.add(categoryLabel, 0, 2);
        searchGrid.add(categoryComboBox, 1, 2);
        searchGrid.add(priorityLabel, 0, 3);
        searchGrid.add(priorityComboBox, 1, 3);
        searchGrid.add(searchButton, 1, 4);

        // Results Section
        VBox resultsBox = new VBox();
        resultsBox.setSpacing(15);
        resultsBox.setStyle("-fx-padding: 15px; -fx-background-color: #1E1E1E; -fx-border-color: #3498db; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        Label resultsLabel = new Label("Results:");
        resultsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
        resultsBox.getChildren().add(resultsLabel);

        searchButton.setOnAction(event -> {
            String titleQuery = titleField.getText().toLowerCase();
            String deadlineQuery = deadlineField.getText();
            String categoryQuery = categoryComboBox.getValue();
            String priorityQuery = priorityComboBox.getValue();

            List<Task> filteredTasks = tasks.stream()
                    .filter(task -> titleQuery.isEmpty() || task.getTitle().toLowerCase().contains(titleQuery))
                    .filter(task -> deadlineQuery.isEmpty() || task.getDeadline().equals(deadlineQuery))
                    .filter(task -> categoryQuery.equals("All") || task.getCategory().equals(categoryQuery))
                    .filter(task -> priorityQuery.equals("All") || task.getPriority().equals(priorityQuery))
                    .collect(Collectors.toList());

            resultsBox.getChildren().clear();
            resultsBox.getChildren().add(resultsLabel);

            if (filteredTasks.isEmpty()) {
                Label noResultsLabel = new Label("No tasks found.");
                noResultsLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 16px;");
                resultsBox.getChildren().add(noResultsLabel);
            } else {
                for (Task task : filteredTasks) {
                    Label taskLabel = new Label(String.format("ID: %d | Title: %s | Category: %s | Priority: %s",
                            task.getId(), task.getTitle(), task.getCategory(), task.getPriority()));
                    taskLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
                    resultsBox.getChildren().add(taskLabel);
                }
            }
        });

        // Combine all sections
        mainLayout.getChildren().addAll(headerBox, searchGrid, resultsBox);

        return new Scene(mainLayout, 1400, 900);
    }
}

