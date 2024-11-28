package chessgame.ui;

import chessgame.network.packets.GeneralPackets.RankingListResponse;
import chessgame.network.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class HistoryController {

    @FXML
    private VBox historyContainer;

    @FXML
    public void updateHistory() {
        Platform.runLater(() -> {
            historyContainer.getChildren().clear();

            for (int i = 0; i < 30; i++) {
                // Button bao quanh các Label
                Button historyButton = new Button();
                historyButton.setPrefWidth(800);
                historyButton.getStylesheets().add("chessgame/style.css");
                historyButton.getStyleClass().add("history-button");
                historyButton.setOnAction(event -> {


                });

                HBox contentRow = new HBox();
                contentRow.setPrefWidth(800);

                Label opponentLabel = new Label("Opponent " + (i + 1));
                opponentLabel.setPrefWidth(500);
                opponentLabel.setStyle("-fx-font-size: 14px;");
                opponentLabel.setAlignment(Pos.CENTER);

                Label resultLabel = new Label(i % 2 == 0 ? "lose" : "win");
                resultLabel.setStyle(i % 2 == 0 ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
                resultLabel.setPrefWidth(100);
                resultLabel.setAlignment(Pos.CENTER);

                Label moveLabel = new Label(String.valueOf(i + 1));
                moveLabel.setPrefWidth(100);
                moveLabel.setStyle("-fx-font-size: 14px;");
                moveLabel.setAlignment(Pos.CENTER);

                contentRow.getChildren().addAll(opponentLabel, resultLabel, moveLabel);

                // Đặt nội dung vào Button
                historyButton.setGraphic(contentRow);

                // Thêm line separator
                Line line = new Line(0, 0, 830, 0);
                line.setStroke(Color.LIGHTGRAY);
                line.setStrokeWidth(1);

                // Gộp Button và line vào VBox
                VBox historyBox = new VBox(5);
                historyBox.getChildren().addAll(historyButton, line);

                // Thêm vào container
                historyContainer.getChildren().add(historyBox);
            }
        });
    }

}