package chessgame.game;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import chessgame.engine.StockfishEngineDemo;
import chessgame.logic.Move;
import chessgame.ui.BoardPane;
import chessgame.ui.CountdownTimer;

public class TwoPlayerOfflineMode {
    private StockfishEngineDemo stockfish = new StockfishEngineDemo();
    
    private CountdownTimer countdownTimerTop;
    private CountdownTimer countdownTimerBottom;

    @FXML
    private Label timerLabelTop;
    @FXML
    private Label timerLabelBottom;
    
    @FXML
    private BoardPane boardPane;
    
    @FXML
    public void initialize() {
        // Example: Add pieces dynamically when the controller is initialized
        stockfish.start(); 
        countdownTimerTop = new CountdownTimer(10 * 60);
        countdownTimerTop.setLabel(timerLabelTop);
        
        countdownTimerBottom = new CountdownTimer(10 * 60);
        countdownTimerBottom.setLabel(timerLabelBottom);
        countdownTimerBottom.start();
        boardPane.setGameMode("twoPlayer");
        
        boardPane.setReverse(false);
        boardPane.setOnMovePiece((tmp) -> {
            new Thread(() -> {
                if(boardPane.getCurrentTurn().equals("w")) {
                    countdownTimerTop.stop();
                    countdownTimerBottom.start();
                    return;
                }
            }).start();
        });

        boardPane.start();
    }
}
