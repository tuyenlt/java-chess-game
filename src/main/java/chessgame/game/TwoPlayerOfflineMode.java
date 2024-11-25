package chessgame.game;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import chessgame.ui.BoardPane;
import chessgame.ui.PlayerSection;

public class TwoPlayerOfflineMode {
    

    @FXML
    private VBox rightSection;

    @FXML
    private BoardPane boardPane;
    
    @FXML
    public void initialize() {
        // boardPane.setGameMode("twoPlayer");
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
