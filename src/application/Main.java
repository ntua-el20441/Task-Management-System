package application;

import components.StyledButton;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Task;
import models.Category;
import models.Priority;
import models.Reminder;
import storage.Loader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;

public class Main extends Application {
    private List<Task> tasks;
    private List<Category> categories;
    private List<Priority> priorities;
    private List<Reminder> reminders;

    @Override
    public void start(Stage primaryStage) {
        tasks = Loader.loadFromJson("medialab/tasks.json", new TypeReference<List<Task>>() {});
        categories = Loader.loadFromJson("medialab/categories.json", new TypeReference<List<Category>>() {});
        priorities = Loader.loadFromJson("medialab/priorities.json", new TypeReference<List<Priority>>() {});
        reminders = Loader.loadFromJson("medialab/reminders.json", new TypeReference<List<Reminder>>() {});

        if (tasks == null) tasks = new ArrayList<>();
        if (categories == null) categories = new ArrayList<>();
        if (priorities == null) priorities = new ArrayList<>();
        if (reminders == null) reminders = new ArrayList<>();

        primaryStage.setScene(getMainScene(primaryStage));
        primaryStage.setTitle("DarkFlow");
        primaryStage.show();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Loader.saveToJson("medialab/tasks.json", tasks);
            Loader.saveToJson("medialab/categories.json", categories);
            Loader.saveToJson("medialab/priorities.json", priorities);
            Loader.saveToJson("medialab/reminders.json", reminders);
            System.out.println("Data saved successfully!");
        }));
    }

    public Scene getMainScene(Stage primaryStage) {
        BorderPane mainLayout = new BorderPane();

        VBox header = createHeader();
        mainLayout.setTop(header);

        ScrollPane dashboard = createDashboard();
        mainLayout.setCenter(dashboard);

        HBox footer = createFooter(primaryStage);
        mainLayout.setBottom(footer);

        return new Scene(mainLayout, 1400, 900);
    }

    public Scene getMainScene(Stage primaryStage, List<Task> tasks, List<Category> categories, List<Priority> priorities, List<Reminder> reminders) {
        this.tasks = tasks;
        this.categories = categories;
        this.priorities = priorities;
        this.reminders = reminders;
        return getMainScene(primaryStage);
    }

    private VBox createHeader() {
        VBox header = new VBox();
        header.setAlignment(Pos.CENTER);
        header.setSpacing(10);
        header.setStyle("-fx-background-color: #121212; -fx-padding: 20px;");

        Text title = new Text("MediaLab");
        title.setFont(Font.font("Sans Serif", FontWeight.EXTRA_BOLD, 40));
        title.setFill(Color.WHITE);

        Text subtitle = new Text("Your Personal Assistant");
        subtitle.setFont(Font.font("Sans Serif", FontWeight.LIGHT, 18));
        subtitle.setFill(Color.GRAY);

        header.getChildren().addAll(title, subtitle);
        return header;
    }

    private ScrollPane createDashboard() {
        ScrollPane dashboardScrollPane = new ScrollPane();
        dashboardScrollPane.setFitToWidth(true);
        dashboardScrollPane.setStyle("-fx-background: #121212; -fx-padding: 20px;");

        GridPane dashboard = new GridPane();
        dashboard.setHgap(20);
        dashboard.setVgap(20);
        dashboard.setAlignment(Pos.CENTER);
        dashboard.setStyle("-fx-background-color: #1E1E1E; -fx-padding: 20px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        dashboard.add(createStatCard("Total Tasks", getTotalTasks(), "#3498db"), 0, 0);
        dashboard.add(createStatCard("Completed", getTasksByStatus(Task.Status.COMPLETED), "#3498db"), 1, 0);
        dashboard.add(createStatCard("Overdue", getTasksByStatus(Task.Status.DELAYED), "#3498db"), 0, 1);
        dashboard.add(createStatCard("Due Soon", getTasksDueIn7Days(), "#3498db"), 1, 1);

        dashboardScrollPane.setContent(dashboard);
        return dashboardScrollPane;
    }

    private VBox createStatCard(String label, int count, String color) {
        VBox statCard = new VBox();
        statCard.setAlignment(Pos.CENTER);
        statCard.setSpacing(10);
        statCard.setStyle("-fx-background-color: #2C2C2C; -fx-border-color: " + color + "; -fx-border-width: 2px; -fx-padding: 15px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        Text countText = new Text(String.valueOf(count));
        countText.setFont(Font.font("Arial", FontWeight.BOLD, 50));
        countText.setFill(Color.web(color));

        Label labelText = new Label(label);
        labelText.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        labelText.setTextFill(Color.LIGHTGRAY);

        statCard.getChildren().addAll(countText, labelText);
        return statCard;
    }

    private HBox createFooter(Stage primaryStage) {
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER);
        footer.setSpacing(40);
        footer.setStyle("-fx-background-color: #1E1E1E; -fx-padding: 15px;");

        StyledButton tasksButton = createFooterButton(" Tasks", "#3498db");
        StyledButton categoriesButton = createFooterButton(" Categories", "#3498db");
        StyledButton prioritiesButton = createFooterButton(" Priorities", "#3498db");
        StyledButton remindersButton = createFooterButton(" Reminders", "#3498db");
        StyledButton searchButton = createFooterButton(" Search", "#3498db");

        tasksButton.setOnAction(e -> primaryStage.setScene(TaskManagement.getScene(primaryStage, tasks, categories, priorities, reminders)));
        categoriesButton.setOnAction(e -> primaryStage.setScene(CategoryManagement.getScene(primaryStage, tasks, categories, priorities, reminders)));
        prioritiesButton.setOnAction(e -> primaryStage.setScene(PriorityManagement.getScene(primaryStage, tasks, categories, priorities, reminders)));
        remindersButton.setOnAction(e -> primaryStage.setScene(ReminderManagement.getScene(primaryStage, tasks, categories, priorities, reminders)));
        searchButton.setOnAction(e -> primaryStage.setScene(SearchTab.getScene(primaryStage, tasks, categories, priorities, reminders)));

        footer.getChildren().addAll(tasksButton, categoriesButton, prioritiesButton, remindersButton, searchButton);
        return footer;
    }

    private StyledButton createFooterButton(String text, String color) {
        StyledButton button = new StyledButton(text);
        button.setStyle("-fx-font-size: 16px; -fx-background-color: " + color + "; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-border-radius: 10px; -fx-background-radius: 10px;");
        button.setPrefHeight(50);
        return button;
    }

    private int getTotalTasks() {
        return tasks != null ? tasks.size() : 0;
    }

    private int getTasksByStatus(Task.Status status) {
        return tasks != null ? (int) tasks.stream().filter(task -> task.getStatus() == status).count() : 0;
    }

    private int getTasksDueIn7Days() {
        if (tasks == null) return 0;

        LocalDate today = LocalDate.now();
        LocalDate sevenDaysLater = today.plusDays(7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return (int) tasks.stream()
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

