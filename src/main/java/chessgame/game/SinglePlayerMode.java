package chessgame.game;

import chessgame.engine.StockfishEngineDemo;
import chessgame.logic.Move;
import javafx.application.Platform;

public class SinglePlayerMode extends MainGame{
    private StockfishEngineDemo stockfish = new StockfishEngineDemo();
    private String playerSide = "w";
    private boolean isBoardReverse;
    
    public SinglePlayerMode(boolean isBoardReverse){
        super("singlePlayer", isBoardReverse);
        this.isBoardReverse = isBoardReverse;
        if(isBoardReverse){
            playerSide = "b";
        }
        if(playerSide.equals("w")){
            setPlayerTop("StockFish", "???", "b");
            setPlayerBottom("You", "???", "w");
        }else{
            setPlayerTop("StockFish", "???", "w");
            setPlayerBottom("You", "???", "b");
        }

        gameOptionsMenu.addButton("Reverse", "custom-button", event->{
            this.isBoardReverse = !this.isBoardReverse;
            if(this.isBoardReverse){
                playerSide = "b";
            }else{
                playerSide = "w";
            }
            stockfish.stop();
            stockfish = new StockfishEngineDemo();
            createBoard(playerSide, this.isBoardReverse);
        });
    }

    @Override
    protected void handleOnMovePiece(String currentTurn) {
        try{
            while(stockfish == null){
                Thread.sleep(100);
            }
        }catch(Exception e){
            
        }
        if(currentTurn.equals(playerSide)){
            return;
        }
        stockfish.setPosition(boardPane.getMove("all"));
        for(String move : boardPane.getMove("all")){
            System.out.print(move + " ");
        }
        System.out.println();
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