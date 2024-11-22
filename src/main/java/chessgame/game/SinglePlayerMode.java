package chessgame.game;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import chessgame.engine.StockfishEngineDemo;
import chessgame.logic.Move;
import chessgame.ui.BoardPane;
import chessgame.ui.CountdownTimer;

public class SinglePlayerMode {
    private StockfishEngineDemo stockfish = new StockfishEngineDemo();
    
    private CountdownTimer countdownTimerTop;
    private CountdownTimer countdownTimerBottom;

    @FXML
    private Label timerLabelTop;
    @FXML
    private Label timerLabelBottom;
    
    @FXML
    private BoardPane singleBoardPane;
    
    @FXML
    public void initialize() {
        stockfish.start(); 
        countdownTimerTop = new CountdownTimer(10 * 60);
        countdownTimerTop.setLabel(timerLabelTop);
        
        countdownTimerBottom = new CountdownTimer(10 * 60);
        countdownTimerBottom.setLabel(timerLabelBottom);
        countdownTimerBottom.start();
        // singleBoardPane.setGameMode("twoPlayer");
        singleBoardPane.setReverse(false);
        singleBoardPane.setOnMovePiece((tmp) -> {
            new Thread(() -> {
                if(singleBoardPane.getCurrentTurn().equals("w")) {
                    countdownTimerTop.stop();
                    countdownTimerBottom.start();
                    return;
                }
                countdownTimerTop.start();
                countdownTimerBottom.stop();

                stockfish.setPosition(singleBoardPane.getMove("all"));
                Move bestMove = new Move(stockfish.getBestMove());
                System.out.println(bestMove);
            
                Platform.runLater(() -> {
                    singleBoardPane.movePiece(bestMove);
                    System.out.println(
                        bestMove.getStartRow() + " " + bestMove.getStartCol() + " " +
                        bestMove.getEndRow() + " " + bestMove.getEndCol()
                    );
                });
            }).start();
        });

        singleBoardPane.start();
    }
}
