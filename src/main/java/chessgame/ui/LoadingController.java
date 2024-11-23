package chessgame.ui;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class LoadingController {

    public StackPane loadingStackPane;
    @FXML
    private Canvas loadingCanvas;

    private double angle = 0; // Độ dài vòng cung
    private double rotation = 0; // Góc xoay

    @FXML
    public void initialize() {
        GraphicsContext gc = loadingCanvas.getGraphicsContext2D();

        // Animation Timer để cập nhật liên tục
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                double size = Math.min(loadingCanvas.getWidth(), loadingCanvas.getHeight());
                if (size <= 10) {
                    return; // Không vẽ nếu Canvas quá nhỏ
                }

                drawRotatingArc(gc, size);
                angle += 2; // Tăng độ dài vòng cung
                if (angle > 360) {
                    angle = 0; // Reset độ dài khi đủ 360
                }
                rotation += 2; // Tăng góc xoay
                if (rotation > 360) {
                    rotation -= 360; // Reset góc xoay nếu vượt quá 360
                }
            }
        }.start();
    }

    private void drawRotatingArc(GraphicsContext gc, double size) {
        // Xóa canvas trước khi vẽ
        gc.clearRect(0, 0, loadingCanvas.getWidth(), loadingCanvas.getHeight());

        // Tọa độ tâm và bán kính vòng tròn
        double centerX = loadingCanvas.getWidth() / 2;
        double centerY = loadingCanvas.getHeight() / 2;
        double radius = size / 2.5;

        // Vẽ vòng cung xoay theo góc rotation
        gc.setStroke(Color.WHITE); // Màu xanh
        gc.setLineWidth(5); // Độ dày nét vẽ
        gc.strokeArc(
                centerX - radius, // Vị trí X (trừ bán kính để vẽ từ tâm)
                centerY - radius, // Vị trí Y
                radius * 2, // Chiều rộng
                radius * 2, // Chiều cao
                rotation, // Góc bắt đầu (xoay theo thời gian)
                angle, // Góc kết thúc (độ dài tăng dần)
                ArcType.OPEN // Kiểu vẽ (vòng cung hở)
        );
    }
}
