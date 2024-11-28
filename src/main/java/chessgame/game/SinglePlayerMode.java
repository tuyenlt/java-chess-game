package chessgame.game;

import chessgame.engine.StockfishEngineDemo;
import chessgame.logic.Move;
import javafx.application.Platform;
import javafx.scene.control.ChoiceBox;

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
            setPlayerTop("StockFish", "???", "b", false);
            setPlayerBottom("You", "???", "w", false);
        }else{
            setPlayerTop("StockFish", "???", "w", false);
            setPlayerBottom("You", "???", "b", false);
        }
        ChoiceBox<String> difficultyChoice = new ChoiceBox<>();
        difficultyChoice.getItems().addAll("Easy", "Medium", "Hard", "Very Hard");
        stockfish.setDifficulty("Easy");
        difficultyChoice.setValue("Easy");
        // difficultyChoice.getStyleClass().add("custom-choice-box");
        difficultyChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            stockfish.stop();
            stockfish = new StockfishEngineDemo();
            stockfish.setDifficulty(newValue);
            createBoard(playerSide, isBoardReverse);
        });
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
        gameOptionsMenu.addNode(difficultyChoice);
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