package application;

import components.BackButton;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.Priority;
import models.Task;
import models.Category;
import models.Reminder;

import java.util.List;
import java.util.ArrayList;

public class PriorityManagement {
    public static Scene getScene(Stage primaryStage, List<Task> tasks, List<Category> categories, List<Priority> priorities, List<Reminder> reminders) {
        final List<Task> finalTasks = tasks != null ? tasks : new ArrayList<>();
        final List<Category> finalCategories = categories != null ? categories : new ArrayList<>();
        final List<Priority> finalPriorities = priorities != null ? priorities : new ArrayList<>();
        final List<Reminder> finalReminders = reminders != null ? reminders : new ArrayList<>();

        BorderPane layout = new BorderPane();

        BackButton backButton = new BackButton(primaryStage, () -> {
            primaryStage.setScene(new Main().getMainScene(primaryStage, finalTasks, finalCategories, finalPriorities, finalReminders));
        });

        Label titleLabel = new Label("Priority Management");
        titleLabel.setStyle("-fx-font-size: 32; -fx-font-weight: bold; -fx-text-fill: #2E4053;");

        HBox titleBox = new HBox(titleLabel);
        titleBox.setAlignment(javafx.geometry.Pos.CENTER);
        titleBox.setStyle("-fx-padding: 20; -fx-background-color: #F8F9F9;");

        BorderPane topPane = new BorderPane();
        topPane.setLeft(backButton);
        topPane.setCenter(titleBox);

        layout.setTop(topPane);

        VBox content = new VBox();
        content.setSpacing(20);

        for (Priority priority : finalPriorities) {
            BorderPane priorityBox = new BorderPane();
            priorityBox.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #D5D8DC; -fx-border-radius: 5; -fx-padding: 10;");

            Label priorityLabel = new Label(priority.getName() + " (" + priority.getTasks().size() + " tasks)");
            priorityLabel.setStyle("-fx-font-size: 18; -fx-text-fill: #2E4053;");

            priorityBox.setLeft(priorityLabel);

            HBox actionButtons = new HBox(10);
            actionButtons.setStyle("-fx-padding: 5;");

            final Priority finalPriority = priority;
            Button deleteButton = new Button("REMOVE");
            deleteButton.setStyle("-fx-font-size: 16; -fx-background-color: transparent; -fx-border: none; -fx-text-fill: #922B21;");
            deleteButton.setOnAction(e -> {
                String priorityNameToRemove = finalPriority.getName();

                finalTasks.stream()
                    .filter(task -> task.getPriority().equals(priorityNameToRemove))
                    .forEach(task -> task.setPriority("Default"));

                finalCategories.forEach(category -> 
                    category.getTasks().stream()
                        .filter(task -> task.getPriority().equals(priorityNameToRemove))
                        .forEach(task -> task.setPriority("Default"))
                );

                finalPriorities.remove(finalPriority);

                primaryStage.setScene(getScene(primaryStage, finalTasks, finalCategories, finalPriorities, finalReminders));
            });

            Button editButton = new Button("EDIT");
            editButton.setStyle("-fx-font-size: 16; -fx-background-color: transparent; -fx-border: none; -fx-text-fill: #1E8449;");
            editButton.setOnAction(e -> 
                openEditPriorityForm(primaryStage, finalTasks, finalCategories, finalPriorities, finalReminders, finalPriority)
            );

            actionButtons.getChildren().addAll(editButton, deleteButton);
            priorityBox.setRight(actionButtons);

            content.getChildren().add(priorityBox);
        }

        if (finalPriorities.isEmpty()) {
            Label noPrioritiesLabel = new Label("No priorities available.");
            noPrioritiesLabel.setStyle("-fx-font-size: 16; -fx-text-fill: #2E4053;");
            content.getChildren().add(noPrioritiesLabel);
        }

        content.setStyle("-fx-padding: 20; -fx-background-color: #FBFCFC;");

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        layout.setCenter(scrollPane);

        return new Scene(layout, 800, 600);
    }

    private static void openEditPriorityForm(Stage primaryStage, List<Task> tasks, List<Category> categories, List<Priority> priorities, List<Reminder> reminders, Priority priorityToEdit) {
        VBox formLayout = new VBox();
        formLayout.setSpacing(15);
        formLayout.setStyle("-fx-padding: 20;");

        String formTitleText = "Edit Priority";
        Label formTitle = new Label(formTitleText);
        formTitle.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");

        TextField priorityNameField = new TextField();
        priorityNameField.setPromptText("Priority Name");
        if (priorityToEdit != null) priorityNameField.setText(priorityToEdit.getName());

        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-font-size: 16; -fx-padding: 10; -fx-background-color: #1E8449; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        saveButton.setOnAction(e -> {
            String newPriorityName = priorityNameField.getText();

            if (newPriorityName.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Priority name is required!", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            if (priorityToEdit != null) {
                String oldPriorityName = priorityToEdit.getName();

                priorityToEdit.setName(newPriorityName);

                tasks.stream()
                    .filter(task -> task.getPriority().equals(oldPriorityName))
                    .forEach(task -> task.setPriority(newPriorityName));

                categories.forEach(category -> 
                    category.getTasks().stream()
                        .filter(task -> task.getPriority().equals(oldPriorityName))
                        .forEach(task -> task.setPriority(newPriorityName))
                );
            }

            primaryStage.setScene(getScene(primaryStage, tasks, categories, priorities, reminders));
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-font-size: 16; -fx-padding: 10; -fx-background-color: #922B21; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        cancelButton.setOnAction(e -> primaryStage.setScene(getScene(primaryStage, tasks, categories, priorities, reminders)));

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setStyle("-fx-padding: 10;");
        buttonBox.setAlignment(javafx.geometry.Pos.BASELINE_LEFT);

        formLayout.getChildren().addAll(formTitle, priorityNameField, buttonBox);

        Scene formScene = new Scene(formLayout, 800, 600);
        primaryStage.setScene(formScene);
    }
}