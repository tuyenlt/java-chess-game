package chessgame.ui;

import chessgame.network.packets.GeneralPackets.RankingListResponse;
import chessgame.network.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
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
                HBox playerRow = new HBox(0);
                playerRow.setPrefWidth(830);
                playerRow.setAlignment(Pos.CENTER_LEFT);


                Label opponentLabel = new Label("Opponent " + (i + 1));
                opponentLabel.setPrefWidth(500);
                opponentLabel.setStyle("-fx-font-size: 14px;");
                opponentLabel.setAlignment(Pos.CENTER);
                Label resultLabel = new Label("win");
                resultLabel.setStyle("-fx-text-fill: green;");
                if(i % 2 == 0) {
                    resultLabel.setText("lose");
                    resultLabel.setStyle("-fx-text-fill: red;");
                }
                resultLabel.setPrefWidth(100);
                resultLabel.setAlignment(Pos.CENTER);

                Label eloLabel = new Label("+" + (i + 1));


                Label moveLabel = new Label(String.valueOf(i + 1));
                moveLabel.setPrefWidth(100);
                moveLabel.setStyle("-fx-font-size: 14px;");
                moveLabel.setAlignment(Pos.CENTER);

                playerRow.getChildren().addAll(opponentLabel, resultLabel, moveLabel);

                Line line = new Line();
                line.setStartX(10);
                line.setEndX(820);
                line.setStartY(0);
                line.setEndY(0);
                line.setStroke(Color.GREEN);
                line.setStrokeWidth(1);

                VBox historyBox = new VBox(5);
                historyBox.getChildren().addAll(playerRow, line);
                historyContainer.getChildren().add(historyBox);
            }
        });
    }
}