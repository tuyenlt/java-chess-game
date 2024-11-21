package chessgame.ui;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.List;
import java.util.function.Consumer;

import chessgame.logic.Board;
import chessgame.logic.Move;
import chessgame.logic.Piece;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class BoardPane extends Pane{
    private static final int TILE_SIZE = 90; 
    private static final int BOARD_SIZE = 8; 
    private Board board = new Board();
    private ImageView draggedPiece; // The piece that is being dragged
    private final Rectangle[][] tiles = new Rectangle[BOARD_SIZE][BOARD_SIZE];
    private Circle[][] moveHightLight = new Circle[8][8];
    private Circle[][] takeableHightLight = new Circle[8][8];
    private ImageView[][] piecesImage = new ImageView[8][8];
    private Rectangle boxHightlightRect;
    private Pair chossingBox;
    private String gameMode = "singlePlayer";
    private String localPlayerSide = "w";
    private Consumer<String> onMovePiece;
    private boolean isBoardReverse = false;
    private PromotionSelect promotionSelectTop;
    private PromotionSelect promotionSelectBot;
    private Pane blockingPane = new Pane();



    private class Pair{
        public int col;
        public int row;
        public Pair(){

        }
        public Pair(int col, int row){
            this.col = col;
            this.row = row;
        }
    }

    public void setOnMovePiece(Consumer<String> onMovePiece){
        this.onMovePiece = onMovePiece;
    }

    public void setGameMode(String gameMode){
        this.gameMode = gameMode;
    }

    public void setLocalPlayerSide(String localPlayerSide){
        this.localPlayerSide = localPlayerSide;
    }

    public BoardPane() {
        // this.isTwoPlayerMode = isTwoPlayerMode;
        setPrefSize(TILE_SIZE * BOARD_SIZE, TILE_SIZE * BOARD_SIZE);
        createBoard();
        initPiece();
        promotionSelectInit();

        blockingPane = new Pane();
        blockingPane.setPrefSize(getPrefWidth(), getPrefHeight());
        blockingPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);");
        blockingPane.setVisible(false);

        // Intercept all mouse events
        blockingPane.setOnMouseClicked(event -> event.consume());
        blockingPane.setOnMousePressed(event -> event.consume());
        blockingPane.setOnMouseDragged(event -> event.consume());

        getChildren().add(blockingPane);
    }
    

    private void createBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                // Create a tile (rectangle)
                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
                tile.setFill((row + col) % 2 == 0 ? Color.valueOf("#EBECD0") : Color.valueOf("#739552"));
                tile.setX(col * TILE_SIZE);
                tile.setY(row * TILE_SIZE);
                
                tile.setOnMouseClicked(event -> {
                    int Col = (int)(event.getX() / TILE_SIZE);
                    int Row = (int)(event.getY() / TILE_SIZE);
                    if(chossingBox != null){
                        movePiece(chossingBox.row, chossingBox.col, Row, Col);
                    }

                });

                tiles[row][col] = tile;

                Circle moveHightlightCircle = new Circle(tile.getX() + TILE_SIZE / 2, tile.getY() + TILE_SIZE / 2, 15);
                moveHightlightCircle.setFill(Color.rgb(0, 0, 0, 0.2));
                moveHightlightCircle.setVisible(false);
                moveHightLight[row][col] = moveHightlightCircle; 
                
                

                Circle takeableHightlightCircle = new Circle(tile.getX() + TILE_SIZE / 2, tile.getY() + TILE_SIZE / 2, TILE_SIZE / 2 - 5);
                takeableHightlightCircle.setFill(Color.TRANSPARENT);
                takeableHightlightCircle.setVisible(false);
                takeableHightlightCircle.setStroke(Color.rgb(0, 0, 0, 0.2));
                takeableHightlightCircle.setStrokeWidth(8);
                takeableHightLight[row][col] = takeableHightlightCircle;                 

                moveHightlightCircle.setOnMouseClicked(event->{
                    tile.fireEvent(event);
                });

                takeableHightlightCircle.setOnMouseClicked(event->{
                    tile.fireEvent(event);
                });
                getChildren().add(tile);
                getChildren().add(moveHightlightCircle);
                getChildren().add(takeableHightlightCircle);
            }
        }
        boxHightlightRect = new Rectangle(TILE_SIZE, TILE_SIZE);
        boxHightlightRect.setFill(Color.TRANSPARENT);
        boxHightlightRect.setVisible(false);
        boxHightlightRect.setStroke(Color.web("#CEDAC3"));
        boxHightlightRect.setStrokeWidth(5);
        getChildren().add(boxHightlightRect);
    }
    private void initPiece(){
        String[][] broadState = board.getBoardState();
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(!broadState[i][j].equals(" ")){
                    if(isBoardReverse){
                        addPiece(broadState[i][j], 7 - j, 7 - i);
                    }else{
                        addPiece(broadState[i][j], j, i);
                    }
                }
            }
        }
    }

    public void addPiece(String pieceName, int col, int row) {
        try {
            Image image = new Image(getClass().getResource("/chessgame/image/pieces/" + pieceName + ".png").toExternalForm());
            ImageView piece = new ImageView(image);
            piece.setFitWidth(TILE_SIZE ); 
            piece.setFitHeight(TILE_SIZE);
            piece.setPickOnBounds(true);
            piece.setX(col * TILE_SIZE);
            piece.setY(row * TILE_SIZE);

            piece.setOnMouseClicked(event -> {
                tiles[row][col].fireEvent(event);
            });

            piecesImage[row][col] = piece;            
            piece.setOnMousePressed(event -> onPiecePressed(event));
            piece.setOnMouseDragged(event -> onPieceDragged(event));
            piece.setOnMouseReleased(event -> onPieceReleased(event));
            piece.setOnMouseEntered(event -> piece.setCursor(Cursor.HAND));

            getChildren().add(piece);
        } catch (Exception e) {
            System.err.println("Could not load image: " + pieceName + ".png");
        }
    }

    private void promotionSelectInit(){
        String topSide = "w";
        String botSide = "b";
        if(isBoardReverse){
            topSide = "b";
            botSide = "w";
        }
        promotionSelectTop = new PromotionSelect(topSide, !isBoardReverse, TILE_SIZE);
        promotionSelectTop.setLayoutX(0);
        promotionSelectTop.setLayoutY(0);
        promotionSelectTop.setOnFinish((name) -> {
            Move promotionMove = promotionSelectTop.getPromotionMove();
            promotionMove.setPromotedPieceType(name.substring(1));
            board.movePiece(promotionMove);
            onMovePiece.accept(board.getCurrentTurn());
            getChildren().remove(draggedPiece);
            addPiece(name, promotionMove.getEndCol(), promotionMove.getEndRow());
            draggedPiece = null;
            blockingPane.setVisible(false);
            System.out.println(name);
        });
        
        promotionSelectBot = new PromotionSelect(botSide, isBoardReverse, TILE_SIZE);
        promotionSelectBot.setLayoutX(0);
        promotionSelectBot.setLayoutY(4 * TILE_SIZE);
        promotionSelectBot.setOnFinish((name) -> {
            Move promotionMove = promotionSelectBot.getPromotionMove();
            promotionMove.setPromotedPieceType(name.substring(1));
            board.movePiece(promotionMove);
            onMovePiece.accept(board.getCurrentTurn());
            getChildren().remove(draggedPiece);
            addPiece(name, promotionMove.getEndCol(), promotionMove.getEndRow());
            draggedPiece = null;
            blockingPane.setVisible(false);
            System.out.println(name);
        });
        getChildren().addAll(promotionSelectTop, promotionSelectBot);
    }

    private void onPiecePressed(MouseEvent event) {
        int row = (int) (event.getSceneY() / TILE_SIZE);
        int col = (int) (event.getSceneX() / TILE_SIZE);
        if(!board.isCorrectTurn(row, col)){
            if(!takeableHightLight[row][col].isVisible()){
                draggedPiece = null;
            }
            return;
        }
        if(!gameMode.equals("twoPlayer") && !board.getCurrentTurn().equals(localPlayerSide)){
            draggedPiece = null;
            return;
        }
        System.out.println("chossing:" + row + " " + col);

        draggedPiece = (ImageView) event.getSource();
        draggedPiece.setViewOrder(-1);
        draggedPiece.setX(event.getSceneX() - TILE_SIZE / 2);
        draggedPiece.setY(event.getSceneY() - TILE_SIZE / 2);
        draggedPiece.setCursor(Cursor.HAND);

        resetState();
        chossingBox = new Pair(col, row);
        hightlightMove(row, col);
        boxHightlightRect.setVisible(true);
        System.out.println("chossing box: "+ chossingBox.col + " " + chossingBox.row);
    }


    private void onPieceDragged(MouseEvent event) {
        if (draggedPiece != null) {
            draggedPiece.setCursor(Cursor.CLOSED_HAND);
            draggedPiece.setX(event.getSceneX() - TILE_SIZE / 2);
            draggedPiece.setY(event.getSceneY() - TILE_SIZE / 2);
            boxHightlightRect.setX((int)(event.getSceneX() / TILE_SIZE) * TILE_SIZE);
            boxHightlightRect.setY((int)(event.getSceneY() / TILE_SIZE) * TILE_SIZE);
        }
    }

    private void onPieceReleased(MouseEvent event) {
        if (draggedPiece != null) {
            draggedPiece.setViewOrder(0);
            draggedPiece.setCursor(Cursor.DEFAULT);
            int endRow = (int) (event.getSceneY() / TILE_SIZE);
            int endCol = (int) (event.getSceneX() / TILE_SIZE);
            boxHightlightRect.setVisible(false);
            int startRow = chossingBox.row;
            int startCol = chossingBox.col;
            if(!movePiece(startRow, startCol, endRow, endCol)){
                draggedPiece.setX(startCol * TILE_SIZE);
                draggedPiece.setY(startRow * TILE_SIZE);
            }
        }
    }

    private void hightlightMove(int row, int col){
        if(isBoardReverse){
            row = 7 - row;
            col = 7 - col;
        }
        Piece chossingPiece = board.getPiece(row, col);
        if(chossingPiece == null){
            return;
        }
        tiles[row][col].setFill(Color.web("#F5F682"));
        List<Move> moves = chossingPiece.getSafeMoves(board, row, col);
        for(Move move : moves){
            int endRow = move.getEndRow();
            int endCol = move.getEndCol();
            if(isBoardReverse){
                endRow = 7 - endRow;
                endCol = 7 - endCol;
            }
            if(board.getPiece(endRow, endCol) != null){
                takeableHightLight[endRow][endCol].setVisible(true);
            }else{
                moveHightLight[endRow][endCol].setVisible(true);
            }
        }
    }

    public void resetState(){
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                moveHightLight[i][j].setVisible(false);
            }
        }
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                takeableHightLight[i][j].setVisible(false);
            }
        }
        if(chossingBox != null){
            tiles[chossingBox.row][chossingBox.col].setFill((chossingBox.row + chossingBox.col) % 2 == 0 ? Color.web("#EBECD0") : Color.web("#739552"));
        }
    }

    public boolean movePiece(int startRow, int startCol, int endRow, int endCol){
        if(startCol == endCol && startRow == endRow){
            return false;
        }
        if(isBoardReverse){
        }
        resetState();
        chossingBox = null;
        System.out.println("move to: " + endRow + " " + endCol);
        Move newMove = new Move(startRow, startCol, endRow, endCol);
        Piece chossingPiece = board.getPiece(startRow, startCol);
        for(Move move : chossingPiece.getSafeMoves(board, startRow, startCol)){
            if(move.equals(newMove)){
                if(piecesImage[endRow][endCol] != null){
                    getChildren().remove(piecesImage[endRow][endCol]);
                }
                if(newMove.isCastling(board)){
                    handleCasleMove(newMove);
                }
                if(newMove.isPromotion(board)){
                    blockingPane.setVisible(true);
                    if(promotionSelectTop.getColor().equals(board.getCurrentTurn())){
                        promotionSelectTop.setVisible(true);
                        promotionSelectTop.setLayoutX(endCol * TILE_SIZE);
                        promotionSelectTop.setPromotionMove(newMove);
                    }else{
                        promotionSelectBot.setVisible(true);
                        promotionSelectBot.setLayoutX(endCol * TILE_SIZE);
                        promotionSelectBot.setPromotionMove(newMove);
                    }
                    return true;
                }
                board.movePiece(newMove);
                onMovePiece.accept(board.getCurrentTurn());

                piecesImage[endRow][endCol] = piecesImage[startRow][startCol];
                if(piecesImage[endRow][endCol] != null){
                    piecesImage[endRow][endCol].setX(endCol * TILE_SIZE);
                    piecesImage[endRow][endCol].setY(endRow * TILE_SIZE);
                }
                piecesImage[startRow][startCol] = null;
                return true;
            }
        }
        return false;
    }

    private void handleCasleMove(Move move){
        System.out.println("pass");
        int rookEndCol = move.getEndCol() + (move.getEndCol() < move.getStartCol() ? 1 : -1);
        int rookStartCol = (move.getEndCol() < move.getStartCol() ? 0 : 7);
        piecesImage[move.getEndRow()][rookEndCol] = piecesImage[move.getEndRow()][rookStartCol];
        piecesImage[move.getEndRow()][rookEndCol].setX(rookEndCol * TILE_SIZE);
        piecesImage[move.getEndRow()][rookEndCol].setY(move.getEndRow() * TILE_SIZE);
        piecesImage[move.getEndRow()][rookStartCol] = null;
    }

    public String getGameState(){
        return board.gameState();
    }

    public List<String> getMove(String side){
        return board.getMoves(side);
    }

    public String getCurrentTurn(){
        return board.getCurrentTurn();
    }
}
