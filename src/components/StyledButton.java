package components;

import javafx.scene.control.Button;

public class StyledButton extends Button {
    public StyledButton(String text) {
        super(text);
        this.setPrefWidth(300);
        this.setStyle("-fx-font-size: 16; -fx-background-color: #f0f0f0; -fx-border-color: #ccc; " +
                "-fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10 20; -fx-text-fill: black; -fx-cursor: hand;");
    }
}
