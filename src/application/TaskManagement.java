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

        BackButton backButton = new BackButton(primaryStage, () -> primaryStage.setScene(new Main().getMainScene(primaryStage, tasks, categories, priorities, reminders)));

        Label titleLabel = createStyledLabel("Task Management", "-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: white;");

        HBox titleBox = createAlignedHBox(javafx.geometry.Pos.CENTER, "-fx-padding: 20px; -fx-background-color: #1E1E1E;", titleLabel);

        Button addTaskButton = createStyledButton("New Task", "-fx-font-size: 16px; -fx-padding: 10px 20px; -fx-background-color: #3498db; -fx-text-fill: white; -fx-border-radius: 20px; -fx-background-radius: 20px;", e -> openAddTaskForm(primaryStage, tasks, categories, priorities, reminders));

        HBox topRightBox = createAlignedHBox(javafx.geometry.Pos.TOP_RIGHT, "-fx-padding: 20px 20px 0px 0px;", addTaskButton);

        BorderPane topPane = new BorderPane();
        topPane.setLeft(backButton);
        topPane.setCenter(titleBox);
        topPane.setRight(topRightBox);
        topPane.setStyle("-fx-background-color: #121212;");

        layout.setTop(topPane);

        VBox content = createStyledVBox(20, "-fx-padding: 20px; -fx-background-color: #121212; -fx-min-height: 6000px; ");

        Map<String, List<Task>> tasksByCategory = groupTasksByCategory(tasks);

        tasksByCategory.forEach((category, tasksInCategory) -> {
            Label categoryLabel = createStyledLabel(category, "-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #e0e0e0;");

            VBox tasksBox = createStyledVBox(10, "-fx-padding: 10px; -fx-background-color: #1E1E1E; -fx-border-color: #3498db; -fx-border-radius: 8px; -fx-border-width: 2px;");

            tasksInCategory.forEach(task -> {
                BorderPane taskBox = createStyledBorderPane("-fx-background-color: #2C2C2C; -fx-border-color: #3498db; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-padding: 10px;");

                Label taskLabel = createStyledLabel(task.getTitle() + ": " + task.getDescription() + " (ID: " + task.getId() + ")", "-fx-font-size: 16px; -fx-text-fill: white;");
                taskBox.setLeft(taskLabel);

                Button deleteButton = createStyledButton("REMOVE", "-fx-font-size: 14px; -fx-background-color: #e74c3c; -fx-text-fill: white; -fx-border-radius: 5px; -fx-background-radius: 5px;", e -> handleTaskRemoval(primaryStage, tasks, categories, priorities, reminders, task));

                Button editButton = createStyledButton("VIEW/EDIT", "-fx-font-size: 14px; -fx-background-color: #2ecc71; -fx-text-fill: white; -fx-border-radius: 5px; -fx-background-radius: 5px;", e -> openEditTaskForm(primaryStage, tasks, categories, priorities, reminders, task));

                HBox actionButtons = createAlignedHBox(javafx.geometry.Pos.CENTER_RIGHT, "-fx-padding: 5px;", editButton, deleteButton);
                taskBox.setRight(actionButtons);

                tasksBox.getChildren().add(taskBox);
            });

            content.getChildren().addAll(categoryLabel, tasksBox);
        });

        ScrollPane scrollPane = createStyledScrollPane(content);
        layout.setCenter(scrollPane);

        return new Scene(layout, 1400, 900);
    }

    private static Map<String, List<Task>> groupTasksByCategory(List<Task> tasks) {
        return tasks.stream().collect(Collectors.groupingBy(Task::getCategory));
    }

    private static void handleTaskRemoval(Stage primaryStage, List<Task> tasks, List<Category> categories, List<Priority> priorities, List<Reminder> reminders, Task task) {
        tasks.remove(task);
        categories.forEach(cat -> cat.getTasks().removeIf(t -> t.equals(task)));
        priorities.forEach(priority -> priority.getTasks().removeIf(t -> t.equals(task)));
        if (reminders != null) reminders.removeIf(reminder -> reminder.getTaskId() == task.getId());
        primaryStage.setScene(getScene(primaryStage, tasks, categories, priorities, reminders));
    }

    private static Label createStyledLabel(String text, String style) {
        Label label = new Label(text);
        label.setStyle(style);
        return label;
    }

    private static Button createStyledButton(String text, String style, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Button button = new Button(text);
        button.setStyle(style);
        button.setOnAction(action);
        return button;
    }

    private static HBox createAlignedHBox(javafx.geometry.Pos alignment, String style, javafx.scene.Node... children) {
        HBox hbox = new HBox(children);
        hbox.setAlignment(alignment);
        hbox.setStyle(style);
        return hbox;
    }

    private static VBox createStyledVBox(int spacing, String style) {
        VBox vbox = new VBox();
        vbox.setSpacing(spacing);
        vbox.setStyle(style);
        return vbox;
    }

    private static BorderPane createStyledBorderPane(String style) {
        BorderPane pane = new BorderPane();
        pane.setStyle(style);
        return pane;
    }

    private static ScrollPane createStyledScrollPane(javafx.scene.Node content) {
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        return scrollPane;
    }

    private static void openAddTaskForm(Stage primaryStage, List<Task> tasks, List<Category> categories, List<Priority> priorities, List<Reminder> reminders) {
        VBox formLayout = new VBox();
        formLayout.setSpacing(15);
        formLayout.setStyle("-fx-padding: 20px; -fx-background-color: #121212;");

        Label formTitle = createStyledLabel("Add New Task", "-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        TextField titleField = createStyledTextField("Task Title");
        TextField descriptionField = createStyledTextField("Task Description");
        TextField categoryField = createStyledTextField("Category");
        TextField priorityField = createStyledTextField("Priority (e.g., High, Medium, Low)");
        TextField deadlineField = createStyledTextField("Deadline (yyyy-MM-dd)");

        VBox remindersBox = createStyledVBox(10, "");
        Label reminderLabel = createStyledLabel("Reminders", "-fx-font-size: 16px; -fx-text-fill: white;");
        ComboBox<String> reminderTypeComboBox = createStyledComboBox();
        reminderTypeComboBox.getItems().addAll(
            "One day before",
            "One week before",
            "One month before",
            "Custom date"
        );

        TextField customDateField = createStyledTextField("Custom Reminder Date (yyyy-MM-dd)");
        customDateField.setDisable(true);

        reminderTypeComboBox.setOnAction(e -> {
            if ("Custom date".equals(reminderTypeComboBox.getValue())) {
                customDateField.setDisable(false);
            } else {
                customDateField.setDisable(true);
            }
        });

        List<String> pendingReminders = new ArrayList<>();

        Button addReminderButton = createStyledButton("Add Reminder", "-fx-font-size: 14px; -fx-background-color: #3498db; -fx-text-fill: white; -fx-border-radius: 5px; -fx-background-radius: 5px;", e -> {
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
                remindersBox.getChildren().add(createStyledLabel("Reminder added for: " + reminderDate, "-fx-text-fill: white;"));
            }
        });

        remindersBox.getChildren().addAll(reminderLabel, reminderTypeComboBox, customDateField, addReminderButton);

        Button saveButton = createStyledButton("Save Task", "-fx-font-size: 16px; -fx-padding: 10px; -fx-background-color: #2ecc71; -fx-text-fill: white; -fx-border-radius: 5px; -fx-background-radius: 5px;", e -> {
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

            primaryStage.setScene(getScene(primaryStage, tasks, categories, priorities, reminders));
        });

        Button cancelButton = createStyledButton("Cancel", "-fx-font-size: 16px; -fx-padding: 10px; -fx-background-color: #e74c3c; -fx-text-fill: white; -fx-border-radius: 5px; -fx-background-radius: 5px;", e -> primaryStage.setScene(getScene(primaryStage, tasks, categories, priorities, reminders)));

        HBox buttonBox = createAlignedHBox(javafx.geometry.Pos.BASELINE_LEFT, "", saveButton, cancelButton);

        formLayout.getChildren().addAll(formTitle, titleField, descriptionField, categoryField, priorityField, deadlineField, remindersBox, buttonBox);

        Scene formScene = new Scene(formLayout, 1400, 900);
        primaryStage.setScene(formScene);
    }

    private static TextField createStyledTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setStyle("-fx-background-color: #1E1E1E; -fx-text-fill: white; -fx-prompt-text-fill: #7F8C8D; -fx-border-color: #3498db; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 5px;");
        return textField;
    }

    private static ComboBox<String> createStyledComboBox() {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setStyle("-fx-background-color: #1E1E1E; -fx-text-fill: white; -fx-border-color: #3498db; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 5px;");
        return comboBox;
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

    private static void openEditTaskForm(Stage primaryStage, List<Task> tasks, List<Category> categories, List<Priority> priorities, List<Reminder> reminders, Task taskToEdit) {
        VBox formLayout = new VBox();
        formLayout.setSpacing(15);
        formLayout.setStyle("-fx-padding: 20px; -fx-background-color: #121212;");

        Label formTitle = createStyledLabel("Edit Task", "-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        TextField titleField = createStyledTextField(taskToEdit.getTitle());
        TextField descriptionField = createStyledTextField(taskToEdit.getDescription());
        TextField categoryField = createStyledTextField(taskToEdit.getCategory());
        TextField priorityField = createStyledTextField(taskToEdit.getPriority());
        TextField deadlineField = createStyledTextField(taskToEdit.getDeadline());

        ComboBox<Task.Status> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll(Task.Status.values());
        statusComboBox.setValue(taskToEdit.getStatus());
        statusComboBox.setStyle("-fx-background-color: #1E1E1E; -fx-text-fill: white; -fx-border-color: #3498db; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 5px;");

        VBox remindersBox = createStyledVBox(10, "");
        Label reminderLabel = createStyledLabel("Reminders", "-fx-font-size: 16px; -fx-text-fill: white;");

        taskToEdit.getReminders().forEach(reminder -> {
            Label reminderEntry = createStyledLabel("Reminder: " + reminder.getDate(), "-fx-text-fill: white;");
            remindersBox.getChildren().add(reminderEntry);
        });

        Button saveButton = createStyledButton("Save Changes", "-fx-font-size: 16px; -fx-padding: 10px; -fx-background-color: #2ecc71; -fx-text-fill: white; -fx-border-radius: 5px; -fx-background-radius: 5px;", e -> {
            taskToEdit.setTitle(titleField.getText());
            taskToEdit.setDescription(descriptionField.getText());
            taskToEdit.setCategory(categoryField.getText());
            taskToEdit.setPriority(priorityField.getText());
            taskToEdit.setDeadline(deadlineField.getText());
            taskToEdit.setStatus(statusComboBox.getValue());

            primaryStage.setScene(getScene(primaryStage, tasks, categories, priorities, reminders));
        });

        Button cancelButton = createStyledButton("Cancel", "-fx-font-size: 16px; -fx-padding: 10px; -fx-background-color: #e74c3c; -fx-text-fill: white; -fx-border-radius: 5px; -fx-background-radius: 5px;", e -> primaryStage.setScene(getScene(primaryStage, tasks, categories, priorities, reminders)));

        HBox buttonBox = createAlignedHBox(javafx.geometry.Pos.BASELINE_LEFT, "", saveButton, cancelButton);

        formLayout.getChildren().addAll(formTitle, titleField, descriptionField, categoryField, priorityField, deadlineField, statusComboBox, remindersBox, buttonBox);

        Scene formScene = new Scene(formLayout, 1400, 900);
        primaryStage.setScene(formScene);
    }
}

