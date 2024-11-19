package chessgame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import chessgame.network.ClientNetwork;
import chessgame.ui.Controller;

/**
 * JavaFX App
 */
public class App extends Application {

    // private static Scene scene;

    @Override
    public void start(Stage stage) throws Exception {
        // Tải tệp FXML
        Parent root = FXMLLoader.load(getClass().getResource("mainScene.fxml"));
        Scene scene = new Scene(root);
        ClientNetwork client = new ClientNetwork(5000, 5555, 6666, "127.0.0.1");
        client.connectMainServer();
        Controller.setClient(client);
        Controller.setStage(stage);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args); // Khởi chạy ứng dụng
    }

}