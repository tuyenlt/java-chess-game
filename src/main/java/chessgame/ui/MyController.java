package chessgame.ui;



import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;

public class MyController {

    // Các AnchorPane cần điều khiển
    @FXML
    private AnchorPane firstAnchorPane;

    @FXML
    private AnchorPane secondAnchorPane;

    // Hàm xử lý sự kiện khi bấm nút trong firstAnchorPane
    @FXML
    private void handleButtonClick() {
        // Ẩn firstAnchorPane khi bấm nút trong đó
        firstAnchorPane.setVisible(false);
        firstAnchorPane.setOpacity(0); // Thêm dòng này để làm trong suốt hoàn toàn
    }

    // Hàm xử lý sự kiện khi bấm nút trong secondAnchorPane
    @FXML
    private void showFirstAnchorPane() {
        // Hiển thị lại firstAnchorPane
        firstAnchorPane.setVisible(true);
        firstAnchorPane.setOpacity(1); // Làm cho nó hiện lại bình thường
    }
}
