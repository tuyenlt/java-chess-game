package chessgame.ui;

import java.util.ArrayList;
import java.util.List;

import chessgame.logic.Board;
import chessgame.logic.Move;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ReplayBoard extends Pane{
    private static final int TILE_SIZE = 90; 
    private static final int BOARD_SIZE = 8; 
    private Board board = new Board();
    private ImageView[][] piecesImage = new ImageView[8][8];
    private final Rectangle[][] tiles = new Rectangle[BOARD_SIZE][BOARD_SIZE];
    private boolean isBoardReverse;
    private ArrayList<String[][]> boardStage = new ArrayList<>();
    private String[] moves;
    private int currentStateIndex = 0;

    public ReplayBoard(String gameMoves, Boolean isBoardReverse) {
        this.setLayoutX(0);
        this.setLayoutY(0);
        this.setPrefSize(720, 720);
        this.isBoardReverse = isBoardReverse;
        this.moves = gameMoves.trim().split(" ");
        createBoard();
        createBoardState(gameMoves);
        loadState(currentStateIndex);
    }

    private void createBoardState(String gameMoves){
        boardStage.add(board.getBoardState());
        for(String move : moves){
            board.movePiece(new Move(move));
            boardStage.add(board.getBoardState());
        }
    }

    private void createBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
                tile.setFill((row + col) % 2 == 0 ? Color.valueOf("#EBECD0") : Color.valueOf("#739552"));
                tile.setX(col * TILE_SIZE);
                tile.setY(row * TILE_SIZE);

                tiles[row][col] = tile;

                getChildren().add(tile);
            }
        }
    }

    public void addPiece(String pieceName, int col, int row) {
        try {
            Image image = new Image(getClass().getResource("/chessgame/image/pieces/" + pieceName + ".png").toExternalForm());
            ImageView piece = new ImageView(image);
            piece.setFitWidth(TILE_SIZE ); 
            piece.setFitHeight(TILE_SIZE);
            piece.setX(col * TILE_SIZE);
            piece.setY(row * TILE_SIZE);

            getChildren().add(piece);
            piecesImage[row][col] = piece;            

        } catch (Exception e) {
            System.err.println("Could not load image: " + pieceName + ".png");
        }
    }

    public boolean loadState(int index){
        if(index < 0 || index > moves.length){
            return false;
        }
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                int row = i;
                int col = j;
                if(isBoardReverse){
                    row = 7 - row;
                    col  = 7 - col;
                }
                if(piecesImage[row][col] != null){
                    getChildren().remove(piecesImage[row][col]);
                    piecesImage[row][col] = null;
                }
                if(!boardStage.get(index)[i][j].equals(" ")){
                    addPiece(boardStage.get(index)[i][j], col, row);   
                }
            }
        }
        return true;
    }

    public void next(){
        if(currentStateIndex >= moves.length){
            return;
        }
        currentStateIndex++;
        loadState(currentStateIndex);
    }
    
    public void prev(){
        if(currentStateIndex == 0){
            return;
        }
        currentStateIndex--;
        loadState(currentStateIndex);
    }

    public String getMovesAtIndex(int index){
        if(index < 0){
            return "";
        }
        if(index >= moves.length){
            index = moves.length - 1;
        }
        StringBuilder res = new StringBuilder();
        for(int i=0; i<= index; i++){
            res.append(moves[i] + " ");
        }
        return res.toString();
    }

    public String getMoves(){
        return getMovesAtIndex(currentStateIndex);
    }
    public String getPrevMoves(){
        return getMovesAtIndex(currentStateIndex-1);
    }
}
