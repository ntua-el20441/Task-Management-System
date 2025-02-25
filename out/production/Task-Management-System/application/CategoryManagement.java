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
        titleLabel.setStyle("-fx-font-size: 32; -fx-font-weight: bold; -fx-text-fill: #2E4053;");

        HBox titleBox = new HBox(titleLabel);
        titleBox.setAlignment(javafx.geometry.Pos.CENTER);
        titleBox.setStyle("-fx-padding: 20; -fx-background-color: #F8F9F9;");

        Button addCategoryButton = new Button("New Category");
        addCategoryButton.setStyle("-fx-font-size: 16; -fx-padding: 10; -fx-background-color: #566573; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        addCategoryButton.setOnAction(e -> openAddCategoryForm(primaryStage, tasks, categories, priorities, reminders));

        HBox topRightBox = new HBox(addCategoryButton);
        topRightBox.setAlignment(javafx.geometry.Pos.TOP_RIGHT);
        topRightBox.setStyle("-fx-padding: 20 20 0 0;");

        BorderPane topPane = new BorderPane();
        topPane.setLeft(backButton);
        topPane.setCenter(titleBox);
        topPane.setRight(topRightBox);

        layout.setTop(topPane);

        VBox content = new VBox();
        content.setSpacing(20);

        for (Category category : categories) {
            BorderPane categoryBox = new BorderPane();
            categoryBox.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #D5D8DC; -fx-border-radius: 5; -fx-padding: 10;");

            Label categoryLabel = new Label(category.getName() + " (" + category.getTasks().size() + " tasks)");
            categoryLabel.setStyle("-fx-font-size: 18; -fx-text-fill: #2E4053;");

            categoryBox.setLeft(categoryLabel);

            HBox actionButtons = new HBox(10);
            actionButtons.setStyle("-fx-padding: 5;");

            Button deleteButton = new Button("REMOVE");
            deleteButton.setStyle("-fx-font-size: 16; -fx-background-color: transparent; -fx-border: none; -fx-text-fill: #922B21;");
            deleteButton.setOnAction(e -> {
                tasks.removeIf(task -> task.getCategory().equals(category.getName()));
                categories.remove(category);

                priorities.forEach(priority -> 
                    priority.getTasks().removeIf(task -> task.getCategory().equals(category.getName()))
                );

                primaryStage.setScene(getScene(primaryStage, tasks, categories, priorities, reminders));
            });

            Button editButton = new Button("EDIT");
            editButton.setStyle("-fx-font-size: 16; -fx-background-color: transparent; -fx-border: none; -fx-text-fill: #1E8449;");
            editButton.setOnAction(e -> openEditCategoryForm(primaryStage, tasks, categories, priorities, reminders, category));

            actionButtons.getChildren().addAll(editButton, deleteButton);
            categoryBox.setRight(actionButtons);

            content.getChildren().add(categoryBox);
        }

        if (categories.isEmpty()) {
            Label noCategoriesLabel = new Label("No categories available.");
            noCategoriesLabel.setStyle("-fx-font-size: 16; -fx-text-fill: #2E4053;");
            content.getChildren().add(noCategoriesLabel);
        }

        content.setStyle("-fx-padding: 20; -fx-background-color: #FBFCFC;");

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        layout.setCenter(scrollPane);

        return new Scene(layout, 800, 600);
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
        formLayout.setStyle("-fx-padding: 20;");

        String formTitleText = (categoryToEdit == null) ? "Add New Category" : "Edit Category";
        Label formTitle = new Label(formTitleText);
        formTitle.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");

        TextField categoryNameField = new TextField();
        categoryNameField.setPromptText("Category Name");
        if (categoryToEdit != null) categoryNameField.setText(categoryToEdit.getName());

        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-font-size: 16; -fx-padding: 10; -fx-background-color: #1E8449; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
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
        cancelButton.setStyle("-fx-font-size: 16; -fx-padding: 10; -fx-background-color: #922B21; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        cancelButton.setOnAction(e -> primaryStage.setScene(getScene(primaryStage, tasks, categories, priorities, reminders)));

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setStyle("-fx-padding: 10;");
        buttonBox.setAlignment(javafx.geometry.Pos.BASELINE_LEFT);

        formLayout.getChildren().addAll(formTitle, categoryNameField, buttonBox);

        Scene formScene = new Scene(formLayout, 800, 600);
        primaryStage.setScene(formScene);
    }
}
