package chessgame.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HistoryGameController {

    @FXML private Label player1Name;
    @FXML private Label player2Name;
    @FXML private Label resultLabel;
    @FXML private Label eloChangePlayer1;
    @FXML private Label eloChangePlayer2;
    @FXML private Label gameTime;
    @FXML private Label moveCount;

    // Hàm để thiết lập dữ liệu cho từng trận đấu
    public void setGameData(String player1, String player2,
                            boolean isPlayer1Winner, boolean isDraw,
                            int moves, String gameTimeValue) {
        // Đặt tên người chơi
        player1Name.setText(player1);
        player2Name.setText(player2);

        // Xử lý kết quả trận đấu
        if (isDraw) {
            resultLabel.setText("It's a draw!");
            resultLabel.setStyle("-fx-text-fill: gray;");
            eloChangePlayer1.setText("+0");
            eloChangePlayer2.setText("+0");
        } else if (isPlayer1Winner) {
            resultLabel.setText(player1 + " won!");
            resultLabel.setStyle("-fx-text-fill: green;");
            eloChangePlayer1.setText("+80");
            eloChangePlayer2.setText("-100");
        } else {
            resultLabel.setText(player2 + " won!");
            resultLabel.setStyle("-fx-text-fill: red;");
            eloChangePlayer1.setText("-100");
            eloChangePlayer2.setText("+80");
        }

        // Đặt thời gian trận đấu
        gameTime.setText("Time: " + gameTimeValue);

        // Đặt số nước đi
        moveCount.setText("Moves: " + moves);
    }
}
