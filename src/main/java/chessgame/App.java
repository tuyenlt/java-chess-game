package chessgame;

import chessgame.ui.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;



/**
 * JavaFX App
 */

 
public class App extends Application {

    // private static Scene scene;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainScene.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        MainController.setStage(stage);
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/chessgame/image/logo.png")));
        stage.setTitle("Chess Game");
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {
        launch(args); 
    }

}
