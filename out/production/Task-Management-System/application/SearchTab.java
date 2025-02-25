package application;

import components.BackButton;
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

        BorderPane layout = new BorderPane();

        BackButton backButton = new BackButton(primaryStage, () -> 
            primaryStage.setScene(new Main().getMainScene(primaryStage, tasks, categories, priorities, reminders))
        );

        Label titleLabel = new Label("Search Tasks");
        titleLabel.setStyle("-fx-font-size: 32; -fx-font-weight: bold; -fx-text-fill: #2E4053;");

        HBox titleBox = new HBox(titleLabel);
        titleBox.setAlignment(javafx.geometry.Pos.CENTER);
        titleBox.setStyle("-fx-padding: 20; -fx-background-color: #F8F9F9;");

        Button placeholderButton = new Button("Search Placeholder");
        placeholderButton.setVisible(false); 
        placeholderButton.setStyle("-fx-font-size: 16; -fx-padding: 10; -fx-background-color: #566573; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");

        HBox topRightBox = new HBox(placeholderButton);
        topRightBox.setAlignment(javafx.geometry.Pos.TOP_RIGHT);
        topRightBox.setStyle("-fx-padding: 20 20 0 0;");

        BorderPane topPane = new BorderPane();
        topPane.setLeft(backButton);
        topPane.setCenter(titleBox);
        topPane.setRight(topRightBox);

        layout.setTop(topPane);

        VBox content = new VBox();
        content.setSpacing(20);
        content.setStyle("-fx-padding: 20;");

        TextField titleField = new TextField();
        titleField.setPromptText("Search by Title");

        TextField deadlineField = new TextField();
        deadlineField.setPromptText("Search by Deadline(yyyy-MM-dd)");

        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().add("All");
        categoryComboBox.getItems().addAll(categories.stream().map(Category::getName).collect(Collectors.toList()));
        categoryComboBox.setValue("All");

        ComboBox<String> priorityComboBox = new ComboBox<>();
        priorityComboBox.getItems().add("All");
        priorityComboBox.getItems().addAll(priorities.stream().map(Priority::getName).collect(Collectors.toList()));
        priorityComboBox.setValue("All");

        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-font-size: 16; -fx-padding: 10; -fx-background-color: #007B83; -fx-text-fill: white;");

        VBox resultsBox = new VBox();
        resultsBox.setSpacing(10);
        resultsBox.setStyle("-fx-padding: 10; -fx-background-color: #FBFCFC;");

        searchButton.setOnAction(e -> {
            String titleQuery = titleField.getText().toLowerCase();
            String deadlineQuery = deadlineField.getText().toLowerCase();
            String categoryQuery = categoryComboBox.getValue();
            String priorityQuery = priorityComboBox.getValue();

            List<Task> filteredTasks = tasks.stream()
                    .filter(task -> titleQuery.isEmpty() || task.getTitle().toLowerCase().contains(titleQuery))
                    .filter(task -> deadlineQuery.isEmpty() || task.getDeadline().equals(deadlineQuery))
                    .filter(task -> categoryQuery == null || categoryQuery.equals("All") || task.getCategory().equals(categoryQuery))
                    .filter(task -> priorityQuery == null || priorityQuery.equals("All") || task.getPriority().equals(priorityQuery))
                    .collect(Collectors.toList());

            resultsBox.getChildren().clear();

            if (filteredTasks.isEmpty()) {
                resultsBox.getChildren().add(new Label("No tasks found."));
            } else {
                for (Task task : filteredTasks) {
                    Label taskLabel = new Label(
                            "ID: " + task.getId() + " | Title: " + task.getTitle() +
                            " | Category: " + task.getCategory() + " | Priority: " + task.getPriority() + " | Deadline: " + task.getDeadline()
                    );
                    resultsBox.getChildren().add(taskLabel);
                }
            }
        });

        VBox searchBox = new VBox(10, titleField, deadlineField, categoryComboBox, priorityComboBox, searchButton);
        searchBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        searchBox.setStyle("-fx-padding: 20; -fx-background-color: #F4F6F7; -fx-border-color: #D5D8DC; -fx-border-radius: 5px;");

        content.getChildren().addAll(searchBox, resultsBox);
        layout.setCenter(content);

        return new Scene(layout, 800, 600);
    }
}
