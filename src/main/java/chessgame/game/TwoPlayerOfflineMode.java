package chessgame.game;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import chessgame.engine.StockfishEngineDemo;
import chessgame.logic.Move;
import chessgame.ui.BoardPane;
import chessgame.ui.CountdownTimer;
import chessgame.ui.PlayerSection;

public class TwoPlayerOfflineMode {
    
    private CountdownTimer countdownTimerTop;
    private CountdownTimer countdownTimerBottom;

    @FXML
    private VBox rightSection;

    @FXML
    private BoardPane boardPane;
    
    @FXML
    public void initialize() {
        boardPane.setGameMode("twoPlayer");
        boardPane.setReverse(false);
        PlayerSection playerSectionTop = new PlayerSection("Player 1", "200", 600, "b");
        PlayerSection playerSectionBottom = new PlayerSection("Player 1", "200", 600, "w");
        rightSection.getChildren().addAll(playerSectionTop);
        rightSection.getChildren().addAll(playerSectionBottom);
        boardPane.setOnMovePiece((tmp) -> {
            new Thread(() -> {
                if(boardPane.getCurrentTurn().equals("w")) {
                    playerSectionTop.stopTimer();
                    playerSectionBottom.startTimer();
                }else{
                    playerSectionTop.startTimer();
                    playerSectionBottom.stopTimer();
                }
            }).start();
        });
        boardPane.start();
        playerSectionBottom.startTimer();
    }
}
