package chessgame.game;

import chessgame.engine.StockfishEngineDemo;
import chessgame.logic.Move;
import javafx.application.Platform;

public class SinglePlayer extends MainGame{
    private StockfishEngineDemo stockfish = new StockfishEngineDemo();
    private String playerSide = "w";

    public SinglePlayer(boolean isBoardReverse){
        super("singlePlayer", isBoardReverse);
        stockfish.start();
        if(playerSide.equals("w")){
            setPlayerTop("StockFish", "???", "b");
            setPlayerBottom("Player(You)", "???", "w");
        }else{
            setPlayerTop("StockFish", "???", "w");
            setPlayerBottom("Player(You)", "???", "b");
        }
    }

    @Override
    protected void handleOnMovePiece(String currentTurn) {
        if(currentTurn.equals(playerSide)){
            return;
        }
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
    }
}