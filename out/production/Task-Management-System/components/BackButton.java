package components;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class BackButton extends StackPane {
    public BackButton(Stage stage, Runnable onBackAction) {
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 14; -fx-padding: 5 10; -fx-background-color: #e0e0e0; " +
                "-fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-radius: 5; -fx-cursor: hand;");
        backButton.setOnAction(e -> onBackAction.run());

        setAlignment(backButton, javafx.geometry.Pos.TOP_LEFT);
        getChildren().add(backButton);

        setStyle("-fx-padding: 20;");
    }
}