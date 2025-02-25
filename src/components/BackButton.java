package components;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class BackButton extends StackPane {
    /**
     * Constructs a BackButton with styling and an action to perform when clicked.
     *
     * @param stage         The primary stage of the application (optional for future use).
     * @param onBackAction  A Runnable representing the action to perform on button click.
     */
    public BackButton(Stage stage, Runnable onBackAction) {
        // Initialize the Back button with label "Back"
        Button backButton = new Button("Back");

        // Apply CSS styling to the Back button
        backButton.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-padding: 10 20; " + // Increased padding for better click area
            "-fx-border-color: #ccc; " +
            "-fx-border-radius: 25px; " + // Rounded corners for a pill-shaped button
            "-fx-background-radius: 25px; " +
            "-fx-background-color: #f0f0f0; " + // Light gray background
            "-fx-text-fill: #333; " + // Dark gray text
            "-fx-cursor: hand;" // Hand cursor on hover
        );

        // Add hover effects using inline CSS
        backButton.setOnMouseEntered(e -> backButton.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-padding: 10 20; " +
            "-fx-border-color: #999; " +
            "-fx-border-radius: 25px; " +
            "-fx-background-radius: 25px; " +
            "-fx-background-color: #e0e0e0; " + // Slightly darker background on hover
            "-fx-text-fill: #111; " + // Darker text on hover
            "-fx-cursor: hand;"
        ));
        
        backButton.setOnMouseExited(e -> backButton.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-padding: 10 20; " +
            "-fx-border-color: #ccc; " +
            "-fx-border-radius: 25px; " +
            "-fx-background-radius: 25px; " +
            "-fx-background-color: #f0f0f0; " + // Original background color
            "-fx-text-fill: #333; " + // Original text color
            "-fx-cursor: hand;"
        ));

        // Set the action to perform when the Back button is clicked
        backButton.setOnAction(e -> onBackAction.run());

        // Align the Back button to the top-left corner of the StackPane
        setAlignment(backButton, Pos.TOP_LEFT);

        // Add the Back button to the StackPane's children
        getChildren().add(backButton);

        // Apply padding to the StackPane to provide spacing around the Back button
        setStyle("-fx-padding: 20;");
    }
}

