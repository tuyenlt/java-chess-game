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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainScene.fxml"));
        Parent root = loader.load();
        ClientNetwork client = new ClientNetwork(10000, 5555, 6666, "3.27.120.232");
        client.setUiResponseHandler(loader.getController());
        client.connectMainServer();
        Scene scene = new Scene(root);
        Controller.setClient(client);
        Controller.setStage(stage);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args); // Khởi chạy ứng dụng
    }

}