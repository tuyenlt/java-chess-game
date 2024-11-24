package chessgame.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameEndAnnouncement extends Pane {

    public GameEndAnnouncement(String player1Name, String player2Name, String result, Runnable onReturn) {
        // getStylesheets().add(getClass().getResource("/chessgame/style.css").toExternalForm());

        double rectWidth = 600;
        double rectHeight = 300;
        Rectangle background = new Rectangle(rectWidth, rectHeight);
        background.setFill(Color.rgb(0, 0, 0, 0.8));
        background.setArcWidth(15); // Rounded corners
        background.setArcHeight(15);
        background.setLayoutX((1280 - rectWidth) / 2); // Center horizontally
        background.setLayoutY((720 - rectHeight) / 2); // Center vertically

        Label announcement = new Label("Game Over!");
        announcement.getStyleClass().add("ChessGameText");

        Label playerInfo = new Label(player1Name + " vs " + player2Name);
        playerInfo.getStyleClass().add("greetingText");

        Label gameResult = new Label("Result: " + result);
        gameResult.getStyleClass().add("greetingText");

        // Return button styled using CSS
        Button returnButton = new Button("Return");
        returnButton.getStyleClass().add("custom-button");

        returnButton.setOnAction(event -> {
            if (onReturn != null) {
                setVisible(false); // Hide the announcement
                onReturn.run();    // Execute return action
            }
        });

        // Layout using VBox
        VBox contentBox = new VBox(20); // Vertical spacing
        contentBox.setAlignment(Pos.CENTER);
        contentBox.getChildren().addAll(announcement, playerInfo, gameResult, returnButton);

        // Position content in the center of the rectangle
        contentBox.setLayoutX((1280 - rectWidth) / 2); // Same X as background
        contentBox.setLayoutY((720 - rectHeight) / 2); // Same Y as background
        contentBox.setPrefWidth(rectWidth); // Match width of the rectangle
        contentBox.setPrefHeight(rectHeight); // Match height of the rectangle

        // Add elements to the pane
        getChildren().addAll(background, contentBox);

        // Set pane size
        setPrefSize(1280, 720);
    }
}
