package chessgame.ui;

import chessgame.network.packets.GeneralPackets.RankingListResponse;
import chessgame.network.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class RankingListController {

    @FXML
    private VBox playerListContainer;

    @FXML
    public void updateRankingList(RankingListResponse response, User user) {
        Platform.runLater(() -> {
            playerListContainer.getChildren().clear();
            for (int i = 0; i < response.rankingList.size(); i++) {
                HBox playerRow = new HBox(0);
                playerRow.setPrefWidth(830);
                playerRow.setAlignment(Pos.CENTER_LEFT);

                Label rankLabel = new Label("#" + (i + 1));
                rankLabel.setPrefWidth(70);
                rankLabel.setStyle("-fx-font-size: 14px;");
                rankLabel.setAlignment(Pos.CENTER);

                Label playerLabel = new Label(response.rankingList.get(i).userName);
                playerLabel.setPrefWidth(500);
                playerLabel.setStyle("-fx-font-size: 14px;");
                playerLabel.setAlignment(Pos.CENTER_LEFT);

                Label eloLabel = new Label(String.valueOf(response.rankingList.get(i).elo));
                eloLabel.setPrefWidth(150);
                eloLabel.setStyle("-fx-font-size: 14px;");
                eloLabel.setAlignment(Pos.CENTER);

                if (i == 0) {
                    playerRow.getStyleClass().add("top-1");
                    rankLabel.getStyleClass().add("label-top-1");
                    eloLabel.getStyleClass().add("label-top-1");
                    playerLabel.getStyleClass().add("label-top-1");
                } else if (i == 1) {
                    playerRow.getStyleClass().add("top-2");
                    rankLabel.getStyleClass().add("label-top-2");
                    eloLabel.getStyleClass().add("label-top-2");
                    playerLabel.getStyleClass().add("label-top-2");
                } else if (i == 2) {
                    playerRow.getStyleClass().add("top-3");
                    rankLabel.getStyleClass().add("label-top-3");
                    eloLabel.getStyleClass().add("label-top-3");
                    playerLabel.getStyleClass().add("label-top-3");
                }

                playerRow.getChildren().addAll(rankLabel, playerLabel, eloLabel);

                Line line = new Line();
                line.setStartX(10);
                line.setEndX(820);
                line.setStartY(0);
                line.setEndY(0);
                line.setStroke(Color.GREEN);
                line.setStrokeWidth(1);

                VBox playerBox = new VBox(5);
                playerBox.getChildren().addAll(playerRow, line);
                playerListContainer.getChildren().add(playerBox);
            }
        });
    }
}