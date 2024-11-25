package application;

import components.StyledButton;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Task;
import models.Category;
import models.Priority;
import models.Reminder;
import storage.ReminderLoader;
import storage.TaskLoader;
import storage.CategoryLoader;
import storage.PriorityLoader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private List<Task> tasks;
    private List<Category> categories;
    private List<Priority> priorities;
    private List<Reminder> reminders;

    @Override
    public void start(Stage primaryStage) {
        tasks = TaskLoader.loadTasksFromJson("medialab/tasks.json");
        categories = CategoryLoader.loadCategoriesFromJson("medialab/categories.json");
        priorities = PriorityLoader.loadPrioritiesFromJson("medialab/priorities.json");
        reminders = ReminderLoader.loadRemindersFromJson("medialab/reminders.json");

        if (tasks == null) tasks = new ArrayList<>();
        if (categories == null) categories = new ArrayList<>();
        if (priorities == null) priorities = new ArrayList<>();
        if (reminders == null) reminders = new ArrayList<>();

        primaryStage.setScene(getMainScene(primaryStage));
        primaryStage.setTitle("MediaLab Assistant");
        primaryStage.show();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            TaskLoader.saveTasksToJson("medialab/tasks.json", tasks);
            CategoryLoader.saveCategoriesToJson("medialab/categories.json", categories);
            PriorityLoader.savePrioritiesToJson("medialab/priorities.json", priorities);
            ReminderLoader.saveRemindersToJson("medialab/reminders.json", reminders);
            System.out.println("Data saved successfully!");
        }));
    }

    public Scene getMainScene(Stage primaryStage) {
        return getMainScene(primaryStage, this.tasks, this.categories, this.priorities, this.reminders);
    }

    public Scene getMainScene(Stage primaryStage, List<Task> tasks, List<Category> categories, List<Priority> priorities, List<Reminder> reminders) {
        BorderPane mainLayout = new BorderPane();

        this.tasks = tasks;
        this.categories = categories;
        this.priorities = priorities;
        this.reminders = reminders;

        VBox topSection = createTaskSummary();
        mainLayout.setTop(topSection);

        VBox bottomSection = createVerticalButtons(primaryStage);
        mainLayout.setCenter(bottomSection);

        return new Scene(mainLayout, 800, 600);
    }

    private VBox createVerticalButtons(Stage primaryStage) {
        VBox buttonSection = new VBox();
        buttonSection.setAlignment(Pos.CENTER);
        buttonSection.setSpacing(20);

        StyledButton searchTasksButton = new StyledButton("Search for a task");
        StyledButton taskManagementButton = new StyledButton("Task Management");
        StyledButton categoryManagementButton = new StyledButton("Category Management");
        StyledButton priorityManagementButton = new StyledButton("Priority Management");
        StyledButton reminderManagementButton = new StyledButton("Reminder Management");

        taskManagementButton.setOnAction(e -> primaryStage.setScene(TaskManagement.getScene(primaryStage, this.tasks, this.categories, this.priorities, this.reminders)));
        categoryManagementButton.setOnAction(e -> primaryStage.setScene(CategoryManagement.getScene(primaryStage, this.tasks, this.categories, this.priorities, this.reminders)));
        priorityManagementButton.setOnAction(e -> primaryStage.setScene(PriorityManagement.getScene(primaryStage, this.tasks, this.categories, this.priorities, this.reminders)));
        reminderManagementButton.setOnAction(e -> primaryStage.setScene(ReminderManagement.getScene(primaryStage, this.tasks, this.categories, this.priorities, this.reminders)));
        searchTasksButton.setOnAction(e -> primaryStage.setScene(SearchTab.getScene(primaryStage, this.tasks, this.categories, this.priorities, this.reminders)));

        buttonSection.getChildren().addAll(searchTasksButton, taskManagementButton, categoryManagementButton, priorityManagementButton, reminderManagementButton);

        return buttonSection;
    }

    private VBox createTaskSummary() {
        VBox topSection = new VBox();
        topSection.setAlignment(Pos.CENTER);
        topSection.setSpacing(30);
        topSection.setStyle("-fx-padding: 60 0 0 0;");

        HBox statsRow = new HBox();
        statsRow.setAlignment(Pos.CENTER);
        statsRow.setSpacing(50);

        statsRow.getChildren().addAll(
                createStatBox("Total", getTotalTasks(), "#003366"),
                createStatBox("Completed", getTasksByStatus(Task.Status.COMPLETED), "#004c99"),
                createStatBox("Delayed", getTasksByStatus(Task.Status.DELAYED), "#dc3545"),
                createStatBox("Due Within 7 Days", getTasksDueIn7Days(), "#007B83")
        );

        topSection.getChildren().add(statsRow);
        return topSection;
    }

    private VBox createStatBox(String label, int number, String color) {
        VBox statBox = new VBox();
        statBox.setAlignment(Pos.CENTER);
        statBox.setSpacing(8);

        Label numberLabel = new Label(String.valueOf(number));
        numberLabel.setStyle("-fx-font-size: 48; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        Label textLabel = new Label(label);
        textLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #333;");

        statBox.getChildren().addAll(numberLabel, textLabel);
        return statBox;
    }

    private int getTotalTasks() {
        return this.tasks != null ? this.tasks.size() : 0;
    }

    private int getTasksByStatus(Task.Status status) {
        return this.tasks != null ? (int) this.tasks.stream().filter(task -> task.getStatus() == status).count() : 0;
    }

    private int getTasksDueIn7Days() {
        if (this.tasks == null) return 0;

        LocalDate today = LocalDate.now();
        LocalDate sevenDaysLater = today.plusDays(7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return (int) this.tasks.stream()
                .filter(task -> {
                    LocalDate deadline = LocalDate.parse(task.getDeadline(), formatter);
                    return deadline.isAfter(today.minusDays(1)) && deadline.isBefore(sevenDaysLater.plusDays(1));
                })
                .count();
    }

    public static void main(String[] args) {
        launch(args);
    }
}