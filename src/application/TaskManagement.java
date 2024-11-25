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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskManagement {
    public static Scene getScene(Stage primaryStage, List<Task> tasks, List<Category> categories, List<Priority> priorities, List<Reminder> reminders) {
        BorderPane layout = new BorderPane();

        BackButton backButton = new BackButton(primaryStage, () -> {
            primaryStage.setScene(new Main().getMainScene(primaryStage, tasks, categories, priorities, reminders));
        });

        Label titleLabel = new Label("Task Management");
        titleLabel.setStyle("-fx-font-size: 32; -fx-font-weight: bold; -fx-text-fill: #2E4053;");

        HBox titleBox = new HBox(titleLabel);
        titleBox.setAlignment(javafx.geometry.Pos.CENTER);
        titleBox.setStyle("-fx-padding: 20; -fx-background-color: #F8F9F9;");

        Button addTaskButton = new Button("New Task");
        addTaskButton.setStyle("-fx-font-size: 16; -fx-padding: 10; -fx-background-color: #566573; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        addTaskButton.setOnAction(e -> openAddTaskForm(primaryStage, tasks, categories, priorities, reminders));

        HBox topRightBox = new HBox(addTaskButton);
        topRightBox.setAlignment(javafx.geometry.Pos.TOP_RIGHT);
        topRightBox.setStyle("-fx-padding: 20 20 0 0;");

        BorderPane topPane = new BorderPane();
        topPane.setLeft(backButton);
        topPane.setCenter(titleBox);
        topPane.setRight(topRightBox);

        layout.setTop(topPane);

        VBox content = new VBox();
        content.setSpacing(20);

        Map<String, List<Task>> tasksByCategory = tasks.stream()
                .collect(Collectors.groupingBy(Task::getCategory));

        for (Map.Entry<String, List<Task>> entry : tasksByCategory.entrySet()) {
            String category = entry.getKey();
            List<Task> tasksInCategory = entry.getValue();

            Label categoryLabel = new Label(category);
            categoryLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

            VBox tasksBox = new VBox();
            tasksBox.setSpacing(10);
            tasksBox.setStyle("-fx-padding: 10;");

            for (Task task : tasksInCategory) {
                BorderPane taskBox = new BorderPane();
                taskBox.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #D5D8DC; -fx-border-radius: 5; -fx-padding: 5;");

                Label taskLabel = new Label(task.getTitle() + ": " + task.getDescription() + " (ID: " + task.getId() + ")");
                taskLabel.setStyle("-fx-font-size: 16; -fx-text-fill: #2E4053;");
                taskBox.setLeft(taskLabel);

                HBox actionButtons = new HBox(10);
                actionButtons.setStyle("-fx-padding: 5;");

                Button deleteButton = new Button("REMOVE");
                deleteButton.setStyle("-fx-font-size: 16; -fx-background-color: transparent; -fx-border: none; -fx-text-fill: #922B21;");
                deleteButton.setOnAction(e -> {
                    tasks.remove(task);

                    for (Category cat : categories) {
                        cat.getTasks().removeIf(t -> t.equals(task));
                    }

                    for (Priority priority : priorities) {
                        priority.getTasks().removeIf(t -> t.equals(task));
                    }

                    if (reminders != null) {
                        reminders.removeIf(reminder -> reminder.getTaskId() == task.getId());
                    }

                    primaryStage.setScene(getScene(primaryStage, tasks, categories, priorities, reminders));
                });

                Button editButton = new Button("VIEW/EDIT");
                editButton.setStyle("-fx-font-size: 16; -fx-background-color: transparent; -fx-border: none; -fx-text-fill: #1E8449;");
                editButton.setOnAction(e -> openEditTaskForm(primaryStage, tasks, categories, priorities, reminders, task));

                actionButtons.getChildren().addAll(editButton, deleteButton);
                taskBox.setRight(actionButtons);

                tasksBox.getChildren().add(taskBox);
            }

            content.getChildren().addAll(categoryLabel, tasksBox);
        }

        content.setStyle("-fx-padding: 20; -fx-background-color: #FBFCFC;");

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        layout.setCenter(scrollPane);

        return new Scene(layout, 800, 600);
    }

    private static void openAddTaskForm(Stage primaryStage, List<Task> tasks, List<Category> categories, List<Priority> priorities, List<Reminder> reminders) {
        VBox formLayout = new VBox();
        formLayout.setSpacing(15);
        formLayout.setStyle("-fx-padding: 20;");

        Label formTitle = new Label("Add New Task");
        formTitle.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");

        TextField titleField = new TextField();
        titleField.setPromptText("Task Title");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Task Description");

        TextField categoryField = new TextField();
        categoryField.setPromptText("Category");

        TextField priorityField = new TextField();
        priorityField.setPromptText("Priority (e.g., High, Medium, Low)");

        TextField deadlineField = new TextField();
        deadlineField.setPromptText("Deadline (yyyy-MM-dd)");

        VBox remindersBox = new VBox();
        remindersBox.setSpacing(10);
        Label reminderLabel = new Label("Reminders");
        ComboBox<String> reminderTypeComboBox = new ComboBox<>();
        reminderTypeComboBox.getItems().addAll(
            "One day before",
            "One week before",
            "One month before",
            "Custom date"
        );

        TextField customDateField = new TextField();
        customDateField.setPromptText("Custom Reminder Date (yyyy-MM-dd)");
        customDateField.setDisable(true);

        reminderTypeComboBox.setOnAction(e -> {
            if ("Custom date".equals(reminderTypeComboBox.getValue())) {
                customDateField.setDisable(false);
            } else {
                customDateField.setDisable(true);
            }
        });

        List<String> pendingReminders = new ArrayList<>();

        Button addReminderButton = new Button("Add Reminder");
        addReminderButton.setOnAction(e -> {
            String reminderType = reminderTypeComboBox.getValue();
            String deadline = deadlineField.getText();
            if (deadline.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid deadline before adding reminders.", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            String reminderDate = calculateReminderDate(reminderType, deadline, customDateField.getText());
            if (reminderDate != null) {
                pendingReminders.add(reminderDate);
                remindersBox.getChildren().add(new Label("Reminder added for: " + reminderDate));
            }
        });

        remindersBox.getChildren().addAll(reminderLabel, reminderTypeComboBox, customDateField, addReminderButton);

        Button saveButton = new Button("Save Task");
        saveButton.setStyle("-fx-font-size: 16; -fx-padding: 10; -fx-background-color: #1E8449; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        saveButton.setOnAction(e -> {
            String title = titleField.getText();
            String description = descriptionField.getText();
            String category = categoryField.getText();
            String priority = priorityField.getText();
            String deadline = deadlineField.getText();

            if (title.isEmpty() || description.isEmpty() || category.isEmpty() || priority.isEmpty() || deadline.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "All fields are required!", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            Task newTask = new Task(title, description, category, priority, deadline);
            tasks.add(newTask);

            for (String reminderDate : pendingReminders) {
                Reminder newReminder = new Reminder(reminderDate, newTask.getId());
                newTask.addReminder(newReminder);
                reminders.add(newReminder);
            }

            Category existingCategory = categories.stream()
                    .filter(cat -> cat.getName().equals(category))
                    .findFirst()
                    .orElse(null);

            if (existingCategory == null) {
                Category newCategory = new Category(category);
                newCategory.getTasks().add(newTask);
                categories.add(newCategory);
            } else {
                existingCategory.getTasks().add(newTask);
            }

            Priority existingPriority = priorities.stream()
                    .filter(pri -> pri.getName().equals(priority))
                    .findFirst()
                    .orElse(null);

            if (existingPriority == null) {
                Priority newPriority = new Priority(priority);
                newPriority.getTasks().add(newTask);
                priorities.add(newPriority);
            } else {
                existingPriority.getTasks().add(newTask);
            }

            primaryStage.setScene(getScene(primaryStage, tasks, categories, priorities, reminders));
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-font-size: 16; -fx-padding: 10; -fx-background-color: #922B21; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        cancelButton.setOnAction(e -> primaryStage.setScene(getScene(primaryStage, tasks, categories, priorities, reminders)));

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setAlignment(javafx.geometry.Pos.BASELINE_LEFT);

        formLayout.getChildren().addAll(formTitle, titleField, descriptionField, categoryField, priorityField, deadlineField, remindersBox, buttonBox);

        Scene formScene = new Scene(formLayout, 800, 600);
        primaryStage.setScene(formScene);
    }

    private static void openEditTaskForm(Stage primaryStage, List<Task> tasks, List<Category> categories, List<Priority> priorities, List<Reminder> reminders, Task taskToEdit) {
        VBox formLayout = new VBox();
        formLayout.setSpacing(15);
        formLayout.setStyle("-fx-padding: 20;");

        Label formTitle = new Label("Edit Task");
        formTitle.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");

        TextField titleField = new TextField(taskToEdit.getTitle());
        titleField.setPromptText("Task Title");

        TextField descriptionField = new TextField(taskToEdit.getDescription());
        descriptionField.setPromptText("Task Description");

        TextField categoryField = new TextField(taskToEdit.getCategory());
        categoryField.setPromptText("Category");

        TextField priorityField = new TextField(taskToEdit.getPriority());
        priorityField.setPromptText("Priority (e.g., High, Medium, Low)");

        TextField deadlineField = new TextField(taskToEdit.getDeadline());
        deadlineField.setPromptText("Deadline (yyyy-MM-dd)");

        Label statusLabel = new Label("Task Status:");
        ComboBox<Task.Status> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll(Task.Status.values());
        statusComboBox.setValue(taskToEdit.getStatus());

        VBox remindersBox = new VBox();
        remindersBox.setSpacing(10);
        Label reminderLabel = new Label("Reminders");
        ComboBox<String> reminderTypeComboBox = new ComboBox<>();
        reminderTypeComboBox.getItems().addAll(
            "One day before",
            "One week before",
            "One month before",
            "Custom date"
        );

        TextField customDateField = new TextField();
        customDateField.setPromptText("Custom Reminder Date (yyyy-MM-dd)");
        customDateField.setDisable(true);

        reminderTypeComboBox.setOnAction(e -> {
            if ("Custom date".equals(reminderTypeComboBox.getValue())) {
                customDateField.setDisable(false);
            } else {
                customDateField.setDisable(true);
            }
        });

        taskToEdit.getReminders().forEach(reminder -> {
            Label reminderEntry = new Label(reminder.getDate());
            remindersBox.getChildren().add(reminderEntry);
        });

        Button addReminderButton = new Button("Add Reminder");
        addReminderButton.setOnAction(e -> {
            String reminderType = reminderTypeComboBox.getValue();
            String deadline = taskToEdit.getDeadline();
            String reminderDate = calculateReminderDate(reminderType, deadline, customDateField.getText());
            if (reminderDate != null) {
                Reminder newReminder = new Reminder(reminderDate, taskToEdit.getId());
                taskToEdit.addReminder(newReminder);
                reminders.add(newReminder);
                remindersBox.getChildren().add(new Label(reminderDate));
            }
        });

        remindersBox.getChildren().addAll(reminderLabel, reminderTypeComboBox, customDateField, addReminderButton);

        statusComboBox.setOnAction(e -> {
            if (statusComboBox.getValue() == Task.Status.COMPLETED) {
                remindersBox.setDisable(true);
            } else {
                remindersBox.setDisable(false);
            }
        });

        Button saveButton = new Button("Save Task");
        saveButton.setStyle("-fx-font-size: 16; -fx-padding: 10; -fx-background-color: #1E8449; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        saveButton.setOnAction(e -> {
            String title = titleField.getText();
            String description = descriptionField.getText();
            String category = categoryField.getText();
            String priority = priorityField.getText();
            String deadline = deadlineField.getText();
            Task.Status status = statusComboBox.getValue();

            if (title.isEmpty() || description.isEmpty() || category.isEmpty() || priority.isEmpty() || deadline.isEmpty() || status == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "All fields are required!", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            String oldCategory = taskToEdit.getCategory();
            String oldPriority = taskToEdit.getPriority();

            categories.stream()
                .filter(cat -> cat.getName().equals(oldCategory))
                .findFirst()
                .ifPresent(cat -> cat.getTasks().remove(taskToEdit));

            priorities.stream()
                .filter(pri -> pri.getName().equals(oldPriority))
                .findFirst()
                .ifPresent(pri -> pri.getTasks().remove(taskToEdit));

            taskToEdit.setTitle(title);
            taskToEdit.setDescription(description);
            taskToEdit.setCategory(category);
            taskToEdit.setPriority(priority);
            taskToEdit.setDeadline(deadline);
            taskToEdit.setStatus(status);

            taskToEdit.getReminders().clear();
            reminders.removeIf(reminder -> reminder.getTaskId() == taskToEdit.getId());

            if (status == Task.Status.COMPLETED) {
                reminders.removeIf(reminder -> reminder.getTaskId() == taskToEdit.getId());
                taskToEdit.getReminders().clear();
            } else {
                remindersBox.getChildren().stream()
                    .filter(node -> node instanceof Label)
                    .map(node -> ((Label) node).getText())
                    .filter(reminderDate -> !reminderDate.equals("Reminders"))
                    .forEach(reminderDate -> {
                        Reminder newReminder = new Reminder(reminderDate, taskToEdit.getId());
                        taskToEdit.addReminder(newReminder);
                        reminders.add(newReminder);
                    });
            }

            Category existingCategory = categories.stream()
                    .filter(cat -> cat.getName().equals(category))
                    .findFirst()
                    .orElse(null);

            if (existingCategory == null) {
                Category newCategory = new Category(category);
                newCategory.getTasks().add(taskToEdit);
                categories.add(newCategory);
            } else {
                existingCategory.getTasks().add(taskToEdit);
            }

            Priority existingPriority = priorities.stream()
                    .filter(pri -> pri.getName().equals(priority))
                    .findFirst()
                    .orElse(null);

            if (existingPriority == null) {
                Priority newPriority = new Priority(priority);
                newPriority.getTasks().add(taskToEdit);
                priorities.add(newPriority);
            } else {
                existingPriority.getTasks().add(taskToEdit);
            }

            primaryStage.setScene(getScene(primaryStage, tasks, categories, priorities, reminders));
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-font-size: 16; -fx-padding: 10; -fx-background-color: #922B21; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        cancelButton.setOnAction(e -> primaryStage.setScene(getScene(primaryStage, tasks, categories, priorities, reminders)));

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setAlignment(javafx.geometry.Pos.BASELINE_LEFT);

        formLayout.getChildren().addAll(formTitle, titleField, descriptionField, categoryField, priorityField, deadlineField, statusLabel, statusComboBox, remindersBox, buttonBox);

        Scene formScene = new Scene(formLayout, 800, 600);
        primaryStage.setScene(formScene);
    }

    private static String calculateReminderDate(String reminderType, String deadline, String customDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate deadlineDate = LocalDate.parse(deadline, formatter);

            switch (reminderType) {
                case "One day before":
                    return deadlineDate.minusDays(1).toString();
                case "One week before":
                    return deadlineDate.minusWeeks(1).toString();
                case "One month before":
                    return deadlineDate.minusMonths(1).toString();
                case "Custom date":
                    if (customDate != null && !customDate.isEmpty() && Reminder.isValidDate(customDate)) {
                        return customDate;
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid custom date format!", ButtonType.OK);
                        alert.showAndWait();
                        return null;
                    }
                default:
                    return null;
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid deadline date format!", ButtonType.OK);
            alert.showAndWait();
            return null;
        }
    }

}