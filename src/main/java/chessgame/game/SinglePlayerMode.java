package chessgame.game;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import chessgame.engine.StockfishEngineDemo;
import chessgame.logic.Move;
import chessgame.ui.BoardPane;
import chessgame.ui.CountdownTimer;
import chessgame.ui.PlayerSection;

public class SinglePlayerMode {
    private StockfishEngineDemo stockfish = new StockfishEngineDemo();
    
    @FXML
    private BoardPane singleBoardPane;

    @FXML
    private VBox rightSection;
    
    @FXML
    public void initialize() {
        stockfish.start(); 
        PlayerSection playerSectionTop = new PlayerSection("StockFish Bot", "???", 600, "b");
        PlayerSection playerSectionBottom = new PlayerSection("Player 1", "200", 600, "w");
        rightSection.getChildren().addAll(playerSectionTop);
        rightSection.getChildren().addAll(playerSectionBottom);
        singleBoardPane.setReverse(false);
        singleBoardPane.setOnMovePiece((tmp) -> {
            new Thread(() -> {
                if(singleBoardPane.getCurrentTurn().equals("w")) {
                    playerSectionTop.stopTimer();
                    playerSectionBottom.startTimer();
                }else{
                    playerSectionTop.startTimer();
                    playerSectionBottom.stopTimer();
                }
                if(singleBoardPane.getCurrentTurn().equals("w")){
                    return;
                }

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
