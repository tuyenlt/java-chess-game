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
    private boolean isBoardReverse = true;
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

    public void start(){
        if(isBoardReverse){
            onMovePiece.accept(board.getCurrentTurn());
        }
    }

    public void setReverse(Boolean isBoardReverse){
        this.isBoardReverse = isBoardReverse;
        board.setReverse(isBoardReverse);
        if(isBoardReverse){
            localPlayerSide = "b";
        }
        initPiece();
    }

    public BoardPane(String gameMode, boolean isBoardReverse) {
        this.setLayoutX(0);
        this.setLayoutY(0);
        this.setPrefSize(720, 720);
        createBoard();
        promotionSelectInit();

        this.gameMode = gameMode;
        setReverse(isBoardReverse);

        blockingPane = new Pane();
        blockingPane.setPrefSize(getPrefWidth(), getPrefHeight());
        blockingPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);");
        blockingPane.setVisible(false);

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
                
                tiles[row][col] = tile;
                tile.setOnMouseClicked(event -> {
                    int Col = (int)(event.getX() / TILE_SIZE);
                    int Row = (int)(event.getY() / TILE_SIZE);
                    if(chossingBox != null){
                        movePiece(chossingBox.row, chossingBox.col, Row, Col);
                    }

                });


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
                }else{
                    if(isBoardReverse){
                        piecesImage[7-i][7-j] = null;
                    }else{
                        piecesImage[i][j] = null;
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

            getChildren().add(piece);
            piecesImage[row][col] = piece;            

            piece.setOnMouseClicked(event -> {
                tiles[row][col].fireEvent(event);
            });

            piece.setOnMousePressed(event -> onPiecePressed(event));
            piece.setOnMouseDragged(event -> onPieceDragged(event));
            piece.setOnMouseReleased(event -> onPieceReleased(event));
            piece.setOnMouseEntered(event -> piece.setCursor(Cursor.HAND));

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
        promotionSelectTop.setLayoutY(4 * TILE_SIZE);
        promotionSelectTop.setOnFinish((name) -> {
            handlePromotionMove(promotionSelectTop.getPromotionMove(), name);
        });
        
        promotionSelectBot = new PromotionSelect(botSide, isBoardReverse, TILE_SIZE);
        promotionSelectBot.setLayoutY(0);
        promotionSelectBot.setOnFinish((name) -> {
            handlePromotionMove(promotionSelectBot.getPromotionMove(), name);
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
        if(draggedPiece == null){
            return;
        }
        draggedPiece.setViewOrder(-1);
        draggedPiece.setX(event.getSceneX() - TILE_SIZE / 2);
        draggedPiece.setY(event.getSceneY() - TILE_SIZE / 2);
        boxHightlightRect.setX((int)(event.getSceneX() / TILE_SIZE) * TILE_SIZE);
        boxHightlightRect.setY((int)(event.getSceneY() / TILE_SIZE) * TILE_SIZE);
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
        System.out.println("Scene for boardPane: " + getScene());
        try {
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
        } catch (Exception e) {
            System.out.println("Error on draggpiece");
            e.printStackTrace();
        }
    }

    private void hightlightMove(int row, int col){
        tiles[row][col].setFill(Color.web("#F5F682"));
        if(isBoardReverse){
            row = 7 - row;
            col = 7 - col;
        }
        Piece chossingPiece = board.getPiece(row, col);
        if(chossingPiece == null){
            return;
        }
        List<Move> moves = chossingPiece.getSafeMoves(board, row, col);
        for(Move move : moves){
            if(isBoardReverse){
                move.reverseBoard();
            }
            int endRow = move.getEndRow();
            int endCol = move.getEndCol();
            if(piecesImage[endRow][endCol] != null){
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

    public boolean movePiece(Move move){
        if(isBoardReverse){
            move.reverseBoard();
        }
        return movePiece(move.getStartRow(), move.getStartCol(), move.getEndRow(), move.getEndCol(), move.getPromotedPieceType());
    }

    public boolean movePiece(int startRow, int startCol, int endRow, int endCol){
        return movePiece(startRow, startCol, endRow, endCol, "");
    }

    public boolean movePiece(int startRow, int startCol, int endRow, int endCol, String promotionType){
        try {
            if(startCol == endCol && startRow == endRow){
                return false;
            }
            resetState();
            chossingBox = null;
            Move newMove = new Move(startRow, startCol, endRow, endCol);
            if(!promotionType.equals("")){
                newMove.setPromotedPieceType(promotionType);
            }
            if(isBoardReverse){
                newMove.reverseBoard();
            }
            Piece chossingPiece = board.getPiece(newMove.getStartRow(), newMove.getStartCol());
            for(Move move : chossingPiece.getSafeMoves(board, newMove.getStartRow(), newMove.getStartCol())){
                if(move.equals(newMove)){
                    if(piecesImage[endRow][endCol] != null){
                        getChildren().remove(piecesImage[endRow][endCol]);
                    }
                    if(newMove.isPromotion(board)){
                        if(!board.getCurrentTurn().equals(localPlayerSide) && !gameMode.equals("twoPlayer")){
                            handlePromotionMove(newMove, promotionType);
                            return true;
                        }
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
                    if(move.isEnPassant()){
                        newMove.setEnPassant(true);
                    }
                    board.movePiece(newMove);
                    onMovePiece.accept(board.getCurrentTurn());
                    
                    piecesImage[endRow][endCol] = piecesImage[startRow][startCol];
                    if(piecesImage[endRow][endCol] != null){
                        piecesImage[endRow][endCol].setX(endCol * TILE_SIZE);
                        piecesImage[endRow][endCol].setY(endRow * TILE_SIZE);
                    }
                    piecesImage[startRow][startCol] = null;
                    cleanNullPiece();
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erorr on move piece");
            return false;
        }
    }

    private void handlePromotionMove(Move promotionMove, String pieceName){
        if(promotionMove.getPromotedPieceType().equals("")){
            promotionMove.setPromotedPieceType(pieceName);
        }
        String pieceImageName = board.getCurrentTurn() + pieceName.toUpperCase();
        board.movePiece(promotionMove);
        if(isBoardReverse){
            promotionMove.reverseBoard();
        }
        addPiece(pieceImageName, promotionMove.getEndCol(), promotionMove.getEndRow());
        onMovePiece.accept(board.getCurrentTurn());
        getChildren().remove(piecesImage[promotionMove.getStartCol()][promotionMove.getStartRow()]);
        piecesImage[promotionMove.getStartCol()][promotionMove.getStartRow()] = null;
        draggedPiece = null;
        blockingPane.setVisible(false);
        cleanNullPiece();
    }

    private void cleanNullPiece(){
        for(int row=0;row<8;row++){
            for(int col=0; col<8; col++){
                Piece piece = board.getPiece(row, col);
                if(isBoardReverse){
                    piece = board.getPiece(7 - row,7 - col);
                }
                if(piece == null && piecesImage[row][col] != null){
                    getChildren().remove(piecesImage[row][col]);
                }
                if(piece != null && piecesImage[row][col] == null){
                    addPiece(piece.getName(), col, row);
                }
            }
        }
    }

    public String getGameState(){
        return board.gameState();
    }

    public List<String> getMove(String side){
        return board.getMoves(side);
    }

    public Move getLastMove(){
        return board.getLastMove();
    }

    public String getCurrentTurn(){
        return board.getCurrentTurn();
    }
}
