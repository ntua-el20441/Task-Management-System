package application;

import components.BackButton;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.Category;
import models.Task;
import models.Priority;
import models.Reminder;

import java.util.List;

public class CategoryManagement {
    public static Scene getScene(Stage primaryStage, List<Task> tasks, List<Category> categories, List<Priority> priorities, List<Reminder> reminders) {
        BorderPane layout = new BorderPane();

        BackButton backButton = new BackButton(primaryStage, () ->
            primaryStage.setScene(new Main().getMainScene(primaryStage, tasks, categories, priorities, reminders))
        );

        Label titleLabel = new Label("Category Management");
        titleLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: white;");

        HBox titleBox = new HBox(titleLabel);
        titleBox.setAlignment(javafx.geometry.Pos.CENTER);
        titleBox.setStyle("-fx-padding: 20px; -fx-background-color: #121212;");

        Button addCategoryButton = new Button("+ Add Category");
        addCategoryButton.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px; -fx-background-color: #3498db; -fx-text-fill: white; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        addCategoryButton.setOnAction(e -> openAddCategoryForm(primaryStage, tasks, categories, priorities, reminders));

        HBox topRightBox = new HBox(addCategoryButton);
        topRightBox.setAlignment(javafx.geometry.Pos.TOP_RIGHT);
        topRightBox.setStyle("-fx-padding: 10px;");

        BorderPane topPane = new BorderPane();
        topPane.setLeft(backButton);
        topPane.setCenter(titleBox);
        topPane.setRight(topRightBox);
        topPane.setStyle("-fx-background-color: #121212;");

        layout.setTop(topPane);

        VBox content = new VBox();
        content.setSpacing(15);
        content.setStyle("-fx-padding: 20px; -fx-background-color: #121212;");

        for (Category category : categories) {
            BorderPane categoryBox = new BorderPane();
            categoryBox.setStyle("-fx-background-color: #1E1E1E; -fx-border-color: #3498db; -fx-border-width: 2px; -fx-padding: 10px; -fx-border-radius: 10px;");

            Label categoryLabel = new Label(category.getName() + " (" + category.getTasks().size() + " tasks)");
            categoryLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");

            categoryBox.setLeft(categoryLabel);

            HBox actionButtons = new HBox(10);
            actionButtons.setStyle("-fx-padding: 5px;");

            Button deleteButton = new Button("REMOVE");
            deleteButton.setStyle("-fx-font-size: 16px; -fx-background-color: transparent; -fx-text-fill: #e74c3c;");
            deleteButton.setOnAction(e -> {
                tasks.removeIf(task -> task.getCategory().equals(category.getName()));
                categories.remove(category);
                priorities.forEach(priority ->
                    priority.getTasks().removeIf(task -> task.getCategory().equals(category.getName()))
                );
                primaryStage.setScene(getScene(primaryStage, tasks, categories, priorities, reminders));
            });

            Button editButton = new Button("EDIT");
            editButton.setStyle("-fx-font-size: 16px; -fx-background-color: transparent; -fx-text-fill: #2ecc71;");
            editButton.setOnAction(e -> openEditCategoryForm(primaryStage, tasks, categories, priorities, reminders, category));

            actionButtons.getChildren().addAll(editButton, deleteButton);
            categoryBox.setRight(actionButtons);

            content.getChildren().add(categoryBox);
        }

        if (categories.isEmpty()) {
            Label noCategoriesLabel = new Label("No categories available.");
            noCategoriesLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #7F8C8D;");
            content.getChildren().add(noCategoriesLabel);
        }

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #121212; -fx-background-color: #121212;");

        layout.setCenter(scrollPane);

        return new Scene(layout, 1400, 900);
    }

    private static void openAddCategoryForm(Stage primaryStage, List<Task> tasks, List<Category> categories, List<Priority> priorities, List<Reminder> reminders) {
        openCategoryForm(primaryStage, tasks, categories, priorities, reminders, null);
    }

    private static void openEditCategoryForm(Stage primaryStage, List<Task> tasks, List<Category> categories, List<Priority> priorities, List<Reminder> reminders, Category categoryToEdit) {
        openCategoryForm(primaryStage, tasks, categories, priorities, reminders, categoryToEdit);
    }

    private static void openCategoryForm(Stage primaryStage, List<Task> tasks, List<Category> categories, List<Priority> priorities, List<Reminder> reminders, Category categoryToEdit) {
        VBox formLayout = new VBox();
        formLayout.setSpacing(15);
        formLayout.setStyle("-fx-padding: 20px; -fx-background-color: #121212;");

        String formTitleText = (categoryToEdit == null) ? "Add New Category" : "Edit Category";
        Label formTitle = new Label(formTitleText);
        formTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        TextField categoryNameField = new TextField();
        categoryNameField.setPromptText("Category Name");
        categoryNameField.setStyle("-fx-background-color: #1E1E1E; -fx-text-fill: white; -fx-prompt-text-fill: #7F8C8D;");
        if (categoryToEdit != null) categoryNameField.setText(categoryToEdit.getName());

        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-font-size: 16px; -fx-padding: 10px; -fx-background-color: #2ecc71; -fx-text-fill: white; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        saveButton.setOnAction(e -> {
            String newCategoryName = categoryNameField.getText();

            if (newCategoryName.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Category name is required!", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            if (categoryToEdit != null) {
                String oldCategoryName = categoryToEdit.getName();

                categoryToEdit.setName(newCategoryName);

                tasks.stream()
                    .filter(task -> task.getCategory().equals(oldCategoryName))
                    .forEach(task -> task.setCategory(newCategoryName));

                categoryToEdit.getTasks().forEach(task -> task.setCategory(newCategoryName));

                priorities.forEach(priority ->
                    priority.getTasks().stream()
                        .filter(task -> task.getCategory().equals(oldCategoryName))
                        .forEach(task -> task.setCategory(newCategoryName))
                );
            } else {
                categories.add(new Category(newCategoryName));
            }

            primaryStage.setScene(getScene(primaryStage, tasks, categories, priorities, reminders));
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-font-size: 16px; -fx-padding: 10px; -fx-background-color: #e74c3c; -fx-text-fill: white; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        cancelButton.setOnAction(e -> primaryStage.setScene(getScene(primaryStage, tasks, categories, priorities, reminders)));

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setAlignment(javafx.geometry.Pos.BASELINE_LEFT);

        formLayout.getChildren().addAll(formTitle, categoryNameField, buttonBox);

        Scene formScene = new Scene(formLayout, 1400, 900);
        primaryStage.setScene(formScene);
    }
}

