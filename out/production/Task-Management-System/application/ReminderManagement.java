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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReminderManagement {
    public static Scene getScene(Stage primaryStage, List<Task> tasks, List<Category> categories, List<Priority> priorities, List<Reminder> reminders) {
        BorderPane layout = new BorderPane();

        BackButton backButton = new BackButton(primaryStage, () -> {
            primaryStage.setScene(new Main().getMainScene(primaryStage, tasks, categories, priorities, reminders));
        });

        Label titleLabel = new Label("Reminder Management");
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

        List<Task> tasksWithReminders = tasks.stream()
                .filter(task -> !task.getReminders().isEmpty())
                .collect(Collectors.toList());

        for (Task task : tasksWithReminders) {
            BorderPane taskBox = new BorderPane();
            taskBox.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #D5D8DC; -fx-border-radius: 5; -fx-padding: 10;");

            Label taskLabel = new Label("Task: " + task.getTitle());
            taskLabel.setStyle("-fx-font-size: 18; -fx-text-fill: #2E4053;");

            VBox remindersBox = new VBox();
            remindersBox.setSpacing(10);
            remindersBox.setStyle("-fx-padding: 10;");

            for (Reminder reminder : task.getReminders()) {
                BorderPane reminderBox = new BorderPane();
                reminderBox.setStyle("-fx-background-color: #FBFCFC; -fx-border-color: #D5D8DC; -fx-border-radius: 5; -fx-padding: 5;");

                Label reminderLabel = new Label("Reminder: " + reminder.getDate());
                reminderLabel.setStyle("-fx-font-size: 16; -fx-text-fill: #2E4053;");
                reminderBox.setLeft(reminderLabel);

                HBox actionButtons = new HBox(10);
                actionButtons.setStyle("-fx-padding: 5;");

                Button editButton = new Button("EDIT");
                editButton.setStyle("-fx-font-size: 16; -fx-background-color: transparent; -fx-border: none; -fx-text-fill: #1E8449;");
                editButton.setOnAction(e -> openEditReminderForm(primaryStage, tasks, categories, priorities, reminders, task, reminder));

                Button deleteButton = new Button("REMOVE");
                deleteButton.setStyle("-fx-font-size: 16; -fx-background-color: transparent; -fx-border: none; -fx-text-fill: #922B21;");
                deleteButton.setOnAction(e -> {
                    task.getReminders().remove(reminder);
                    updateTaskInCategoriesAndPriorities(categories, priorities, task);
                    primaryStage.setScene(getScene(primaryStage, tasks, categories, priorities, reminders));
                });

                actionButtons.getChildren().addAll(editButton, deleteButton);
                reminderBox.setRight(actionButtons);

                remindersBox.getChildren().add(reminderBox);
            }

            taskBox.setTop(taskLabel);
            taskBox.setCenter(remindersBox);
            content.getChildren().add(taskBox);
        }

        if (tasksWithReminders.isEmpty()) {
            Label noRemindersLabel = new Label("No reminders available.");
            noRemindersLabel.setStyle("-fx-font-size: 16; -fx-text-fill: #2E4053;");
            content.getChildren().add(noRemindersLabel);
        }

        content.setStyle("-fx-padding: 20; -fx-background-color: #FBFCFC;");
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        layout.setCenter(scrollPane);

        return new Scene(layout, 800, 600);
    }

    private static void openEditReminderForm(Stage primaryStage, List<Task> tasks, List<Category> categories, List<Priority> priorities, List<Reminder> reminders, Task task, Reminder reminderToEdit) {
        VBox formLayout = new VBox();
        formLayout.setSpacing(15);
        formLayout.setStyle("-fx-padding: 20;");

        String formTitleText = "Edit Reminder";
        Label formTitle = new Label(formTitleText);
        formTitle.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");

        TextField reminderDateField = new TextField();
        reminderDateField.setPromptText("Reminder Date (yyyy-MM-dd)");
        reminderDateField.setText(reminderToEdit.getDate());

        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-font-size: 16; -fx-padding: 10; -fx-background-color: #1E8449; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        saveButton.setOnAction(e -> {
            String newDate = reminderDateField.getText();
            LocalDate taskDeadline = LocalDate.parse(task.getDeadline());
            LocalDate reminderDate;

            try {
                reminderDate = LocalDate.parse(newDate);
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid date format. Use yyyy-MM-dd.", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            if (!reminderDate.isBefore(taskDeadline)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Reminder date must be before the task deadline: " + task.getDeadline(), ButtonType.OK);
                alert.showAndWait();
                return;
            }

            reminderToEdit.setDate(newDate);
            updateTaskInCategoriesAndPriorities(categories, priorities, task);

            primaryStage.setScene(getScene(primaryStage, tasks, categories, priorities, reminders));
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-font-size: 16; -fx-padding: 10; -fx-background-color: #922B21; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        cancelButton.setOnAction(e -> primaryStage.setScene(getScene(primaryStage, tasks, categories, priorities, reminders)));

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setStyle("-fx-padding: 10;");
        buttonBox.setAlignment(javafx.geometry.Pos.BASELINE_LEFT);

        formLayout.getChildren().addAll(formTitle, reminderDateField, buttonBox);

        Scene formScene = new Scene(formLayout, 800, 600);
        primaryStage.setScene(formScene);
    }

    private static void updateTaskInCategoriesAndPriorities(List<Category> categories, List<Priority> priorities, Task task) {
        categories.forEach(category -> category.getTasks().stream()
                .filter(t -> t.getId() == task.getId())
                .forEach(t -> t.setReminders(task.getReminders())));

        priorities.forEach(priority -> priority.getTasks().stream()
                .filter(t -> t.getId() == task.getId())
                .forEach(t -> t.setReminders(task.getReminders())));
    }
}