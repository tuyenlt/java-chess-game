package chessgame.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class TestController {

    @FXML
    private ImageView imageView;

    // Phương thức xử lý khi nhấn nút tải ảnh
    @FXML
    private void handleLoadImage(ActionEvent event) {
        // Mở cửa sổ chọn file
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        // Hiển thị cửa sổ chọn file và lấy file đã chọn
        File selectedFile = fileChooser.showOpenDialog((Stage) imageView.getScene().getWindow());

        if (selectedFile != null) {
            // Tạo đối tượng Image từ file đã chọn
            Image image = new Image(selectedFile.toURI().toString());

            // Hiển thị ảnh lên ImageView
            imageView.setImage(image);
        }
    }
}
