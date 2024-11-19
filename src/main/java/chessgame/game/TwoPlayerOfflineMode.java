package game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import logic.Board;
import engine.StockfishEngineDemo;
// import logic.Move;

public class SinglePlayerMode extends Application {
    private Board board = new Board();
    StockfishEngineDemo stockfish;
    private int [] selectedSquare =null;
    private static final int SQUARE_SIZE = 80;
    private static final int BOARD_SIZE = 8;
    private GridPane gridPane;
    private boolean gameRunning;

    public void start(Stage primaryStage){
        gridPane = new GridPane();
        stockfish = new StockfishEngineDemo();

        // Thử khởi động stocfish, nếu không thành công thông báo lỗi!
        if(!stockfish.start()){
            System.out.println("Stockfish đang gặp vấn đề!");
        }

        gameRunning = true;
        drawBoard();

        Scene scene = new Scene(gridPane, SQUARE_SIZE * BOARD_SIZE, SQUARE_SIZE * BOARD_SIZE);
        primaryStage.setTitle("Single Player Chess Game");
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
                // Vẽ bàn cờ tô màu trắng đen cho các ô vuông(UI)
                // Vẽ các quân cờ(UI)

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

                if(!gameRunning){
                    exitGame();
                    return;
                }
                botTurn();
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

        if(gamestate.equals("draw")) System.out.println("Draw!");       // Chỗ này sẽ thay bằng giao diện cờ hòa(UI)
        else{
            // Thay bằng giao diện chiến thắng hoặc thua
            System.out.println((board.getCurrentTurn().equals("w")) ? "You Lost" : "You Win!");
        }
        gameRunning =false;
    }

    // Lượt chơi của máy
    private void botTurn(){
        stockfish.setPosition(board.getLastMove());
        String botMove = stockfish.getBestMove();
        board.movePiece(botMove);
        stockfish.setPosition(botMove);
    }

    // Thoát game
    private void exitGame() {
        if (stockfish != null) {
            stockfish.stop();
        }
        System.out.println("Đã thoát khỏi trò chơi");
        System.exit(0);
    }
}