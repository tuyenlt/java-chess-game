package chessgame.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import chessgame.engine.StockfishEngineDemo;
import chessgame.logic.Move;
import chessgame.ui.BoardPane;

public class GameController {
    private StockfishEngineDemo stockfish = new StockfishEngineDemo();

    @FXML
    private BoardPane boardPane;

    @FXML
    public void initialize() {
        // Example: Add pieces dynamically when the controller is initialized
        stockfish.start(); 
        boardPane.setOnMovePiece((tmp) -> {
            new Thread(() -> {
                if (boardPane.getCurrentTurn().equals("w")) {
                    return;
                }
            
                // Run Stockfish logic in the background
                stockfish.setPosition(boardPane.getMove("a"));
                Move bestMove = new Move(stockfish.getBestMove());
                System.out.println(bestMove);
            
                // Update the UI safely
                Platform.runLater(() -> {
                    boardPane.otherMovePiece(
                        bestMove.getStartRow(), 
                        bestMove.getStartCol(), 
                        bestMove.getEndRow(), 
                        bestMove.getEndCol()
                    );
                    System.out.println(
                        bestMove.getStartRow() + " " + bestMove.getStartCol() + " " +
                        bestMove.getEndRow() + " " + bestMove.getEndCol()
                    );
                });
            }).start();
        });
    }
}
