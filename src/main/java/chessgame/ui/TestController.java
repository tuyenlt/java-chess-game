package chessgame.ui;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

public class TestController {

    @FXML
    private VBox playerList; // Liên kết với VBox trong FXML


    public void initialize() {
        // Thêm 30 người chơi vào danh sách
        for (int i = 1; i <= 30; i++) {
            HBox playerRow = new HBox(0); // Khoảng cách giữa các cột
//            playerRow.setStyle("-fx-alignment: CENTER_LEFT;");

            // Cột Rank
            Label rankLabel = new Label("#" + i);
            rankLabel.setPrefWidth(80);
            rankLabel.setStyle("-fx-font-size: 14px;");
            rankLabel.setAlignment(javafx.geometry.Pos.CENTER);

            // Cột Player
            Label playerLabel = new Label("Player" + i);
            playerLabel.setPrefWidth(500);
            playerLabel.setStyle("-fx-font-size: 14px;");
            playerLabel.setAlignment(Pos.CENTER_LEFT);

            // Cột ELO
            Label eloLabel = new Label(String.valueOf(1000 + i * 10));
            eloLabel.setPrefWidth(150);
            eloLabel.setStyle("-fx-font-size: 14px;");
            eloLabel.setAlignment(javafx.geometry.Pos.CENTER);

            // Thêm các cột vào dòng
            playerRow.getChildren().addAll(rankLabel, playerLabel, eloLabel);

            // Đường phân cách
            Line line = new Line();
            line.setStartX(10); // Khoảng cách từ trái (dịch vào 10px)
            line.setEndX(830 - 10); // Khoảng cách từ phải (dịch vào 10px)
            line.setStartY(0); // Vị trí dọc (giữ nguyên)
            line.setEndY(0);
            line.setStroke(Color.GREEN);
            line.setStrokeWidth(1);

            // Thêm dòng vào danh sách
            VBox playerBox = new VBox(5);
            playerBox.getChildren().addAll(playerRow, line);

            playerList.getChildren().add(playerBox);
        }
    }

}
