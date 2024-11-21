package chessgame;
//
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//
//public class Test extends Application {
//
//    // private static Scene scene;
//
//    @Override
//    public void start(Stage stage) throws Exception {
//        // Tải tệp FXML
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("loadingIcon.fxml"));
//        Parent root = loader.load();
//
//        Scene scene = new Scene(root);
//
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args); // Khởi chạy ứng dụng
//    }
//
//}
import chessgame.network.ClientNetwork;
import chessgame.ui.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Test extends Application {

    // private static Scene scene;

    @Override
    public void start(Stage stage) throws Exception {
        // Tải tệp FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("test.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {
        launch(args); // Khởi chạy ứng dụng
    }
}
