package components;

import javafx.scene.control.Button;

/**
 * A customized Button with predefined styling.
 * 
 * <p>
 * The StyledButton class extends the JavaFX Button and applies consistent styling 
 * across the application. It includes properties such as preferred width, font size, 
 * background color, border radius, and hover effects to enhance the user interface.
 * </p>
 */
public class StyledButton extends Button {

    /**
     * Constructs a StyledButton with the specified text.
     * 
     * @param text The text to display on the button.
     */
    public StyledButton(String text) {
        super(text);
        initializeStyles();
    }

    /**
     * Initializes the styles for the button, including default and hover states.
     */
    private void initializeStyles() {
        // Set the preferred width of the button
        this.setPrefWidth(300);

        // Apply default CSS styling
        this.getStyleClass().add("styled-button");

        // Enable hover effects by adding and removing CSS classes
        this.setOnMouseEntered(event -> this.getStyleClass().add("hovered"));
        this.setOnMouseExited(event -> this.getStyleClass().remove("hovered"));
    }
}

