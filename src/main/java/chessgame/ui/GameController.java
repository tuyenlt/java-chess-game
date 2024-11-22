package chessgame.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import chessgame.engine.StockfishEngineDemo;
import chessgame.logic.Move;
import chessgame.ui.BoardPane;

public class GameController {
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
        // boardPane.setGameMode("twoPlayer");
        // boardPane.setR
        boardPane.setReverse(false);
        boardPane.setOnMovePiece((tmp) -> {
            new Thread(() -> {
                if(boardPane.getCurrentTurn().equals("w")) {
                    countdownTimerTop.stop();
                    countdownTimerBottom.start();
                    return;
                }
                countdownTimerTop.start();
                countdownTimerBottom.stop();

                stockfish.setPosition(boardPane.getMove("all"));
                Move bestMove = new Move(stockfish.getBestMove());
                System.out.println(bestMove);
            
                Platform.runLater(() -> {
                    boardPane.movePiece(bestMove);
                    System.out.println(
                        bestMove.getStartRow() + " " + bestMove.getStartCol() + " " +
                        bestMove.getEndRow() + " " + bestMove.getEndCol()
                    );
                });
            }).start();
        });

        boardPane.start();
    }
}
