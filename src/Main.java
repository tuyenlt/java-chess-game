import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Tải tệp FXML
        Parent root = FXMLLoader.load(getClass().getResource("../resources/fxml/mainScene.fxml"));
        System.out.println("pass");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args); // Khởi chạy ứng dụng
    }
}
