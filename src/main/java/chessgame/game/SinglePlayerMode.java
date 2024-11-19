package game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import logic.Board;

public class TwoPlayerOfflineMode extends Application {
    private Board board = new Board();
    private int[] selectedSquare = null;
    private static final int SQUARE_SIZE = 80;
    private static final int BOARD_SIZE = 8;
    private GridPane gridPane;
    private boolean gameRunning;
    private String currentPlayer = "w"; // 'w' dành cho Trắng, 'b' dành cho Đen

    @Override
    public void start(Stage primaryStage) {
        gridPane = new GridPane();
        gameRunning = true;
        drawBoard();

        Scene scene = new Scene(gridPane, SQUARE_SIZE * BOARD_SIZE, SQUARE_SIZE * BOARD_SIZE);
        primaryStage.setTitle("Two Player Chess Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Lắng nghe sự kiện thoát game
        primaryStage.setOnCloseRequest(event -> {
            exitGame();
        });
    }

    private void drawBoard() {
        gridPane.getChildren().clear(); // Xóa UI trước khi vẽ lại

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Rectangle square = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);

                // Thêm màu sắc cho bàn cờ
                square.setFill((row + col) % 2 == 0 ? Color.BEIGE : Color.BROWN);

                final int finalRow = row;
                final int finalCol = col;

                // Lắng nghe sự kiện click chuột
                square.setOnMouseClicked(event -> handleClick(finalRow, finalCol));
                gridPane.add(square, col, row);
            }
        }
    }

    private void handleClick(int row, int col) {
        if (!gameRunning) return; // Ngăn không xử lý nếu game đã kết thúc
        if (board.isEmpty(row, col) && selectedSquare == null) return; // Bỏ qua nếu chọn ô trống khi chưa chọn quân

        if (selectedSquare == null) {
            selectPiece(row, col);
        } else {
            int startRow = selectedSquare[0];
            int startCol = selectedSquare[1];

            if (board.isValidMove(startRow, startCol, row, col)) {
                playTurn(startRow, startCol, row, col);

                // Đoạn thêm: Kiểm tra trạng thái kết thúc game
                String gameState = board.gameState();
                if (!gameState.equals("ongoing")) {
                    gameRunning = false;
                    showEndGameDialog(gameState); // Hiển thị thông báo khi game kết thúc
                }

                // Chuyển lượt
                currentPlayer = currentPlayer.equals("w") ? "b" : "w"; // Đổi lượt người chơi
            }
            selectedSquare = null; // Bỏ chọn sau khi di chuyển
        }
    }

    private void selectPiece(int row, int col) {
        if (board.isCorrectTurn(row, col, currentPlayer)) {
            selectedSquare = new int[]{row, col};
            Rectangle square = (Rectangle) gridPane.getChildren().get(row * BOARD_SIZE + col);
            square.setStroke(Color.YELLOW); // Thêm viền vàng để đánh dấu quân được chọn
            square.setStrokeWidth(3);
        }
    }

    private void playTurn(int startRow, int startCol, int endRow, int endCol) {
        // Bỏ highlight
        Rectangle square = (Rectangle) gridPane.getChildren().get(startRow * BOARD_SIZE + startCol);
        square.setStroke(null);

        // Di chuyển quân cờ (UI)
        Rectangle startSquare = (Rectangle) gridPane.getChildren().get(startRow * BOARD_SIZE + startCol);
        Rectangle endSquare = (Rectangle) gridPane.getChildren().get(endRow * BOARD_SIZE + endCol);
        endSquare.setFill(startSquare.getFill());
        startSquare.setFill((startRow + startCol) % 2 == 0 ? Color.BEIGE : Color.BROWN);

        // Di chuyển quân cờ (logic)
        board.movePiece(startRow, startCol, endRow, endCol);
    }

    // Đoạn thêm: Hiển thị thông báo khi kết thúc game
    private void showEndGameDialog(String gameState) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Kết thúc trò chơi");
        alert.setHeaderText(null);

        switch (gameState) {
            case "checkmate":
                alert.setContentText((currentPlayer.equals("w") ? "Đen" : "Trắng") + " thắng bằng chiếu tướng!");
                break;
            case "stalemate":
                alert.setContentText("Trò chơi hòa vì thế bí!");
                break;
            case "draw":
                alert.setContentText("Trò chơi hòa!");
                break;
            default:
                alert.setContentText("Trạng thái không xác định!");
        }

        alert.showAndWait();
    }

    private void exitGame() {
        System.out.println("Đã thoát khỏi trò chơi");
        System.exit(0); // Thoát chương trình
    }

    public static void main(String[] args) {
        launch(args);
    }
}