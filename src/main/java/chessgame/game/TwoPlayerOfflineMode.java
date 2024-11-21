package chessgame.game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import chessgame.logic.Board;
// import logic.Move;

public class TwoPlayerOfflineMode extends Application {
    private Board board = new Board();
    private int [] selectedSquare =null;
    private static final int SQUARE_SIZE = 80;
    private static final int BOARD_SIZE = 8;
    private GridPane gridPane;
    private boolean gameRunning;

    public void start(Stage primaryStage){
        gridPane = new GridPane();
        gameRunning = true;
        drawBoard();

        Scene scene = new Scene(gridPane, SQUARE_SIZE * BOARD_SIZE, SQUARE_SIZE * BOARD_SIZE);
        primaryStage.setTitle("Two Player Chess Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Lắng nghe sự kiện thoát game
        primaryStage.setOnCloseRequest(event -> {
            exitGame();
        });
    }

    private void drawBoard(){
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Rectangle square = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
                // Vẽ bàn cờ và các quân cờ
                final int finalRow = row;
                final int finalCol = col;

                // lắng nghe sự kiện click chuột
                square.setOnMouseClicked(event -> handleClick(finalRow, finalCol));
                gridPane.add(square, finalRow, finalCol);
            }
        }
    }

    private void handleClick(int row, int col){
        if (board.isEmpty(row, col)) return;
        if(selectedSquare == null || board.isCorrectTurn(row, col)){
            selectPiece(row, col);
        }else{
            int startRow = selectedSquare[0];
            int startCol = selectedSquare[1];

            if(board.isValidMove(startRow, startCol, row, col)){
                playturn(startRow, startCol, row, col);

                // Thoát game nếu có người dành chiến thắng
                if(!gameRunning){
                    exitGame();
                    return;
                }
            }
        }
    }
    private void selectPiece(int row,int col){
        if(board.isCorrectTurn(row, col)){
            selectedSquare = new int[]{row, col};
            // Thực hiện highlight quân cờ
        }
    }

    private void playturn(int startRow, int startCol, int endRow, int endCol){
        // Bỏ highlight
        // Di chuyển quân cờ(UI)

        // Di chuyển quân cờ(logic)
        board.movePiece(startRow, startCol, endRow, endCol);

        // Biến kiểm tra trạng thái của game
        String gamestate = board.gameState();

        // Trò chơi tiếp tục
        if(gamestate.equals("ongoing")) return;

        // Trò chơi kết thúc
        // Giao diện chiến thắng hoặc hòa(UI)
        System.out.println((board.getCurrentTurn().equals("w")) ? "blackPlayer" : "whitePlayer" + gamestate );
        gameRunning =false;
    }

    // Thoát game
    private void exitGame() {
        System.out.println("Đã thoát khỏi trò chơi");
        System.exit(0); // Thoát chương trình
    }

    public static void main(String[] args) {
        launch(args);
    }
}