package chessgame.ui;

import java.util.Collection;
import java.util.Collections;

import chessgame.game.HistoryGameReplay;
import chessgame.network.packets.GeneralPackets.HistoryGame;
import chessgame.network.packets.GeneralPackets.HistoryGameResponse;
import chessgame.network.packets.GeneralPackets.RankingListResponse;
import chessgame.network.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class HistoryController {

    @FXML
    private VBox historyContainer;

    @FXML
    public void updateHistory(HistoryGameResponse response, User user) {
        Platform.runLater(() -> {
            historyContainer.getChildren().clear();
            Collections.reverse(response.historyGameList);
            for(HistoryGame historyGame : response.historyGameList){
                String result;
                int totalsMove = historyGame.moves.split(" ").length;
                if(historyGame.onWhite){
                    result = historyGame.result;
                }else{
                    result = "draw";
                    if(historyGame.result.equals("win")){
                        result = "lose";
                    }
                    if(historyGame.result.equals("lose")){
                        result = "win";
                    }
                }

                // Button bao quanh các Label
                Button historyButton = new Button();
                historyButton.setPrefWidth(800);
                historyButton.getStylesheets().add("chessgame/style.css");
                historyButton.getStyleClass().add("history-button");
                historyButton.setOnAction(event -> {
                    if(historyGame.moves.equals("")){
                        return;
                    }
                    Stage replayStage = new Stage();
                    replayStage.setTitle("Game Replay");
                    replayStage.setResizable(false);
                    HistoryGameReplay historyGameReplay = new HistoryGameReplay(historyGame.moves, !historyGame.onWhite);
                    historyGameReplay.setOnReturn(replayStage::close);
                    replayStage.setScene(new Scene(historyGameReplay));
                    replayStage.show();
                });

                HBox contentRow = new HBox();
                contentRow.setPrefWidth(800);

                Label opponentLabel = new Label(historyGame.opponentName);
                opponentLabel.setPrefWidth(500);
                opponentLabel.setStyle("-fx-font-size: 14px;");
                opponentLabel.setAlignment(Pos.CENTER);

                Label resultLabel = new Label(result);
                resultLabel.setStyle(result.equals("lose") ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
                resultLabel.setPrefWidth(100);
                resultLabel.setAlignment(Pos.CENTER);

                Label moveLabel = new Label(String.valueOf(totalsMove));
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